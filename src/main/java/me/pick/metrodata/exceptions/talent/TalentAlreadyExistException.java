package me.pick.metrodata.exceptions.talent;

public class TalentAlreadyExistException extends RuntimeException {

    public TalentAlreadyExistException(String name) {
        super("Talent by the name of " + name + " with requested NIK already exists. Please use a different NIK");
    }
}
