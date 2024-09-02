package alcoholboot.toastit.auth.service;

import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

public interface AuthService {

    User login(UserLoginRequest userLoginRequest, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void registerUser(UserJoinRequest userJoinRequest, BindingResult bindingResult);

    User getResignUser();

    void resignUser(HttpServletRequest request, HttpServletResponse response);
}