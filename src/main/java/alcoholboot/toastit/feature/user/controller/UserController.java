package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import alcoholboot.toastit.auth.jwt.repository.TokenRepository;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.service.CloudStorageService;
import alcoholboot.toastit.feature.image.service.ImageService;
import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CloudStorageService cloudStorageService;
    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final TokenRepository tokenRepository;
    private final FollowService followService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());

        log.info("로그인 템플릿 반환");

        return "feature/user/loginForm";
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
            return "feature/user/loginForm";
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

        return "feature/user/joinForm";
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

            return "feature/user/joinForm";
        }

        log.info("유저 저장 시작! 이메일: {}, 인증코드: {}", userJoinDto.getEmail(), userJoinDto.getAuthCode());

        userService.save(userJoinDto);

        log.info("유저 저장 성공! 로그인 페이지로 PRG 실행");

        return "redirect:/user/login";
    }

    /**
     * 메인 화면에서 마이페이지를 누르면 동작하는 메서드 입니다.
     * @param model : 로그인 한 사용자의 정보를 담고 있습니다.
     * @return : 내 정보를 볼 수 있는 페이지로 이동합니다.
     */
    @GetMapping("/mypages")
    public String showMyPages(Model model) {
        log.debug("myPages 로 GetMapping 들어옴!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
                model.addAttribute("notLoginUser", false);

            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "feature/user/mypageForm";
    }

    /**
     * 닉네임을 클릭하여 자신의 정보 또는 다른 사용자의 정보를 볼 수 있는 페이지로 연결해주는 메서드 입니다.
     * @param nickname : 정보를 조회 하고 싶은 사용자의 닉네임 입니다
     * @param model : 정보를 조회하고 싶은 사용자의 정보입니다.
     * @return : 사용자의 정보를 볼 수 있는 페이지로 이동합니다.
     */
    @GetMapping("/mypage")
    public String showMyPage(@RequestParam("nickname") String nickname, Model model) {
        log.debug("myPage로 GetMapping 들어옴!");

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            // 로그인한 사용자 정보 조회
            Optional<User> loggedInUserOptional = userService.findByEmail(email);
            if (loggedInUserOptional.isPresent()) {
                User loggedInUser = loggedInUserOptional.get();
                model.addAttribute("loggedInUser", loggedInUser);

                // 요청된 닉네임과 로그인한 사용자의 닉네임 비교
                if (loggedInUser.getNickname().equals(nickname)) {
                    // 자신의 정보인 경우
                    model.addAttribute("user", loggedInUser);
                    model.addAttribute("notLoginUser", false);

                } else {
                    // 다른 사용자의 정보인 경우
                    Optional<User> otherUserOptional = userService.findByNickname(nickname);

                    //접속한 user id 와 팔로우할 사람의 id 로 조회하여 FollowEntity 를 생성한다.
                    FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(
                            loggedInUser.getId(),
                            otherUserOptional.get().getId()
                    );
                    if (otherUserOptional.isPresent()) {

                        model.addAttribute("user", otherUserOptional.get());
                        model.addAttribute("notLoginUser", true);
                        if (alreadyFollow != null) {
                            //팔로우를 하고 있다면 모델에 alreadyFollow 를 담아 간다
                            model.addAttribute("alreadyFollow", alreadyFollow);
                        }
                    } else {
                        model.addAttribute("error", "다른 사용자를 찾을 수 없습니다.");
                    }
                }
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "feature/user/mypageForm";
    }

    /**
     * 사용자가 자신의 정보를 수정하기 위한 페이지로 이동하는 메서드 입니다
     * @param model : 현재 접속한 사용자의 정보를 담고 있습니다.
     * @return 회원 정보 수정이 가능한 페이지로 이동합니다.
     */
    @GetMapping("/edit")
    public String showEditPage(Model model) {
        log.debug("edit 로 GetMapping 들어옴!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            Optional<User> user = userService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
        }
        return "feature/user/editForm";
    }

    /**
     * 사용자가 정보를 변경 후 수정을 완료하기 위해 보내는 요청에 응답하는 메서드 입니다.
     * @param nickname : 사용자가 변경을 원하는 닉네임 입니다
     * @return : 변경을 완료하면 mypage 화면으로 디라이렉션 합니다
     */
    @PostMapping("/edit")
    public String editNickname(@RequestParam("nickname") String nickname) {
        log.debug("닉네임 수정 요청이 들어옴");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 닉네임 변경
                user.setNickname(nickname);
                // 변경된 사용자 정보 저장
                userService.save(user.convertToEntity());

            } else {
                return "redirect:/error"; // 사용자 없음 처리
            }
        }

        return "redirect:/user/mypages"; // 변경 후 마이 페이지로 리다이렉트
    }

    /**
     * 프로필 사진의 이미지를 변경하고자 하는 요청이 들어왔을 때 실행되는 메서드 입니다.
     * @param filePath : 사용자가 올린 이미지의 정보를 받아옵니다.
     * @return : 이미지 변경이 성공되면, 정보수정 페이지로 리다이렉션 합니다
     */
    @PostMapping("/imageChange")
    public String imageChange(@RequestParam("filePath") MultipartFile filePath) {
        log.debug("이미지 변경 PostMapping 이 들어옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            User user = userOptional.get();

            try{
                String imageUrl = cloudStorageService.uploadProfileImage(filePath);
                log.debug("AWS bucket /profile 에 이미지 업로드 성공");

                if(imageService.findByUserId(user.getId()) != null) { //DB에 user ID 가 존재하는 경우 새로 생성하지 않는다.
                    log.debug("DB에 이미지 테이블을 가지고 있는 User 가 접근");
                    ImageEntity imageEntity = imageService.findByUserId(user.getId());
                    imageEntity.setImageName(filePath.getOriginalFilename());
                    imageEntity.setImagePath(imageUrl);
                    imageEntity.setImageType(filePath.getContentType());
                    imageEntity.setImageSize(String.valueOf(filePath.getSize()));
                    imageEntity.setImageUse("profile");
                    imageService.save(imageEntity);
                } else {
                    log.debug("DB에 이미지 테이블을 가지고 있지 않은 user 가 접근");
                    ImageEntity imageEntity = new ImageEntity(); //DB에 User Id가 존재하지 않는 경우 새로운 테이블을 만들어서 저장한다
                    imageEntity.setId(user.getId());
                    imageEntity.setImageName(filePath.getOriginalFilename());
                    imageEntity.setImagePath(imageUrl);
                    imageEntity.setImageType(filePath.getContentType());
                    imageEntity.setImageSize(String.valueOf(filePath.getSize()));
                    imageEntity.setImageUse("profile");
                    imageService.save(imageEntity);
                }

                //S3에 있는 이미지를 바로 사용 가능하도록 parsing
                String newUrl = imageUrl.replace("https://s3.amazonaws.com/toastitbucket",
                        "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

                userOptional.get().setProfileImageUrl(newUrl);
                userService.save(user.convertToEntity());
                log.debug("MySQL image 에 저장성공");

                return "redirect:/user/edit" ;
            }catch(Exception e){
                log.debug("파일 업로드를 실패 하였습니다. "+e.getMessage());
                return "redirect:/user/edit" ;
            }
        }
        return "redirect:/user/eidt";
    }

    /**
     * 회원탈퇴를 위한 페이지로 매핑되는 메서드 입니다.
     * @param model : Spring MVC 모델로, 탈퇴할 회원의 정보입니다.
     * @return 회원 탈퇴가 가능한 페이지로 이동합니다
     */
    @GetMapping("/resign")
    public String resign(Model model) {
        log.debug("회원 탈퇴 폼 접속");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> userOptional = userService.findByEmail(email);

        model.addAttribute("user", userOptional.get().convertToEntity());
        return "feature/user/resignForm";
    }

    /**
     * 회원탈퇴 요청이 들어오면 처리하는 기능입니다.
     * @param request 회원 탈퇴 요청이 오면 새로운 요청을 발생시켜 새로운 쿠키를 발행합니다.
     * @param response 요청에 대한 응답에 새로운 쿠키를 저장합니다.
     * @return : 회원 탈퇴가 완료되면 메인 페이지로 리다이렉션 됩니다.
     */
    @PostMapping("/resign")
    public String resign(HttpServletRequest request, HttpServletResponse response) {
        log.debug("회원탈퇴 postMapping 요청이 옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> userOptional = userService.findByEmail(email);
        String accessToken = tokenRepository.findByUserEntityId(userOptional.get().getId())
                .map(TokenEntity::getAccessToken)
                .orElse(null);

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