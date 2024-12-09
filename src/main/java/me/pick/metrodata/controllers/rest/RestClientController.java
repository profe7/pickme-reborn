package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.services.client.ClientService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@AllArgsConstructor
public class RestClientController {

    private final ClientService clientService;

    private static final String SUCCESS = "SUCCESS";

    @GetMapping("/employees/{clientId}")
    @PreAuthorize("hasAnyAuthority('READ_APPLICANT')")
    public ResponseEntity<Object> getClientEmployees(@PathVariable Long clientId) {
        return ResponseHandler.generateResponse(new Response(
                "Employees found", HttpStatus.OK, SUCCESS, clientService.getClientEmployees(clientId)
        ));
    }

    @DeleteMapping("/employees/{clientId}/delete/{talentId}")
    @PreAuthorize("hasAnyAuthority('DELETE_APPLICANT')")
    public ResponseEntity<Object> deleteClientEmployee(@PathVariable Long clientId, @PathVariable String talentId) {
        clientService.deleteClientEmployee(clientId, talentId);
        return ResponseHandler.generateResponse(new Response(
                "Employee deleted", HttpStatus.OK, SUCCESS, null
        ));
    }

    @GetMapping("/dashboard-telemetry/{clientId}")
    @PreAuthorize("hasAnyAuthority('READ_APPLICANT')")
    public ResponseEntity<Object> getClientDashboardTelemetry(@PathVariable Long clientId) {
        return ResponseHandler.generateResponse(new Response(
                "Dashboard telemetry", HttpStatus.OK, SUCCESS, clientService.getClientDashboardTelemetry(clientId)
        ));
    }
}
