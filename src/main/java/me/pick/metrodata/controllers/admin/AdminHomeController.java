package me.pick.metrodata.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.user.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {

    private final UserService userService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public String home(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);

        model.addAttribute("isActive", "home");

        return "admin";
    }
}
