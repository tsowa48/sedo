package sedo;

import java.util.Date;

/**
 *
 * @author tsowa
 */
public class Document {
    private Type type;
    private Boolean isControl;
    private String regNumber;
    private Date regDate;
    private String content;
    private String addr;
    private String outNumber;
    private Date outDate;
    private File[] files;
    
    private State state;
    
    public enum Type {
      IN,
      OUT
    }
    
    public enum State {
      NOT_REGISTERED,
      REGISTERED,
      IN_ACTION,
      AT_MANAGER,
      IN_REVIEW,
      WORK_OFF
    }
    
    public Document(Type type, Boolean isControl, String regNumber, Date regDate, String content, String addr, String outNumber, Date outDate, File... files) {
        this.type = type;
        this.isControl = isControl;
        this.regNumber = regNumber;
        this.regDate = regDate;
        this.content = content;
        this.addr = addr;
        this.outNumber = outNumber;
        this.outDate = outDate;
        this.files = files;
        this.state = null != regNumber && null != regDate ? State.REGISTERED : State.NOT_REGISTERED;
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
     * @return the isControl
     */
    public Boolean isControl() {
        return isControl;
    }

    /**
     * @param isControl the isControl to set
     */
    public void setControl(Boolean isControl) {
        this.isControl = isControl;
    }

    /**
     * @return the regNumber
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * @param regNumber the regNumber to set
     */
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    /**
     * @return the regDate
     */
    public Date getRegDate() {
        return regDate;
    }

    /**
     * @param regDate the regDate to set
     */
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
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
     * @return the addr
     */
    public String getAddr() {
        return addr;
    }

    /**
     * @param addr the addr to set
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * @return the outNumber
     */
    public String getOutNumber() {
        return outNumber;
    }

    /**
     * @param outNumber the outNumber to set
     */
    public void setOutNumber(String outNumber) {
        this.outNumber = outNumber;
    }

    /**
     * @return the outDate
     */
    public Date getOutDate() {
        return outDate;
    }

    /**
     * @param outDate the outDate to set
     */
    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    /**
     * @return the files
     */
    public File[] getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(File... files) {
        this.files = files;
    }

  /**
   * @return the state
   */
  public State getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(State state) {
    this.state = state;
  }
}
