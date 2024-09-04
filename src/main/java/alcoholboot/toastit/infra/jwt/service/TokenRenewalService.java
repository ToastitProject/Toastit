package alcoholboot.toastit.infra.jwt.service;

import jakarta.servlet.http.HttpServletResponse;

public interface TokenRenewalService {
    String refreshAccessToken(HttpServletResponse response, String refreshToken);
}