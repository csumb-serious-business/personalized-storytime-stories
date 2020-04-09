package storytime.stories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StoryRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private StoryService service;

    public StoryRestController(StoryService storyService) {
        this.service = storyService;
    }

    @GetMapping("/api/0.0.1/{story-name}")
    public ResponseEntity<Story> getStoryByName(
            @PathVariable("story-name") String storyName) {
        Optional<Story> fromService = service.getStoryByName(storyName);

        if (!fromService.isPresent()) {
            log.info("story {} not found", storyName);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            log.info("story {} found", storyName);
            return new ResponseEntity<>(fromService.get(), HttpStatus.OK);
        }
    }

}
