package storytime.child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryPreferencesRepository extends JpaRepository<StoryPreferences, Long> {
  Optional<StoryPreferences> findByOwnerId(long ownerId);
}
