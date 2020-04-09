package storytime.stories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import storytime.stories.Story;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByName(String name);
}
