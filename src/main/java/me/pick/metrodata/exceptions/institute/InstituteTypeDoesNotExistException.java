package me.pick.metrodata.exceptions.institute;

public class InstituteTypeDoesNotExistException extends RuntimeException {
    public InstituteTypeDoesNotExistException(String instituteType) {
        super("Institute type with name " + instituteType + " does not exist");
    }
}
