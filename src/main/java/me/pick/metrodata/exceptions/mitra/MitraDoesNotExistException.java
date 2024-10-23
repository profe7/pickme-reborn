package me.pick.metrodata.exceptions.mitra;

public class MitraDoesNotExistException extends RuntimeException{
    public MitraDoesNotExistException(Long mitraId) {
        super("Mitra with id " + mitraId + " does not exist");
    }
}
