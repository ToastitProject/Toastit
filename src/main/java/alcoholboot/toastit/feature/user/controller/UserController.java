package alcoholboot.toastit.feature.user.controller;


import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import alcoholboot.toastit.auth.jwt.repository.TokenRepository;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.ImageService;
import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final S3imageUploadService s3imageUploadService;
    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final TokenRepository tokenRepository;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());

        log.info("로그인 템플릿 반환");

        return "/feature/user/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid UserLoginRequest userLoginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {
        log.info(userLoginDto.getEmail() + " 해당 이메일이 로그인 요청을 하였습니다.");

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error("Validation error: {}", error.getDefaultMessage()));
            return "/feature/user/loginForm";
        }

        User user = userService.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD));

        log.info(user.getNickname() + "님이 로그인 하였습니다.");

        // 비밀번호 일치여부 체크
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD);
        }

        // 액세스 토큰 발급
        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        log.info("액세스 토큰 발급 완료 -> " + accessToken);

        // 리프레쉬 토큰 발급
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        log.info("리프레쉬 토큰 발급 완료 -> " + refreshToken);

        // 리프레시 토큰 디비 저장
        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        tokenService.saveOrUpdate(token);

        // 액세스 토큰 쿠키 생성 및 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        response.addCookie(accessTokenCookie);

        // 리프레쉬 토큰 쿠키 생성 및 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.refreshTokenExpire / 1000));
        response.addCookie(refreshTokenCookie);

        model.addAttribute("user", user);

        log.info("메인 홈페이지 PRG 실행");

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃 실행");

        String accessToken = null;

        // access 및 refresh token cookie 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "accessToken":
                        accessToken = cookie.getValue();
                    case "refreshToken":
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                }
            }
        }

        // tokens 데이터 삭제
        tokenService.deleteByAccessToken(accessToken);

        log.info("로그아웃 실행 후 메인 페이지로 RG 실행");

        return "redirect:/";
    }

    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());

        log.info("회원가입 템플릿 반환");

        return "/feature/user/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid UserJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        log.info("회원가입 요청");

        if (bindingResult.hasErrors()) {
            // 글로벌 오류 출력
            bindingResult.getGlobalErrors().forEach(error -> log.error("GLOBAL ERROR : {}", error.getDefaultMessage()));

            // 필드 오류 출력 및 passwordMatching 필드 오류가 있는지 확인
            bindingResult.getFieldErrors().forEach(error -> {
                log.error("{} : {}", error.getField(), error.getDefaultMessage());

                // passwordMatching 필드가 존재할 경우, 모델에 추가
                if ("passwordMatching".equals(error.getField())) {
                    model.addAttribute("passwordMatchingError", error.getDefaultMessage());
                }
            });

            return "/feature/user/joinForm";
        }

        log.info("유저 저장 시작! 이메일: {}, 인증코드: {}", userJoinDto.getEmail(), userJoinDto.getAuthCode());

        userService.save(userJoinDto);

        log.info("유저 저장 성공! 로그인 페이지로 PRG 실행");

        return "redirect:/user/login";
    }

    @GetMapping("/mypage")
    public String showMyPage(Model model) {
        log.info("myPage 로 GetMapping 들어옴!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
                log.info("찾은 user email 을 모델에 담은 값 : " + userOptional.get().getEmail());
                log.info("찾은 user Nickname 을 모델에 담은 값 : " + userOptional.get().getNickname());
                log.info("찾은 user create_date 를 모델에 담은 값 : " + userOptional.get().getCreateDate());
                log.info("이미지 url : " +userOptional.get().getProfileImageUrl());
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "/feature/user/mypageForm";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model) {
        log.info("edit 로 GetMapping 들어옴!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> user = userService.findByEmail(email);
            log.info(email + "로 user 를 찾는다.");
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                log.info(email+"로 찾은 user 의 정보를 화면에 보여준다");
                log.info(user.get().getEmail());
                log.info(user.get().getNickname());
                log.info(user.get().getProfileImageUrl());
            }
        }
        return "/feature/user/editForm";
    }

    @PostMapping("/edit")
    public String editNickname(@RequestParam("nickname") String nickname) {
        log.info("닉네임 수정 요청이 들어옴");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                log.info("기존 닉네임: " + user.getNickname());
                log.info("새로운 닉네임: " + nickname);

                // 닉네임 변경
                user.setNickname(nickname);
                userService.save(user.convertToEntity()); // 변경된 사용자 정보 저장
                log.info("닉네임이 변경됨: " + user.getNickname());
            } else {
                log.warn("사용자를 찾을 수 없습니다: " + email);
                return "redirect:/error"; // 사용자 없음 처리
            }
        }

        return "redirect:/user/mypage"; // 변경 후 마이 페이지로 리다이렉트
    }

    @PostMapping("/imageChange")
    public String imageChange(@RequestParam("filePath") MultipartFile filePath) {
        log.info("이미지 변경 PostMapping 이 들어옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            User user = userOptional.get();
            log.info("접속한 user 의 DB 에 저장된 프로필사진 경로"+user.getProfileImageUrl());

            try{
                log.info("upload try start!!!");
                String imageUrl = s3imageUploadService.uploadProfileImage(filePath);
                log.info("AWS bucket /profile 에 이미지 업로드 성공");

                if(imageService.findByUserId(user.getId()) != null) { //DB에 user ID 가 존재하는 경우 새로 생성하지 않는다.
                    log.info("DB에 이미지 테이블을 가지고 있는 User 가 접근");
                    Image image = imageService.findByUserId(user.getId());
                    image.setImageName(filePath.getOriginalFilename());
                    image.setImagePath(imageUrl);
                    image.setImageType(filePath.getContentType());
                    image.setImageSize(String.valueOf(filePath.getSize()));
                    image.setImageUse("profile");
                    imageService.save(image);
                } else {
                    log.info("DB에 이미지 테이블을 가지고 있지 않은 user 가 접근");
                    Image image = new Image(); //DB에 User Id가 존재하지 않는 경우 새로운 테이블을 만들어서 저장한다
                    image.setId(user.getId());
                    image.setImageName(filePath.getOriginalFilename());
                    image.setImagePath(imageUrl);
                    image.setImageType(filePath.getContentType());
                    image.setImageSize(String.valueOf(filePath.getSize()));
                    image.setImageUse("profile");
                    imageService.save(image);
                }

                String newUrl = imageUrl.replace("https://s3.amazonaws.com/toastitbucket",
                        "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

                userOptional.get().setProfileImageUrl(newUrl);
                userService.save(user.convertToEntity());
                log.info("MySQL image 에 저장성공");

                return "redirect:/user/edit" ;
            }catch(Exception e){
                System.out.println("파일 업로드 실패"+e.getMessage());
                return "redirect:/user/edit" ;
            }
        }
        return "redirect:/user/eidt";
    }

    @GetMapping("/resign")
    public String resign(Model model) {
        log.info("회원 탈퇴 폼 접속");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userService.findByEmail(email);
        log.info("접속한 사용자의 이메일 : "+userOptional.get().getEmail());
        model.addAttribute("user", userOptional.get().convertToEntity());
        log.info("Model에 담긴 닉네임 값 : "+ userOptional.get().convertToEntity().getNickname());
        return "/feature/user/resignForm";
    }

    @PostMapping("/resign")
    public String resign(HttpServletRequest request, HttpServletResponse response) {
        log.info("회원탈퇴 postMapping 요청이 옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("접속한 사용자의 이메일 : " + email);
        Optional<User> userOptional = userService.findByEmail(email);
        String accessToken = tokenRepository.findByUserEntityId(userOptional.get().getId())
                .map(TokenEntity::getAccessToken)
                .orElse(null);
        log.info("찾은 Access Token ID: " + accessToken);

        // 쿠키 제거
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName()) || "refreshToken".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        if (accessToken != null) {
            tokenService.deleteByAccessToken(accessToken);
        }

        userService.deleteByEmail(email);
        log.info("회원 탈퇴 완료");
        return "redirect:/";
    }

}