package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.services.mitra.MitraService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/mitra")
public class RestMitraController {
    private final MitraService mitraService;

    @GetMapping("/all-talents/{mitraId}")
    @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Object> getAllTalents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @PathVariable Long mitraId
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Talents found", HttpStatus.OK, "SUCCESS", mitraService.getMitraTalents(mitraId, page, size)
        ));
    }

    @GetMapping("/dashboard-telemetry/{mitraId}")
    @PreAuthorize("hasAnyAuthority('CREATE_TALENT')")
    public ResponseEntity<Object> getDashboardTelemetry(@PathVariable Long mitraId) {
        return ResponseHandler.generateResponse(new Response(
                "Dashboard telemetry", HttpStatus.OK, "SUCCESS", mitraService.getMitraDashboardTelemetry(mitraId)
        ));
    }

}
