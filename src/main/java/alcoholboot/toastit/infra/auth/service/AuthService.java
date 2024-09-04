package alcoholboot.toastit.infra.auth.service;

import alcoholboot.toastit.infra.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.infra.auth.controller.request.AuthLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

/**
 * 인증 관련 서비스 인터페이스.
 * 사용자 로그인, 로그아웃, 회원가입, 회원 탈퇴 등의 기능을 정의합니다.
 */
public interface AuthService {

    /**
     * 사용자 로그인 처리.
     *
     * @param authLoginRequest 로그인 요청 데이터
     * @param response HTTP 응답 객체
     * @return 로그인한 사용자 객체
     */
    User login(AuthLoginRequest authLoginRequest, HttpServletResponse response);

    /**
     * 사용자 로그아웃 처리.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 사용자 회원가입 처리.
     *
     * @param authJoinRequest 회원가입 요청 데이터
     * @param bindingResult 유효성 검증 결과
     */
    void registerUser(AuthJoinRequest authJoinRequest, BindingResult bindingResult);

    /**
     * 탈퇴 요청한 사용자 정보를 조회.
     *
     * @return 탈퇴 요청한 사용자 객체
     */
    User getResignUser();

    /**
     * 사용자 탈퇴 처리.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    void resignUser(HttpServletRequest request, HttpServletResponse response);
}