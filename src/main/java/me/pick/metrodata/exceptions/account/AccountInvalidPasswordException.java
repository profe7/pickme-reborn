package me.pick.metrodata.exceptions.account;

public class AccountInvalidPasswordException extends RuntimeException{
    public AccountInvalidPasswordException() {
        super("Wrong password, please try again");
    }
}
