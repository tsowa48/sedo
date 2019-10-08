package sedo.db.entity;

import java.util.Date;
import sedo.db.entity.lists.User;
/**
 *
 * @author tsowa
 * @name Поручение
 */
public class Mission {
  private Long id;
  private User author;
  private Date date;
  private String text;
  private User[] executor;
  private Long did;
}
