package sedo.db.entity.lists;

import java.util.Enumeration;

/**
 *
 * @author tsowa
 */
public class Template implements Enumeration {
    private Integer value;
    
    public Template() {
      this.value = 1;
    }

    @Override
    public boolean hasMoreElements() {
      return Integer.MAX_VALUE > value;        
    }

    @Override
    public Integer nextElement() {
      Integer ret = value;
      value++;
      return ret;
    }
    
    public void removeElement() {
      value--;
    }
    
    public Integer getValue() {
      return value;
    }
  }
