package me.pick.metrodata.exceptions.interviewschedule;

public class InterviewScheduleDoesNotExistException extends RuntimeException {
    public InterviewScheduleDoesNotExistException(Long id) {
        super("Interview schedule with id " + id + " does not exist");
    }
}
