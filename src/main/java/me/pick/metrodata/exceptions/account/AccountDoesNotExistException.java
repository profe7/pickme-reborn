package me.pick.metrodata.exceptions.account;

public class AccountDoesNotExistException extends RuntimeException{
    public AccountDoesNotExistException(String username) {
        super("Account with username " + username + " does not exist");
    }
}
