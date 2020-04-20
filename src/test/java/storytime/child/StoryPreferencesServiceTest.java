package storytime.child;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import storytime.parent.Parent;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StoryPreferencesServiceTest {
    // predefined test objects
    private static final Parent parent = new Parent(0, "Parent",
            "passphrase", Set.of());
    private static final Child child = new Child(1, "Child", parent,
            new StoryPreferences(), null, null);


    private static final StoryPreferences validStoryPreferences =
            new StoryPreferences(0, child, "setting", "protagonist", "mom", "dad", "bro", "sis", "pet", "species");
    private static final StoryPreferences emptyStoryPreferences =
            new StoryPreferences();
    // test subject
    private StoryPreferencesService subject;
    @Mock
    private StoryPreferencesRepository storyPreferencesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        subject = new StoryPreferencesService(storyPreferencesRepository);

        // mock story-prefs-repo
        given(storyPreferencesRepository.findById(1L)).willReturn(Optional.of(validStoryPreferences));
        given(storyPreferencesRepository.findById(-1L)).willReturn(Optional.empty());

        given(storyPreferencesRepository.findByOwnerId(child.getId())).willReturn(Optional.of(validStoryPreferences));
        given(storyPreferencesRepository.findByOwnerId(-1L)).willReturn(Optional.empty());

        given(storyPreferencesRepository.save(validStoryPreferences)).willReturn(validStoryPreferences);
        given(storyPreferencesRepository.save(emptyStoryPreferences)).willThrow(new IllegalArgumentException());
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void getStoryPreferencesByOwner__found() {
        Optional<StoryPreferences> actual = subject.getStoryPreferencesForOwner(child);
        Optional<StoryPreferences> expect = Optional.of(validStoryPreferences);

        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void getStoryPreferencesByOwner__not_found() {
        Child nonexistantChild = new Child();
        nonexistantChild.setId(-1L);
        Optional<StoryPreferences> actual = subject.getStoryPreferencesForOwner(nonexistantChild);
        Optional<StoryPreferences> expect = Optional.empty();

        assertThat(actual).isEqualTo(expect);

    }

    @Test
    void createOrUpdate__create() {
        assertTrue(subject.createOrUpdate(validStoryPreferences));
    }

    @Test
    void createOrUpdate__update() {
        StoryPreferences changed = validStoryPreferences;
        changed.setId(999L);
        assertTrue(subject.createOrUpdate(changed));
    }

    @Test
    void createOrUpdate__fail() {
        assertFalse(subject.createOrUpdate(emptyStoryPreferences));
    }


}