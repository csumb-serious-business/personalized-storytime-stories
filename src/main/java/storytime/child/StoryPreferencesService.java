package storytime.child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoryPreferencesService {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private StoryPreferencesRepository repo;

    public StoryPreferencesService() {
        super();
    }

    public StoryPreferencesService(StoryPreferencesRepository storyPreferencesRepository) {
        super();
        this.repo = storyPreferencesRepository;
    }

    public Optional<StoryPreferences> getStoryPreferencesById(long id) {
        log.info("getStoryPreferencesById -- {}", id);
        return repo.findById(id).stream().findFirst();
    }

    public Optional<StoryPreferences> getStoryPreferencesByOwner(Child owner) {
        log.info("getStoryPreferencesByOwner -- {}", owner);
        Optional<StoryPreferences> result = repo.findByOwnerId(owner.getId()).stream().findFirst();
        result.ifPresentOrElse(
                r -> log.info("found: {}", r),
                () -> log.info("nothing found")
        );
        return result;
    }

    public boolean persist(StoryPreferences storyPreferences) {
        try {
            repo.save(storyPreferences);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
