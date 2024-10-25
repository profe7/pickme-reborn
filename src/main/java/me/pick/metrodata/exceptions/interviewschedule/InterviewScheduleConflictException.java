package me.pick.metrodata.exceptions.interviewschedule;

public class InterviewScheduleConflictException extends RuntimeException{
    public InterviewScheduleConflictException(String startTime, String endTime, String date) {
        super("An interview is already scheduled between " + startTime + " and " + endTime + " on " + date + " or you already invited this applicant to an interview.");
    }
}
