package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.services.account.AccountService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class RestAccountController {
    private final AccountService accountService;

    public RestAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_CREATE_ACCOUNT', 'EXTERNAL_CREATE_ACCOUNT')")
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request) {
        return ResponseHandler.generateResponse(new Response(
                "Account created", HttpStatus.CREATED, "SUCCESS", accountService.createAccount(request)
        ));
    }

    @GetMapping("/available-accounts")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAllAvailableAccounts(
            @RequestParam(defaultValue = "0") Integer page
            ,@RequestParam(defaultValue = "10") Integer size) {
        return ResponseHandler.generateResponse(new Response(
                "Available accounts", HttpStatus.OK, "SUCCESS", accountService.getAllAvailableAccounts(page, size)
        ));
    }
}
