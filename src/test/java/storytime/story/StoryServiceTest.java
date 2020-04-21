package storytime.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StoryServiceTest {

  // predefined test objects
  private static final Story storyA = new Story(1L, "storyA", "contentA");
  private static final Story storyB = new Story(2L, "storyB", "contentB");
  private static final Story emptyStory = new Story();

  // test subject
  private StoryService subject;

  @Mock private StoryRepository storyRepository;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);
    subject = new StoryService(storyRepository);

    given(storyRepository.findAll()).willReturn(List.of(storyA, storyB));

  }

  @AfterEach void tearDown() {
  }



  @Test void readAll() {
    List<Story> actual = subject.readAll();
    List<Story> expect = List.of(storyA, storyB);

    assertThat(actual).isEqualTo(expect);
  }

}
