package sedo.db;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import sedo.db.entity.Document;
import sedo.db.entity.Document.Type;
import sedo.db.entity.lists.Nomenclature;
import sedo.db.entity.lists.Template;

/**
 *
 * @author tsowa
 */
public class testDB {
    private final static Collection<Document> DOCS = new ArrayList<>();
    
    static {
      ArrayList<Entry<Class, Object>> params = new ArrayList<>();
      params.add(new AbstractMap.SimpleEntry<>(Integer.class, 4));
      params.add(new AbstractMap.SimpleEntry<>(String.class, "-"));
      params.add(new AbstractMap.SimpleEntry<>(Integer.class, 10));
      params.add(new AbstractMap.SimpleEntry<>(String.class, "/"));
      params.add(new AbstractMap.SimpleEntry<>(Template.class, new Template()));
      Nomenclature n1 = new Nomenclature(params, "Переписка с бухгалтерией");
      
      
      DOCS.add(new Document(Type.IN, null, null, "17-102/14р", new GregorianCalendar(2019, 5, 14).getTime(), "Тестовый документ", null));
      DOCS.add(new Document(Type.IN, null, null, "17/14р", new GregorianCalendar(2019, 10, 4).getTime(), "Приходите в гости к нам", new GregorianCalendar(2019, 10, 29).getTime()));
      
      DOCS.add(new Document(Type.IN, null, null, null, new GregorianCalendar(2019, 10, 4).getTime(), "Сообщение электронной почты", null));
      
      DOCS.add(new Document(Type.OUT, n1, new GregorianCalendar(2019, 10, 8).getTime(), null, null, "О возможности перечисления биткоинов", null));
    }
    
    public static Collection<Document> getNotRegistered() {
        return DOCS.stream().filter(D -> null == D.getReg_number()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getIncoming() {
        return DOCS.stream().filter(D -> Type.IN.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getOutcoming() {
        return DOCS.stream().filter(D -> Type.OUT.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getControl() {
        return DOCS.stream().filter(D -> null != D.getControl().getKey() && null == D.getControl().getValue() && null != D.getReg_number()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getRegistered() {
        return DOCS.stream().filter(D -> null != D.getReg_number()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getInAction() {
        return DOCS;/* Все документы (DEBUG) */
    }
}
