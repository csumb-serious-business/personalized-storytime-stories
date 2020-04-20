package storytime.child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storytime.common.CrudService;

import java.util.Optional;

@Service
public class StoryPreferencesService
    extends CrudService<StoryPreferences, StoryPreferencesRepository> {
  Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  public StoryPreferencesService(StoryPreferencesRepository storyPreferencesRepository) {
    super(storyPreferencesRepository);
  }

  public Optional<StoryPreferences> getStoryPreferencesForOwner(Child owner) {
    log.info("getStoryPreferencesByOwner -- {}", owner);
    Optional<StoryPreferences> result =
        repository.findByOwnerId(owner.getId()).stream().findFirst();
    result.ifPresentOrElse(r -> log.info("found: {}", r), () -> log.info("nothing found"));
    return result;
  }

  public boolean createOrUpdate(StoryPreferences storyPreferences) {
    log.info("createOrUpdate -- {}", storyPreferences);

    try {
      // we assume that the passed storyPreferences is correct except for
      // its Id
      // so capture the id and place it on the passed one.
      Long id = getStoryPreferencesForOwner(storyPreferences.getOwner())
          .map(StoryPreferences::getId).orElse(storyPreferences.getId());

      storyPreferences.setId(id);

      repository.save(storyPreferences);
    } catch (Exception e) {
      log.info("error -- {}, {}", e.getMessage(), e.getStackTrace());
      return false;
    }
    return true;
  }
}
