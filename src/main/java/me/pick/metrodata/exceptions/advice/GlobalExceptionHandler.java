package me.pick.metrodata.exceptions.advice;

import me.pick.metrodata.exceptions.Applicant.ApplicantAlreadyExistsException;
import me.pick.metrodata.exceptions.account.AccountAlreadyExistException;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.exceptions.institute.InstituteDoesNotExistException;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.exceptions.talent.InvalidTalentNikException;
import me.pick.metrodata.exceptions.talent.TalentAlreadyExistException;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String FAILED = "FAILED";

    @ExceptionHandler(value = {
            InstituteDoesNotExistException.class,
            TalentDoesNotExistException.class,
            MitraDoesNotExistException.class,
            RoleDoesNotExistException.class,
            VacancyNotExistException.class,
            AccountDoesNotExistException.class
    })
    public ResponseEntity<Object> resourceNotAvailableHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.NOT_FOUND, FAILED, null));
    }

    @ExceptionHandler(value = {
            TalentAlreadyExistException.class,
            ApplicantAlreadyExistsException.class,
            AccountAlreadyExistException.class
    })
    public ResponseEntity<Object> resourceAlreadyExistHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.CONFLICT, FAILED, null));
    }

    @ExceptionHandler(value = {
            InvalidTalentNikException.class,
            AccountInvalidPasswordException.class
    })
    public ResponseEntity<Object> badRequestHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.BAD_REQUEST, FAILED, null));
    }
}
