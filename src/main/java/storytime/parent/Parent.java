package storytime.parent;

import storytime.child.Child;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "PARENT")
public class Parent {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @OneToMany
    private Set<Child> children;

    public Parent(long id, String name, Set<Child> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public Parent() {

    }

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public long getId() {
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
}
