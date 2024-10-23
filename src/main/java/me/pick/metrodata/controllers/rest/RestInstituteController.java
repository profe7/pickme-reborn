package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/institute")
public class RestInstituteController {

    private final InstituteService instituteService;

    public RestInstituteController(InstituteService instituteService) {
        this.instituteService = instituteService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READ_INSTITUTE')")
    public ResponseEntity<Object> getInstituteById(@PathVariable Long id) {
        Institute institute = instituteService.getInstituteById(id);
        return ResponseHandler.generateResponse(new Response(
                "Institute found", HttpStatus.OK, "SUCCESS", institute
        ));
    }
}
