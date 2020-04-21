package storytime.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

// note, don't use this style of test, instead create a MVC test
// like: StoryRestControllerMVCTest
@SpringBootTest
class StoryRestControllerTest {

    // predefined test objects
    private static final Story story =
            new Story(1L, "title", "content");


    // predefined responses
    private static final ResponseEntity<Story> responseOK =
            new ResponseEntity<>(story, HttpStatus.OK);
    private static final ResponseEntity<Story> responseNF =
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    // test subject
    private StoryRestController subject;

    @Mock
    private StoryService storyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        subject = new StoryRestController(storyService);

        // mock story-service
        given(storyService.read(1L)).willReturn(Optional.of(story));
        given(storyService.read(-1L)).willReturn(Optional.empty());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStoryById__valid() {
        ResponseEntity<Story> actual = subject.api__ver__id(story.getId());
        assertThat(actual).isEqualTo(responseOK);
    }

    @Test
    void getStoryById__invalid() {
        ResponseEntity<Story> actual = subject.api__ver__id(-1L);
        assertThat(actual).isEqualTo(responseNF);

    }
}
