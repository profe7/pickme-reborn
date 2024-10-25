package me.pick.metrodata.exceptions.applicant;

public class ApplicantDoesNotExistException extends RuntimeException{
    public ApplicantDoesNotExistException(Long applicantId) {
        super("Applicant with id " + applicantId + " does not exist");
    }
}
