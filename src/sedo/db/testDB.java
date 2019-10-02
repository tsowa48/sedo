/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedo.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import sedo.Document.Type;
import sedo.Document;
import sedo.File;

/**
 *
 * @author tsowa
 */
public class testDB {
    private final static Collection<Document> DOCS = new ArrayList<>();
    
    static {
      DOCS.add(new Document(Type.IN, false, "01-15/24", new GregorianCalendar(2019, 5, 14).getTime(), "Тестовый документ", "Иванов И.А.", "07-15-456/2", new GregorianCalendar(2019, 5, 12).getTime(), new File("doc.doc", 65535)));
      DOCS.add(new Document(Type.IN, false, "01-02/4", new GregorianCalendar(2019, 5, 14).getTime(), "Документ №2", "ООО \"Витязь\" - Галкин А.П.", "б/н", new GregorianCalendar(2019, 5, 10).getTime()));
      DOCS.add(new Document(Type.OUT, true, "01-07-2", new GregorianCalendar(2016, 8, 2).getTime(), "Сообщаю Вам сведения о наличии задолженнности", "Брыков С.А.", null, null, new File("Сведения.pdf",2564654)));
      DOCS.add(new Document(Type.OUT, false, "0107/2", new GregorianCalendar(2019, 8, 14).getTime(), "Предоставьте данные", "Rappa X", null, null));
      
      DOCS.add(new Document(Type.IN, false, null, null, "Документ №2", "ООО \"Витязь\" - Галкин А.П.", "б/н", new GregorianCalendar(2019, 5, 10).getTime()));
      DOCS.add(new Document(Type.IN, true, "01-02/4", new GregorianCalendar(2019, 5, 14).getTime(), "Документ №2", "ООО \"Витязь\" - Галкин А.П.", "б/н", new GregorianCalendar(2019, 5, 10).getTime()));
    }
    
    public static Collection<Document> getNotRegistered() {
        return DOCS.stream().filter(D -> null == D.getRegNumber()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getIncoming() {
        return DOCS.stream().filter(D -> Type.IN.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getOutcoming() {
        return DOCS.stream().filter(D -> Type.OUT.equals(D.getType())).collect(Collectors.toList());
    }
    
    public static Collection<Document> getControl() {
        return DOCS.stream().filter(D -> D.isControl()).collect(Collectors.toList());
    }
    
    public static Collection<Document> getRegistered() {
        return DOCS.stream().filter(D -> null != D.getRegNumber()).collect(Collectors.toList());
    }
}
