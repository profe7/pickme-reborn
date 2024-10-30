package me.pick.metrodata.exceptions.vacancy;

public class IncompleteVacancyRequestException extends RuntimeException{
    public IncompleteVacancyRequestException() {
        super("One or more fields are empty or invalid. Please check your request.");
    }
}
