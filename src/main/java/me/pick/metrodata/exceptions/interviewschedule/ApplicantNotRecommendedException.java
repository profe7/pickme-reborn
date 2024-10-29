package me.pick.metrodata.exceptions.interviewschedule;

public class ApplicantNotRecommendedException extends RuntimeException{
    public ApplicantNotRecommendedException(Long id, String position) {
        super("Applicant " + id + " has not been recommended " + position + " position");
    }
}
