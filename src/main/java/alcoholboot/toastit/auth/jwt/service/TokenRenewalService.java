package alcoholboot.toastit.auth.jwt.service;

import jakarta.servlet.http.HttpServletResponse;

public interface TokenRenewalService {
    void refreshAccessToken(HttpServletResponse response, String refreshToken);
}