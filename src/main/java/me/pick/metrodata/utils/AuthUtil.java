package me.pick.metrodata.utils;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.AccountDetail;
import me.pick.metrodata.services.auth.AuthServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class AuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Authentication is set and authenticated.");
            Object principal = authentication.getPrincipal();
            if (principal instanceof AccountDetail appUserDetail) {
                Long userId = appUserDetail.getId();
                logger.info("Logged in user ID: " + userId);
                return userId;
            } else {
                logger.warn("Principal is not of type AccountDetail.");
            }
        } else {
            logger.warn("Authentication is null or not authenticated.");
        }
        return null;
    }
}
