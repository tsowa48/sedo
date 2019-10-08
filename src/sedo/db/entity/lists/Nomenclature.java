package sedo.db.entity.lists;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 *
 * @author tsowa
 * @name Номенклатура
 */
public class Nomenclature {
  ArrayList<Entry<Class, Object>> params;
  private String name;
  
  public Nomenclature(ArrayList<Entry<Class, Object>> params, String name) {
    this.params = params;
    this.name = name;
  }
  
  public String getNext() {
    StringBuilder sb = new StringBuilder();
    params.forEach(x -> {
      if(x.getKey().equals(Template.class)) {
        Template t = Template.class.cast(x.getValue());
        if(t.hasMoreElements())
          sb.append(t.nextElement().intValue());
        else
          throw new NoSuchElementException();
      } else
        sb.append(x.getKey().cast(x.getValue()));
    });
    return sb.toString();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    params.forEach(x -> {
      if(x.getKey().equals(Template.class)) {
        Template t = Template.class.cast(x.getValue());
          sb.append(t.getValue());
      } else
        sb.append(x.getKey().cast(x.getValue()));
    });
    return sb.toString();
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
}
