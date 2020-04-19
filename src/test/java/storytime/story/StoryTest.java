package storytime.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// note: these tests are just for coverage,
// since this is a Model entity with no logic
@SpringBootTest
class StoryTest {

    // test subject
    private Story subject;

    @BeforeEach
    void setUp() {
        subject = new Story();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_entity() {
        long id = 1L;
        subject.setId(id);
        assertThat(subject.getId()).isEqualTo(id);

        String title = "title";
        subject.setTitle(title);
        assertThat(subject.getTitle()).isEqualTo(title);

        String content = "content";
        subject.setContent(content);
        assertThat(subject.getContent()).isEqualTo(content);

        assertThat(subject.toString()).isEqualTo("Story{1}");

        assertThat(subject).isEqualToComparingFieldByField(
                new Story(id, title, content)
        );


    }
}