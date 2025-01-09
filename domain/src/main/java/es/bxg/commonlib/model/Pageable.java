package es.bxg.commonlib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable {
  private int pageNumber;
  private int pageSize;
  private String sortBy;
  private boolean ascending;

  public Pageable(int pageNumber, int pageSize, String sortBy, boolean ascending) {
    if (pageNumber < 0) {
      throw new IllegalArgumentException("Page number must not be less than 0");
    }
    if (pageSize <= 0) {
      throw new IllegalArgumentException("Page size must be greater than 0");
    }
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.sortBy = sortBy;
    this.ascending = ascending;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public String getSortBy() {
    return sortBy;
  }

  public boolean isAscending() {
    return ascending;
  }
}

