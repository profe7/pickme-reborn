package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.services.recommendation.RecommendationService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/recommendation")
public class RestRecommendationController {

    private final RecommendationService recommendationService;

    private static final String SUCCESS = "SUCCESS";

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_INTERVIEW')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseHandler.generateResponse(new Response(
                "Recommendation deleted", HttpStatus.OK, SUCCESS, null
        ));
    }
}
