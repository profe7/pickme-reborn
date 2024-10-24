package me.pick.metrodata.exceptions.talent;

public class InvalidTalentNikException extends RuntimeException{
    public InvalidTalentNikException(String nik) {
        super("The NIK given (" + nik + ") is invalid");
    }
}
