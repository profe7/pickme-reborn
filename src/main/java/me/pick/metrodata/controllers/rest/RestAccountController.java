package me.pick.metrodata.controllers.rest;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RestAccountController {

    private final AccountService accountService;

    private static final String SUCCESS = "SUCCESS";

    @PostMapping("/create-account")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_CREATE_ACCOUNT', 'EXTERNAL_CREATE_ACCOUNT')")
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request) {
        return ResponseHandler.generateResponse(new Response(
                "Account created", HttpStatus.CREATED, SUCCESS, accountService.createAccount(request)
        ));
    }

    @GetMapping("/available-accounts")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAllAvailableAccounts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Long institute,
            @RequestParam(required = false) Long baseBudget,
            @RequestParam(required = false) Long limitBudget
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Available accounts", HttpStatus.OK,  SUCCESS, accountService.getAllAvailableAccounts(page, size, search, institute, baseBudget, limitBudget)
        ));
    }

    @GetMapping("/available-accounts-rm")
    @PreAuthorize("hasAnyAuthority('EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAllAvailableAccountsByRM(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Long institute,
            @RequestParam(required = false) Long baseBudget,
            @RequestParam(required = false) Long limitBudget
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Available accounts", HttpStatus.OK, SUCCESS, accountService.getAvailableAccountsOfRm(page, size, search, institute, baseBudget, limitBudget)
        ));
    }

    @PostMapping("/edit-account/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_UPDATE_ACCOUNT', 'EXTERNAL_UPDATE_ACCOUNT')")
    public ResponseEntity<Object> editAccount(@PathVariable Long id, @RequestBody AccountRequest request) {
        return ResponseHandler.generateResponse(new Response(
                "Account updated", HttpStatus.OK, SUCCESS, accountService.editAccount(id, request)
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id) {
        return ResponseHandler.generateResponse(new Response(
                "Account found", HttpStatus.OK, SUCCESS, accountService.getAccountById(id)
        ));
    }
}
