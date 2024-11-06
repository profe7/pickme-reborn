package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/institute")
@AllArgsConstructor
public class RestInstituteController {

    private final InstituteService instituteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READ_INSTITUTE')")
    public ResponseEntity<Object> getInstituteById(@PathVariable Long id) {
        Institute institute = instituteService.getInstituteById(id);
        return ResponseHandler.generateResponse(new Response(
                "Institute found", HttpStatus.OK, "SUCCESS", institute
        ));
    }

    @GetMapping("/all-institutes")
    @PreAuthorize("hasAnyAuthority('READ_INSTITUTE')")
    public ResponseEntity<Object> getAllInstitutes(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Long instituteTypeId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Institutes found", HttpStatus.OK, "SUCCESS", instituteService.getAllInstitutes(
                        name, instituteTypeId, page, size
                )
        ));
    }

}
