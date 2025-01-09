package es.bxg.commonlib.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;

  public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
    this.content = content;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
  }

  public List<T> getContent() {
    return content;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return (int) Math.ceil((double) totalElements / pageSize);
  }
}

