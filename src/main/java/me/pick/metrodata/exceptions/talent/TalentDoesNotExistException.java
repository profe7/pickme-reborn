package me.pick.metrodata.exceptions.talent;

public class TalentDoesNotExistException extends RuntimeException {
    public TalentDoesNotExistException(Long id) {
        super("Talent with id " + id + " does not exist");
    }
}
