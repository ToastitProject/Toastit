package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.UserProfileImageService;
import alcoholboot.toastit.feature.user.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserManagementService userManagementService;
    private final FollowService followService;
    private final UserProfileImageService userProfileImageService;

    //홈 화면에서 마이페이지 접속하는 컨트롤러
    @GetMapping("/mypages")
    public String showMyPages(Model model) {
        log.info("myPages 로 GetMapping 들어옴!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            Optional<User> userOptional = userManagementService.findByEmail(email);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
                model.addAttribute("notLoginUser", false);
                log.debug("찾은 사용자 이메일을 모델에 담았습니다: " + userOptional.get().getEmail());
                log.debug("찾은 사용자 닉네임을 모델에 담았습니다: " + userOptional.get().getNickname());
                log.debug("찾은 사용자 생성일자를 모델에 담았습니다: " + userOptional.get().getCreateDate());
                log.debug("이미지 URL: " + userOptional.get().getProfileImageUrl());
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "user/mypage-form";
    }

    //닉네임을 클릭해서 마이페이지로 접속하는 컨트롤러
    @GetMapping("/mypage")
    public String showMyPage(@RequestParam("nickname") String nickname, Model model) {
        log.debug("myPage로 GetMapping 요청이 들어왔습니다.");

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            // 로그인한 사용자 정보 조회
            Optional<User> loggedInUserOptional = userManagementService.findByEmail(email);
            if (loggedInUserOptional.isPresent()) {
                User loggedInUser = loggedInUserOptional.get();
                model.addAttribute("loggedInUser", loggedInUser);

                log.debug("로그인한 사용자의 이메일: " + loggedInUser.getEmail());
                log.debug("로그인한 사용자의 닉네임: " + loggedInUser.getNickname());

                // 요청된 닉네임과 로그인한 사용자의 닉네임 비교
                if (loggedInUser.getNickname().equals(nickname)) {
                    // 자신의 정보인 경우
                    model.addAttribute("user", loggedInUser);
                    model.addAttribute("notLoginUser", false);
                    log.debug("본인의 정보를 보여드립니다.");
                } else {
                    // 다른 사용자의 정보인 경우
                    Optional<User> otherUserOptional = userManagementService.findByNickname(nickname);

                    //접속한 user id와 팔로우할 사람의 id로 조회하여 FollowEntity를 생성합니다.
                    FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(
                            loggedInUser.getId(),
                            otherUserOptional.get().getId()
                    );
                    if (otherUserOptional.isPresent()) {
                        model.addAttribute("user", otherUserOptional.get());
                        model.addAttribute("notLoginUser", true);
                        log.debug("다른 사용자의 정보를 보여드립니다: " + nickname);
                        if (alreadyFollow != null) {
                            //팔로우를 하고 있다면 모델에 alreadyFollow를 담아갑니다.
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

        return "user/mypage-form";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model) {
        log.debug("edit로 GetMapping 요청이 들어왔습니다.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            Optional<User> user = userManagementService.findByEmail(email);
            log.debug(email + "로 사용자를 찾습니다.");

            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                log.debug(email + "로 찾은 사용자의 정보를 화면에 보여드립니다.");
                log.debug(user.get().getEmail());
                log.debug(user.get().getNickname());
                log.debug(user.get().getProfileImageUrl());
            }
        }

        return "user/edit-form";
    }

    @PostMapping("/edit")
    public String editNickname(@RequestParam("nickname") String nickname) {
        log.debug("닉네임 수정 요청이 들어왔습니다.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                log.debug("기존 닉네임: " + user.getNickname());
                log.debug("새로운 닉네임: " + nickname);

                // 닉네임 변경
                user.setNickname(nickname);
                userManagementService.save(user.convertToEntity()); // 변경된 사용자 정보 저장
                log.debug("닉네임이 변경되었습니다: " + user.getNickname());
            } else {
                log.debug("사용자를 찾을 수 없습니다: " + email);
                return "redirect:/error"; // 사용자 없음 처리
            }
        }

        return "redirect:/user/mypages"; // 변경 후 마이 페이지로 리다이렉트
    }

    @PostMapping("/imageChange")
    public String imageChange(@RequestParam("filePath") MultipartFile filePath) {
       log.debug("이미지 변경 PostMapping 이 들어옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            try {
                userProfileImageService.changeUserProfileImage(email, filePath);
               log.debug("이미지 변경 완료");
                return "redirect:/user/edit";
            } catch (Exception e) {
                log.error("파일 업로드 실패: {}", e.getMessage(), e);
                return "redirect:/user/edit";
            }
        }
        return "redirect:/user/edit";
    }
}