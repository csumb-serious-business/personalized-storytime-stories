package storytime.story;

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
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  private final StoryService service;

  public StoryRestController(StoryService storyService) {
    this.service = storyService;
  }

  @GetMapping("/api/0.0.1/{id}")
  public ResponseEntity<Story> getStoryById(@PathVariable("id") long id) {
    Optional<Story> fromService = service.read(id);

    if (!fromService.isPresent()) {
      log.info("story {} not found", id);
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    } else {
      log.info("story {} found", id);
      return new ResponseEntity<>(fromService.get(), HttpStatus.OK);
    }
  }

}
