package storytime.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoryRestController.class) class StoryRestControllerMVCTest {

  // predefined test objects
  private static final Story story = new Story(1L, "title", "content");

  @MockBean private StoryService storyService;

  @Autowired private MockMvc mvc;

  // subject (for api testing)
  private JacksonTester<Story> subject;

  @BeforeEach void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @AfterEach void tearDown() {
  }

}
