package sedo.db.entity;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import sedo.db.entity.lists.ICorrespondent;
import sedo.db.entity.lists.Nomenclature;
import sedo.db.entity.lists.Rubric;
import sedo.db.entity.lists.User;

/**
 *
 * @author tsowa
 */
public class Document {

  public Document(Type type, Nomenclature reg_number, Date reg_date, String out_number, Date out_date, String content, Date control, File... file) {
    this.file = new ArrayList<>();
    this.type = type;
    this.reg_number = reg_number;
    this.reg_date = reg_date;
    this.out_number = out_number;
    this.out_date = out_date;
    this.content = content;
    this.control = new AbstractMap.SimpleEntry<>(control, null);
    //this.file.addAll(file);/* TODO */
  }
  
  private Long id;
  private Type type;/* Вх. или Исх. */
  private Nomenclature reg_number; /* Номер регистрации */
  private Date reg_date; /* Дата регистрации */
  private String out_number; /* Для входящих - номер документа */
  private Date out_date; /* Для входящих - дата документа */
  //private Access access;/* Тип доступа (ДСП, общий) */
  private String content;/* Содержимое */
  //private Integer pages;/* Количество страниц */
  //private String note; /* Примечание */
  //private List<Rubric> rubric; /* Рубрики */
  private Entry<Date, Date> control; /* На контроле */
  private Delivery delivered_by; /* Вид доставки (почта, e-mail и т.п.) */
  private Status status; /* Статус (у руководства, на исполнении и т.д.)*/
  
  private ICorrespondent[] to; /* Кому, адресат */
  private List<User> vise; /* Визы */
  private List<User> signed; /* Подписи */
  private List<User> executor; /* Исполнители */
  
  private List<Mission> mission; /* Поручения */
  private List<File> file; /* Список файлов */
  private ICorrespondent correspondent; /* Корреспондент */
  
  public enum Access {
    ALL, /* Общий */
    FBU /* Для служебного пользования (ДСП) */
  }
  
  public enum Delivery {
    MAIL,
    EMAIL
    /* TODO: add new */
  }
  
  public enum Status {
    UN_REGISTERED,/* Не зарегистрирован */
    REGISTERED, /* Зарегистрирован */
    IN_ACTION, /* На исполнении */
    AT_MANAGER, /* У руководства */
    IN_REVIEW, /* На рассмотрении ??? */
    WORK_OFF, /* В дело */
    
    REJECTED /* Отклоненная регистрация */
  }
  
  public enum Type {
    IN,
    OUT
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the status
   */
  public Status getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * @return the control
   */
  public Entry<Date, Date> getControl() {
    return control;
  }

  /**
   * @param control the control to set
   */
  public void setControl(Date control) {
    this.control.setValue(control);
  }

  /**
   * @return the reg_number
   */
  public Nomenclature getReg_number() {
    return reg_number;
  }

  /**
   * @param reg_number the reg_number to set
   */
  public void setReg_number(Nomenclature reg_number) {
    this.reg_number = reg_number;
  }

  /**
   * @return the reg_date
   */
  public Date getReg_date() {
    return reg_date;
  }

  /**
   * @param reg_date the reg_date to set
   */
  public void setReg_date(Date reg_date) {
    this.reg_date = reg_date;
  }

  /**
   * @return the out_number
   */
  public String getOut_number() {
    return out_number;
  }

  /**
   * @return the out_date
   */
  public Date getOut_date() {
    return out_date;
  }

  /**
   * @return the file
   */
  public File getFile(Integer index) {
    return file.get(index);
  }
  
  public List<File> getFile() {
    return file;
  }

  /**
   * @param file the file to set
   */
  public void setFile(File file) {
    this.file.add(file);
  }
}
