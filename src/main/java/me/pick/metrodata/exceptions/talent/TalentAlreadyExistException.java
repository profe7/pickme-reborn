package me.pick.metrodata.exceptions.talent;

import me.pick.metrodata.models.entity.Talent;

public class TalentAlreadyExistException extends RuntimeException {
    public TalentAlreadyExistException(String name) {
        super("Talent by the name of " + name + " with requested NIK already exists. Please use a different NIK");
    }
}
