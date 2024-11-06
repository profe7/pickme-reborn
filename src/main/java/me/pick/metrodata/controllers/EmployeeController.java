package me.pick.metrodata.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/employee")
@AllArgsConstructor

public class EmployeeController {
    @GetMapping
    public String landingPage() {
        return "employee/employee";
    }
    
}
