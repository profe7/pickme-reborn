package me.pick.metrodata.enums;

public enum EmailSubjectEnum {
    INVITATION_INTERNAL_NOTICE("Interview Invitation Notice"),
    INVITATION_SELF_NOTICE("Interview Schedule Notice"),
    INVITATION_RESCHEDULE_INTERNAL_NOTICE("Interview Reschedule Notice"),
    INVITATION_RESCHEDULE_SELF_NOTICE("Interview Reschedule Notice"),
    CANCELLED_INTERNAL_NOTICE("Interview Cancelled Notice"),
    REJECTED_INTERNAL_NOTICE("Rejected Notice"),
    ACCEPTED_INTERNAL_NOTICE("Accepted Notice"),
    REMINDER_SELF_NOTICE("Interview Schedule Reminder");

    public final String label;

    private EmailSubjectEnum(String label) {
        this.label = label;
    }
}
