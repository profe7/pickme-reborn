package me.pick.metrodata.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import me.pick.metrodata.models.entity.Account;

import jakarta.servlet.http.HttpSession;
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
    public String home(Model model, HttpSession session) {

        Account loggedAccount = (Account) session.getAttribute("loggedAccount");

        User loggedUser = userService.getById(loggedAccount.getId());

        model.addAttribute("logged", loggedUser);

        model.addAttribute("isActive", "home");

        return "admin";
    }
}
