package storytime.parent;

import storytime.child.Child;
import storytime.common.HasId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "PARENT")
public class Parent implements HasId {

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  @Size(min = 3, max = 255)
  @Column(unique = true)
  private String username;

  @NotNull
  @Size(min = 3, max = 255)
  private String passphrase;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "parent_id")
  @OrderBy(value = "name")
  private Set<Child> children;

  public Parent(long id, String username, String passphrase, Set<Child> children) {
    this.id = id;
    this.username = username;
    this.passphrase = passphrase;
    this.children = children;
  }

  public Parent() {

  }

  public String getPassphrase() {
    return passphrase;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  public Set<Child> getChildren() {
    return children;
  }

  public void setChildren(Set<Child> children) {
    this.children = children;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String name) {
    this.username = name;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + this.id + "}";
  }
}
