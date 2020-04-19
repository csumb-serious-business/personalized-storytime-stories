package storytime.child;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import storytime.parent.Parent;
import storytime.story.Story;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// note: these tests are just for coverage,
// since this is a Model entity with no logic
@SpringBootTest
class ChildTest {

    // test subject
    private Child subject;

    @BeforeEach
    void setUp() {
        subject = new Child();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_entity() {
        long id = 1L;
        subject.setId(id);
        assertThat(subject.getId()).isEqualTo(id);

        String name = "Child";
        subject.setName(name);
        assertThat(subject.getName()).isEqualTo(name);

        Parent parent = new Parent();
        subject.setParent(parent);
        assertThat(subject.getParent()).isEqualTo(parent);

        StoryPreferences storyPreferences = new StoryPreferences();
        subject.setStoryPreferences(storyPreferences);
        assertThat(subject.getStoryPreferences()).isEqualTo(storyPreferences);

        Set<Story> stories = Set.of(new Story(), new Story());
        subject.setFavoriteStories(stories);
        assertThat(subject.getFavoriteStories()).isEqualTo(stories);

        subject.setDislikedStories(stories);
        assertThat(subject.getDislikedStories()).isEqualTo(stories);

        assertThat(subject.toString()).isEqualTo("Child{1}");

        assertThat(subject).isEqualToComparingFieldByField(
                new Child(id, name, parent, storyPreferences, stories, stories)
        );
    }
}