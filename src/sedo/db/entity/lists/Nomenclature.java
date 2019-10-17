package sedo.db.entity.lists;

import java.util.NavigableSet;
import java.util.TreeSet;
import sedo.db.entity.Document;

/**
 *
 * @author tsowa
 * @name Номенклатура
 */
public class Nomenclature {
  private final String format;
  private final NavigableSet<Long> availableNumbers;
  private final String name;
  private final Document.Type type;

  /**
   * Nomenclature constructor
   * @param name is full name of nomenclature
   * @param format is number generation format, required {0} as incremental number
   * @param type {@see sedo.db.entity.Document.Type}
   */
  public Nomenclature(String name, String format, Document.Type type) {
    this.name = name;
    this.format = format;
    this.type = type;
    this.availableNumbers = new TreeSet<>();
  }
  
  /**
   * Get first available number (TEST)
   * @return full number
   */
  public String get() {
    Long number = 1L;
    if(!availableNumbers.isEmpty()) {
      number = availableNumbers.first();
      if(availableNumbers.last().equals(number))
        availableNumbers.add(availableNumbers.last() + 1L);
      availableNumbers.remove(number);
    } else {
      availableNumbers.add(number + 1L);
    }
    return this.format.replace("{0}", number.toString());
  }
  
  @Override
  public String toString() {
    return this.format.replace("{0}", availableNumbers.isEmpty() ? "1" : availableNumbers.first().toString());
  }

  /**
   * @return nomenclature full name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the type
   */
  public Document.Type getType() {
    return type;
  }
}