package sedo.db;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sedo.db.entity.Document;
import sedo.db.entity.Document.Type;
import sedo.db.entity.lists.Nomenclature;

/**
 *
 * @author tsowa
 */
public class testDB {
    private final static Collection<Document> DOCS = new ArrayList<>();
    private final static ObservableList<Nomenclature> NOMS = FXCollections.observableArrayList();
    
    static {
      NOMS.add(new Nomenclature("Распоряжения", "01-01/{0}", Document.Type.OUT));
      NOMS.add(new Nomenclature("Переписка с организациями", "01-02/{0}", Document.Type.OUT));
      NOMS.add(new Nomenclature("Бухгалтерская отчетность", "01-03/{0}", Document.Type.OUT));
      NOMS.add(new Nomenclature("Жалобы и заявления", "01-04/{0}", Document.Type.IN));
      NOMS.add(new Nomenclature("Переписка с организациями", "01-02/{0}", Document.Type.IN));
      NOMS.add(new Nomenclature("Запросы из ведомств", "01-05/{0}", Document.Type.IN));
      
      Document d1 = new Document("Основной текст первого входящего документа");
      d1.setType(Type.IN);
      d1.setOutDate(Instant.now());
      d1.setOutNumber("ИА-3317");
      DOCS.add(d1);
      DOCS.add(new Document("Проверочный документ", new File("file00_1.pdf")));
      DOCS.add(new Document("X2 документ"));
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
    
    public static ObservableList getNomenclatures(Document.Type type) {
      return FXCollections.observableList(NOMS.stream().filter(N -> N.getType() == type).collect(Collectors.toList()));
    }
}
