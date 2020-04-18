package storytime.child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryPreferencesRepository extends JpaRepository<StoryPreferences, Long> {
    List<StoryPreferences> findById(long id);


    List<StoryPreferences> findByOwnerId(long ownerId);

}
