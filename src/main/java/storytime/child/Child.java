package storytime.child;

import io.micrometer.core.lang.NonNull;
import storytime.common.HasId;
import storytime.parent.Parent;
import storytime.story.Story;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "CHILD")
public class Child implements HasId {

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  @Size(min = 3, max = 255)
  private String name;

  @NonNull
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "parent_id")
  private Parent parent;

  @OneToOne(cascade = {CascadeType.ALL})
  private StoryPreferences storyPreferences;

  @OneToMany
  private Set<Story> favoriteStories;

  @OneToMany
  private Set<Story> dislikedStories;

  public Child() {

  }

  public Child(long id, String name, Parent parent, StoryPreferences storyPreferences,
      Set<Story> favoriteStories, Set<Story> dislikedStories) {
    this.id = id;
    this.name = name;
    this.parent = parent;
    this.storyPreferences = storyPreferences;
    this.favoriteStories = favoriteStories;
    this.dislikedStories = dislikedStories;
  }

  public StoryPreferences getStoryPreferences() {
    return storyPreferences;
  }

  public void setStoryPreferences(StoryPreferences storyPreferences) {
    this.storyPreferences = storyPreferences;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Parent getParent() {
    return parent;
  }

  public void setParent(Parent parent) {
    this.parent = parent;
  }

  public Set<Story> getFavoriteStories() {
    return favoriteStories;
  }

  public void setFavoriteStories(Set<Story> favoriteStories) {
    this.favoriteStories = favoriteStories;
  }

  public Set<Story> getDislikedStories() {
    return dislikedStories;
  }

  public void setDislikedStories(Set<Story> dislikedStories) {
    this.dislikedStories = dislikedStories;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + this.id + "}";
  }
}
