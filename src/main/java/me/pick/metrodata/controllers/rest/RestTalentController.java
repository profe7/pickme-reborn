package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/talent")
public class RestTalentController {

    private final TalentService talentService;

    public RestTalentController(TalentService talentService){
        this.talentService = talentService;
    }

    @PostMapping("/create-via-vacancy")
    @PreAuthorize("hasAnyAuthority('CREATE_TALENT', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> createTalentViaVacancy(@RequestBody TalentFromVacancyRequest request){
        Talent talent = talentService.createViaVacancy(request);
        return ResponseHandler.generateResponse(new Response(
                "Talent created", HttpStatus.OK, "SUCCESS", talent));
    }

    @PostMapping("/complete-new-talent-data")
    @PreAuthorize("hasAnyAuthority('READ_TALENT', 'UPDATE_TALENT')")
    public ResponseEntity<Object> completeNewTalentData(@RequestBody TalentDataCompletionRequest request){
        Talent talent = talentService.completeNewTalentData(request);
        return ResponseHandler.generateResponse(new Response(
                "Talent data completed", HttpStatus.OK, "SUCCESS", talent));
    }
}
