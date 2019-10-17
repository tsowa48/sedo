package sedo.db.entity;

import java.io.File;
import java.util.*;
import java.time.Instant;
import java.util.Map.Entry;
import sedo.db.entity.lists.ICorrespondent;
import sedo.db.entity.lists.Rubric;
import sedo.db.entity.lists.User;

/**
 *
 * @author tsowa
 */
public class Document {

  /**
   * By default document type is Outcoming ({@see Document.Type}),
   * status - unregistered ({@see Document.Status})
   * @param content - document content
   * @param control - control date of document
   * @param access - {@see Document.Accees}
   * @param file - list of files to be attached
   */
  public Document(String content, Instant control, Access access, File... file) {
    this.file = new ArrayList<>();
    this.status = Status.UN_REGISTERED;
    this.type = Type.OUT;
    this.access = access;
    this.content = content;
    this.control = new AbstractMap.SimpleEntry<>(control, null);
    this.file.addAll(Arrays.asList(file));
  }
  
  public Document(String content, Instant control, File... file) {
    this(content, control, Access.ALL, file);
  }
  
  public Document(String content, File... file) {
    this(content, null, Access.ALL, file);
  }
  
  public Document(String content, Access access, File... file) {
    this(content, null, access, file);
  }
  
  public Document() {
    this.file = new ArrayList<>();
    this.control = new AbstractMap.SimpleEntry<>(null, null);
    this.status = Status.UN_REGISTERED;
  }
  
  private Long id;
  private Type type;/* Вх. или Исх. */
  private String reg_number; /* Номер регистрации */
  private Instant reg_date; /* Дата регистрации */
  private String out_number; /* Для входящих - номер документа */
  private Instant out_date; /* Для входящих - дата документа */
  private Access access;/* Тип доступа (ДСП, общий) */
  private String content;/* Содержимое */
  //private Integer pages;/* Количество страниц */
  private String note; /* Примечание */
  //private List<Rubric> rubric; /* Рубрики */
  private Entry<Instant, Instant> control; /* На контроле */
  private Delivery delivered_by; /* Вид доставки (почта, e-mail и т.п.) */
  private Status status; /* Статус (у руководства, на исполнении и т.д.)*/
  
  private ICorrespondent[] to; /* Кому, адресат */
  private List<User> vise; /* Визы */
  private List<User> signed; /* Подписи */
  private List<User> executor; /* Исполнители */
  
  private List<Mission> mission; /* Поручения */
  private List<File> file; /* Список файлов */
  private ICorrespondent correspondent; /* Корреспондент */
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Document [");
    sb.append("id=");
    sb.append(this.id);
    sb.append(", type=");
    sb.append(this.type);
    sb.append(", reg_number=");
    sb.append(this.reg_number);
    sb.append(", reg_date=");
    sb.append(this.reg_date);
    sb.append(", out_number=");
    sb.append(this.out_number);
    sb.append(", out_date=");
    sb.append(this.out_date);
    sb.append(", access=");
    sb.append(this.access);
    sb.append(", content='");
    sb.append(this.content);
    sb.append("', note='");
    sb.append(this.note);
    sb.append("', control=");
    sb.append(this.control);
    sb.append(", delivered_by=");
    sb.append(this.delivered_by);
    sb.append(", status=");
    sb.append(this.status);
    
    sb.append(", ...]");
    return sb.toString();
  }
  public enum Access {
    ALL("Общий"),
    SECRET("Секретно"),
    CONFIDENCIAL("Конфиденциально"),
    FBU("ДСП"), /* Для служебного пользования */
    COMMERCIAL("Коммерческая тайна");
    
    private final String text;
    private Access(String text) {
      this.text = text;
    }
    @Override
    public String toString() {
      return this.text;
    }
  }
  
  public enum Delivery {
    COURIER("Нарочно"),
    MAIL("Почта"),
    EMAIL("Эл.почта"),
    FAX("Факс"),
    TELEGRAMM("Телеграмма"),
    DELO("ПИ Дело");

    private final String text;
    private Delivery(String text) {
      this.text = text;
    }
    @Override
    public String toString() {
      return this.text;
    }
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
    IN("Входящий"),
    OUT("Исходящий");
    
    private final String text;
    private Type(String text) {
      this.text = text;
    }
    @Override
    public String toString() {
      return this.text;
    }
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
  public Entry<Instant, Instant> getControl() {
    return control;
  }

  /**
   * @param control the control to unset
   */
  public void unsetControl(Instant control) {
    this.control.setValue(control);
  }
  
  /**
   * @param control the control to set
   */
  public void setControl(Instant control) {
    this.control = new AbstractMap.SimpleEntry<>(control, null);
  }

  /**
   * @return the reg_number
   */
  public String getRegNumber() {
    return reg_number;
  }

  /**
   * @param number the reg_number to set
   */
  public void setRegNumber(String number) {
    this.reg_number = number;
  }

  /**
   * @return the reg_date
   */
  public Instant getRegDate() {
    return reg_date;
  }

  /**
   * @param date the reg_date to set
   */
  public void setRegDate(Instant date) {
    this.reg_date = date;
  }

  /**
   * @return the out_number
   */
  public String getOutNumber() {
    return out_number;
  }

  /**
   * @return the out_date
   */
  public Instant getOutDate() {
    return out_date;
  }

  /**
   * @param index is index in file array
   * @return file by index
   */
  public File getFile(Integer index) {
    return file.get(index);
  }
  
  /**
   * @param name is name of searched file
   * @return file by name if founded else - null
   */
  public File getFile(String name) {
    return file.parallelStream().filter(F -> F.getName().equals(name)).findFirst().orElse(null);
  }
  
  /**
   * @return all files as list
   */
  public List<File> getFile() {
    return file;
  }

  /**
   * @param file the file to set
   */
  public void setFile(File file) {
    this.file.add(file);
  }

  /**
   * @return the access
   */
  public Access getAccess() {
    return access;
  }

  /**
   * @param access the access to set
   */
  public void setAccess(Access access) {
    this.access = access;
  }

  /**
   * @return the delivered_by
   */
  public Delivery getDelivered_by() {
    return delivered_by;
  }

  /**
   * @param delivered_by the delivered_by to set
   */
  public void setDelivered_by(Delivery delivered_by) {
    this.delivered_by = delivered_by;
  }

  /**
   * @return the correspondent
   */
  public ICorrespondent getCorrespondent() {
    return correspondent;
  }

  /**
   * @param correspondent the correspondent to set
   */
  public void setCorrespondent(ICorrespondent correspondent) {
    this.correspondent = correspondent;
  }

  /**
   * @return the note
   */
  public String getNote() {
    return note;
  }

  /**
   * @param note the note to set
   */
  public void setNote(String note) {
    this.note = note;
  }

  /**
   * @param out_date the out_date to set
   */
  public void setOutDate(Instant out_date) {
    this.out_date = out_date;
  }

  /**
   * @param out_number the out_number to set
   */
  public void setOutNumber(String out_number) {
    this.out_number = out_number;
  }
}
