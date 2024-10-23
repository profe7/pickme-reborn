package me.pick.metrodata.exceptions.talent;

import me.pick.metrodata.models.entity.Talent;

public class TalentAlreadyExistException extends RuntimeException {
    public TalentAlreadyExistException(String name) {
        super("Talent with email " + name + " already exist");
    }
}
