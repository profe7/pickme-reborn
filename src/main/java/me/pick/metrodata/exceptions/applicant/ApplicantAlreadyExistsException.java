package me.pick.metrodata.exceptions.applicant;

public class ApplicantAlreadyExistsException extends RuntimeException{
    public ApplicantAlreadyExistsException(String applicantName) {
        super("Applicant with name " + applicantName + " already exists");
    }
}
