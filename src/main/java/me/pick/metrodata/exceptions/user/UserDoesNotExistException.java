package me.pick.metrodata.exceptions.user;

public class UserDoesNotExistException extends  RuntimeException{
    public UserDoesNotExistException(String userId) {
        super("User with id " + userId + " does not exist");
    }
}
