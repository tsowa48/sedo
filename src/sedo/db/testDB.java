package sedo.db;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final static ObservableList<Nomenclature> NOMS = FXCollections.observableArrayList();
    
    static {
      ArrayList<Entry<Class, Object>> params = new ArrayList<>();
      params.add(new AbstractMap.SimpleEntry<>(String.class, "04-10/"));
      params.add(new AbstractMap.SimpleEntry<>(Template.class, new Template()));
      Nomenclature n1 = new Nomenclature(params, "Переписка с бухгалтерией");

      params = new ArrayList<>();
      params.add(new AbstractMap.SimpleEntry<>(String.class, "01-01/"));
      params.add(new AbstractMap.SimpleEntry<>(Template.class, new Template()));
      Nomenclature n2 = new Nomenclature(params, "Переписка с другими");
      
      NOMS.add(n1);
      NOMS.add(n2);
      
      DOCS.add(new Document("Проверочный документ", new File("file00_1.pdf")));
    }
    
    public static Collection<Document> getNotRegistered() {
        return DOCS.stream().filter(D -> Document.Status.UN_REGISTERED.equals(D.getStatus())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getIncoming() {
        return DOCS.stream().filter(D -> Type.IN.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getOutcoming() {
        return DOCS.stream().filter(D -> Type.OUT.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getControl() {
        return DOCS.stream().filter(D -> null != D.getControl().getKey() && null == D.getControl().getValue() && !Document.Status.UN_REGISTERED.equals(D.getStatus())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getRegistered() {
        return DOCS.stream().filter(D -> null != D.getRegNumber()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getInAction() {
        return DOCS;/* Все документы (DEBUG) */
    }
    
    public static ObservableList getNomenclatures() {
      return NOMS;
    }
}
