package storytime.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import storytime.child.Child;

import java.util.Optional;

@Service
public class StoryService {
  Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  private StoryRepository repo;

  public StoryService() {
    super();
  }

  public StoryService(StoryRepository storyRepository) {
    super();
    this.repo = storyRepository;
  }

  public Optional<Story> getStoryById(long id) {
    log.info("getStoryById -- {}", id);

    // sql => story
    return repo.findById(id).stream().findFirst();
  }

  public void populateStories() {
    // todo

  }

  public boolean persist(Story story) {
    try {
      repo.save(story);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

    public boolean persist(Story story) {
        try {
            repo.save(story);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
