package me.pick.metrodata.exceptions.vacancy;

public class VacancyStatusDoesNotExistException extends RuntimeException {
    public VacancyStatusDoesNotExistException(String status) {
        super("Vacancy status " + status + " does not exist");
    }
}
