package me.pick.metrodata.exceptions.Applicant;

public class ApplicantAlreadyExistsException extends RuntimeException{
    public ApplicantAlreadyExistsException(String applicantName) {
        super("Applicant with name " + applicantName + " already exists");
    }
}
