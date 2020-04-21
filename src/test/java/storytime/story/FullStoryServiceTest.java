package storytime.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import storytime.child.StoryPreferences;
import storytime.child.StoryPreferencesService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest class FullStoryServiceTest {
  // predefined test objects
  private static final String content =
    "setting: ${setting}," + " protagonist: ${protagonist}," + " mom: ${mom}," + " dad: ${dad},"
      + " brother: ${brother}," + " sister: ${sister}," + " pet: ${pet},"
      + " pet-species: ${pet-species}";

  private static final String validDefaultStory =
    "setting: beautiful setting," + " protagonist: The Brave Protagonist," + " mom: Mom,"
      + " dad: Dad," + " brother: Brother," + " sister: Sister," + " pet: Fido,"
      + " pet-species: dog";

  private static final String validFullStory =
    "setting: Setting," + " protagonist: Protagonist," + " mom: Mom," + " dad: Dad,"
      + " brother: Brother," + " sister: Sister," + " pet: Pet," + " pet-species: Pet-Species";

  private static final StoryPreferences validStoryPreferences =
    new StoryPreferences(1L, null, "Setting", "Protagonist", "Mom", "Dad", "Brother", "Sister",
      "Pet", "Pet-Species");

  private static final Story validStory = new Story(1L, "story", content);

  // mocks
  @Mock private StoryService storyService;
  @Mock private StoryPreferencesService storyPreferencesService;

  // test subject
  private FullStoryService subject;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new FullStoryService(storyService, storyPreferencesService);

    given(storyService.read(1L)).willReturn(Optional.of(validStory));
    given(storyService.read(-1L)).willReturn(Optional.empty());

    given(storyPreferencesService.read(1L)).willReturn(Optional.of(validStoryPreferences));
    given(storyPreferencesService.read(-1L)).willReturn(Optional.empty());

  }

  @AfterEach void tearDown() {
  }

  @Test void getFullStory__story_valid__prefs__valid() {
    assertThat(subject.getFullStory(validStory.getId(), validStoryPreferences.getId()))
      .isEqualTo(Optional.of(validFullStory));
  }

  @Test void getFullStory__story_invalid__prefs__valid() {
    assertThat(subject.getFullStory(-1L, validStoryPreferences.getId()))
      .isEqualTo(Optional.empty());
  }

  @Test void getFullStory__story_valid__prefs__invalid() {
    assertThat(subject.getFullStory(validStory.getId(), -1L)).isEqualTo(Optional.empty());
  }

  @Test void getFullStory__story_invalid__prefs__invalid() {
    assertThat(subject.getFullStory(-1L, -1L)).isEqualTo(Optional.empty());
  }

  @Test void getDefault__story_valid() {
    assertThat(subject.getDefault(validStory.getId())).isEqualTo(Optional.of(validDefaultStory));
  }

  @Test void getDefault__story_invalid() {
    assertThat(subject.getDefault(-1L)).isEqualTo(Optional.empty());
  }
}
