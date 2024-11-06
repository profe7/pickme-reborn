package me.pick.metrodata.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AnyUtil {

    private AnyUtil() {}

    public static boolean isNumberInArray(Integer[] array, Integer number) {
        for (int element : array) {
            if (element == number) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLongNumberInArray(Long[] array, Long number) {
        for (long element : array) {
            if (element == number) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static DecimalFormat salaryFormat() {
        Locale indonesiaLocale = new Locale("id", "ID");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(indonesiaLocale);
        symbols.setCurrencySymbol("Rp");

        return new DecimalFormat("###,###,###.##", symbols);
    }

    public static PageData pagination(Integer count, Integer page, Integer perPage, UriComponentsBuilder uriBuilder) {
        PageData total = new PageData();
        total.setPerPage(perPage);
        total.setTotal(count);
        try {
            total.setCurrentPage(page);
            total.setLastPage((int) Math.floor((double) count / (double) perPage));
        } catch (Exception e) {
            total.setCurrentPage(1);
            total.setLastPage(1);
        }
        String previous = total.getCurrentPage() > 0 && total.getCurrentPage() <= total.getLastPage()
                ? uriBuilder.replaceQueryParam("page", total.getCurrentPage()).toUriString()
                : "";
        String next = total.getCurrentPage() >= 0 && total.getCurrentPage() < total.getLastPage()
                ? uriBuilder.replaceQueryParam("page", total.getCurrentPage() + 2).toUriString()
                : "";
        total.setPrevious(previous);
        total.setNext(next);

        return total;
    }

}
