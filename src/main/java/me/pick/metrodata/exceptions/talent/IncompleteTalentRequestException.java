package me.pick.metrodata.exceptions.talent;

public class IncompleteTalentRequestException extends RuntimeException {
    public IncompleteTalentRequestException(String attribute) {
        super("Incomplete talent request " + attribute + " is required");
    }
}
