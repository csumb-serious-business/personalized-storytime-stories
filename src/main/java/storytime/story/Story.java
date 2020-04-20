package storytime.story;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "STORY")
public class Story {
  @Id
  @GeneratedValue
  private long id;

  @Lob
  @Column(name = "TITLE")
  @NotNull
  private String title;

  @Lob
  @Column(name = "CONTENT")
  private String content;

  public Story(long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
  }

  public Story() {

  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String name) {
    this.title = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + this.id + "}";
  }
}
