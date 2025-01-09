package es.bxg.commonlib.model.interfaces;

public interface Obsoletable {
  void setObsolete(boolean obsolete);

  boolean isObsolete();

  default void markAsObsolete() {
    setObsolete(true);
  }
}
