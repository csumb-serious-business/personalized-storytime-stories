package storytime.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storytime.child.ChildService;
import storytime.child.StoryPreferences;
import storytime.child.StoryPreferencesService;
import storytime.common.templatefill.TemplatedString;
import storytime.parent.ParentService;

import java.util.Map;
import java.util.Optional;

@Service
public class FullStoryService {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
  @Autowired
  StoryPreferencesService storyPreferencesService;
  @Autowired
  private StoryService storyService;
  @Autowired
  private ChildService childService;
  @Autowired
  private ParentService parentService;

  // given a childPrefs and a Story provide a Full Story
  public Optional<String> getFullStory(long storyId, long storyPreferencesId) {
    Optional<StoryPreferences> prefs = storyPreferencesService.read(storyPreferencesId);
    Optional<Story> story = storyService.read(storyId);

    if (!story.isPresent() || !prefs.isPresent()) {
      return Optional.empty();
    }
    return getFullStory(story.get(), prefs.get());
  }

  private Optional<String> getFullStory(Story s, StoryPreferences sp) {
    Map<String, String> prefsMap = prefsAsMap(sp);
    TemplatedString ts = new TemplatedString(s.getContent());
    return Optional.of(ts.fill(prefsMap));
  }

  public Optional<String> getDefault(long storyId) {
    Optional<Story> story = storyService.read(storyId);
    if (!story.isPresent()) {
      return Optional.empty();
    }

    StoryPreferences sp = new StoryPreferences(-1L, null, "beautiful setting",
        "The Brave Protagonist", "Mom", "Dad", "Brother", "Sister", "Fido", "dog");
    return getFullStory(story.get(), sp);
  }

  private Map<String, String> prefsAsMap(StoryPreferences p) {
    //@formatter:off
    return Map.of(
      "setting"     , p.getSetting(),
      "protagonist" , p.getProtagonistCharacterName(),
      "mom"         , p.getMomCharacterName(),
      "dad"         , p.getDadCharacterName(),
      "brother"     , p.getBrotherCharacterName(),
      "sister"      , p.getSisterCharacterName(),
      "pet"         , p.getPetCharacterName(),
      "pet-species" , p.getPetCharacterSpecies()
    );
    //formatter:on
  }

}
