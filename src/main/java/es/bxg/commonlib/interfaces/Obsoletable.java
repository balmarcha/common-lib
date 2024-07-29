package es.bxg.commonlib.interfaces;

public interface Obsoletable {
  void setObsolete(boolean obsolete);

  boolean isObsolete();

  default void markAsObsolete() {
    setObsolete(true);
  }
}
