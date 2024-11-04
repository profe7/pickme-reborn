package me.pick.metrodata.exceptions.talent;

public class IncompleteTalentCvException extends RuntimeException {
    public IncompleteTalentCvException() {
        super("Talent CV is incomplete");
    }
}
