package storytime.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

class StoryServiceTest {

    // predefined test objects
    private static final Story validStory =
            new Story(1L, "Story", "content");
    private static final Story emptyStory = new Story();

    // test subject
    private StoryService subject;

    @Mock
    private StoryRepository storyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        subject = new StoryService(storyRepository);

        // mock story-repo
        given(storyRepository.findById(1L)).willReturn(Optional.of(validStory));
        given(storyRepository.findById(-1L)).willReturn(Optional.empty());

        given(storyRepository.save(validStory)).willReturn(validStory);
        given(storyRepository.save(emptyStory)).willThrow(new IllegalArgumentException());


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void persist__valid_story() {
        assertTrue(subject.persist(validStory));
    }

    @Test
    void persist__invalid_story() {
        assertFalse(subject.persist(emptyStory));
    }

    @Test
    void getChildById__found() {
        Optional<Story> actual = subject.getStoryById(1L);
        Optional<Story> expect = Optional.of(validStory);

        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void getChildById__not_found() {
        Optional<Story> actual = subject.getStoryById(-1L);
        Optional<Story> expect = Optional.empty();

        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void populateStories__valid() {
        // todo
        subject.populateStories();
    }
}