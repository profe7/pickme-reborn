package me.pick.metrodata.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.models.entity.AccountDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AccountDetail accountDetail = (AccountDetail) authentication.getPrincipal();

        String redirectUrl = "/";
        if ("CLIENT".equals(accountDetail.getAccount().getRole().getName())) {
            redirectUrl = "/client";
        } else if ("MITRA".equals(accountDetail.getAccount().getRole().getName())) {
            request.getSession().setAttribute("mitraId", accountDetail.getAccount().getUser().getMitra().getId());
            redirectUrl = "/mitra";
        } else if ("ADMIN".equals(accountDetail.getAccount().getRole().getName())) {
            redirectUrl = "/admin";
        }

        response.sendRedirect(redirectUrl);
    }
}