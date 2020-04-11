package storytime.child;

import storytime.parent.Parent;
import storytime.story.Story;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "CHILD")
public class Child {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    @OneToMany
    private Set<Parent> parents;

    @OneToMany
    private Set<Story> favoriteStories;

    @OneToMany
    private Set<Story> dislikedStories;

    public Child() {

    }

    public Child(long id, String name, Set<Parent> parents, Set<Story> favoriteStories, Set<Story> dislikedStories) {
        this.id = id;
        this.name = name;
        this.parents = parents;
        this.favoriteStories = favoriteStories;
        this.dislikedStories = dislikedStories;
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

    public Set<Parent> getParents() {
        return parents;
    }

    public void setParents(Set<Parent> parents) {
        this.parents = parents;
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
        return this.getClass().getSimpleName() + " -- " + this.id;
    }
}
