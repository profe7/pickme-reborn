// File: src/main/java/me/pick/metrodata/utils/PaginationUtil.java
package me.pick.metrodata.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationUtil {

    private PaginationUtil() {}

    public static <T> List<T> undoPagination(Page<T> page) {
        return page.getContent();
    }
}