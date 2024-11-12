// package me.pick.metrodata.controllers;


// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import lombok.AllArgsConstructor;
// import me.pick.metrodata.services.recommendation.RecommendationService;
// @Controller
// @RequestMapping("/recommendation")
// @AllArgsConstructor
// public class RecommendationController {
//     private RecommendationService recommendationService;

//     @GetMapping
//     public String recommendation(Model model) {
//         // System.out.println("recommendation.check");
//         // model.addAttribute("recommendations", recommendationService.getAll());
//         // model.addAttribute("isActive", "recommendation");
//         // recommendationService.getAll().forEach(s -> System.out.println(s.getId()));

//         return "client/recomendation";
        
//     }
    
// }





    
package me.pick.metrodata.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/recommendation")
@AllArgsConstructor
public class RecommendationController {

    @GetMapping
    public String recommendation(Model model) {
        // System.out.println("recommendation.check");
        // model.addAttribute("recommendations", recommendationService.getAll());
        // model.addAttribute("isActive", "recommendation");
        // recommendationService.getAll().forEach(s -> System.out.println(s.getId()));

        return "client/recommendation";
        
    }
}