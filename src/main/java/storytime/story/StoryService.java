package storytime.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import storytime.common.CrudService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Service
public class StoryService extends CrudService<Story, StoryRepository> {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


  public StoryService(StoryRepository storyRepository) {
    super(storyRepository);
  }

  public List<Story> readAll() {
    List<Story> stories = repository.findAll();
    log.info("fetched {} stories", stories.size());
    return stories;
  }

  public void populateStories() throws FileNotFoundException {
    // todo

  }

}
