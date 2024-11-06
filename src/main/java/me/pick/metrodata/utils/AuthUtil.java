package me.pick.metrodata.utils;

import me.pick.metrodata.models.entity.AccountDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    private AuthUtil() {}

    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AccountDetail appUserDetail) {
                return appUserDetail.getId();
            }
        }
        return null;
    }
}