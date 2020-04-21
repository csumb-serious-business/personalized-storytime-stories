package storytime.story;

import storytime.common.HasId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// todo rename to StoryTemplate
@Entity
@Table(name = "STORY")
public class Story implements HasId {
  @Id
  @GeneratedValue
  private long id;

  @Lob
  @Column(name = "TITLE")
  @NotNull
  @Size(min = 3)
  private String title;

  @Lob
  @Column(name = "CONTENT")
  @Size(min = 3)
  private String content;

  public Story(long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
  }

  public Story() {

  }

  public Long getId() {
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
