package storytime.child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import storytime.story.Story;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Story, Long> {
    List<Story> findByName(String name);
}
