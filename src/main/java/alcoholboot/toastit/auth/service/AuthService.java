package alcoholboot.toastit.auth.service;

import alcoholboot.toastit.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.auth.controller.request.AuthLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

public interface AuthService {

    User login(AuthLoginRequest authLoginRequest, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void registerUser(AuthJoinRequest authJoinRequest, BindingResult bindingResult);

    User getResignUser();

    void resignUser(HttpServletRequest request, HttpServletResponse response);
}