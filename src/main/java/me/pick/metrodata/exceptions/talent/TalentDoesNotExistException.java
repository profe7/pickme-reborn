package me.pick.metrodata.exceptions.talent;

public class TalentDoesNotExistException extends RuntimeException {
    public TalentDoesNotExistException(String id) {
        super("Talent with id " + id + " does not exist");
    }
}
