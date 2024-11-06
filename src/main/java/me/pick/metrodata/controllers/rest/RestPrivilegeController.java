package me.pick.metrodata.controllers.rest;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.services.privilege.PrivilegeService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/privilege")
@RequiredArgsConstructor
public class RestPrivilegeController {

    private final PrivilegeService privilegeService;
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('READ_PRIVILEGE')")
    public ResponseEntity<Object> getAllPrivilege() {
        return ResponseHandler.generateResponse(new Response(
                "Privileges", HttpStatus.OK, "SUCCESS", privilegeService.getAll()
        ));
    }
}
