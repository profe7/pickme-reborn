// package me.pick.metrodata.controllers;
// import java.util.List;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;

// import lombok.AllArgsConstructor;
// import me.pick.metrodata.models.entity.Talent;
// import me.pick.metrodata.services.client.ClientService;

// @Controller
// @RequestMapping("/employee")
// @AllArgsConstructor
// public class EmployeeController {

//     private final ClientService clientService;
    

//     @GetMapping("/{clientId}")
//     public String landingPage(@PathVariable Long clientId, Model model) {
//         List<Talent> getEmployee = clientService.getClientEmployees(clientId);

//         model.addAttribute("employee", getEmployee);
//         return "client/employee";
//     }
    
// }
