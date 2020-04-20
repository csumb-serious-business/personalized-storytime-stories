package storytime.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import storytime.common.CrudService;

import java.util.Optional;

@Service
public class StoryService extends CrudService<Story, StoryRepository> {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


  public StoryService(StoryRepository storyRepository) {
    super(storyRepository);
  }

  public void populateStories() {
    // todo

  }

}
