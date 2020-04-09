package storytime.stories;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import storytime.stories.Story;
import storytime.stories.StoryRepository;

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

    public Optional<Story> getStoryByName(String name) {
        log.info("getStoryByName -- {}", name);

        // sql => story
        return repo.findByName(name).stream().findFirst();
    }

    public void populateStories() {

    }


}
