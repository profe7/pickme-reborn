package me.pick.metrodata.utils;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.AccountDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class AuthUtil {
  public static Long getLoginUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof AccountDetail) {
        AccountDetail appUserDetail = (AccountDetail) principal;
        Long userId = appUserDetail.getId();
        return userId;
      } else {
        return null;
      }
    }
    return null;
  }
}
