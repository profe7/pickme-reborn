package me.pick.metrodata.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@NoArgsConstructor
public class PageData {
  private int total, perPage, currentPage, lastPage;
  private String previous, next;

  public PageData(int total, int perPage, int currentPage, int lastPage, String previous, String next) {
    this.total = total;
    this.perPage = perPage;
    this.currentPage = currentPage;
    this.lastPage = lastPage;
    this.previous = previous;
    this.next = next;
  }

  public static PageData pagination(long totalElements, int currentPage, int pageSize, UriComponentsBuilder uriBuilder) {
    int lastPage = (int) Math.ceil((double) totalElements / pageSize);

    String previous = currentPage > 0 ? uriBuilder.replaceQueryParam("currentPage", currentPage - 1).toUriString() : null;
    String next = currentPage < lastPage - 1 ? uriBuilder.replaceQueryParam("currentPage", currentPage + 1).toUriString() : null;

    return new PageData((int) totalElements, pageSize, currentPage, lastPage, previous, next);
  }

}