package storytime.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StoryRestController.class) class StoryRestControllerMVCTest {

  // predefined test objects
  private static final Story validStory = new Story(1L, "title", "content");

  // mocked dependencies
  @MockBean private StoryService storyService;

  // fixtures (for api testing)
  @Autowired private MockMvc mvc;
  private JacksonTester<Story> fixture;

  @BeforeEach void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
    given(storyService.read(1L)).willReturn(Optional.of(validStory));
    given(storyService.read(-1L)).willReturn(Optional.empty());
  }

  @AfterEach void tearDown() {
  }

  @Test public void api__ver__id___valid() throws Exception {
    // given
    long id = 1L;

    // when
    MockHttpServletResponse response =
      mvc.perform(get("/api/0.0.1/" + id)).andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(fixture.write(validStory).getJson());

  }

  @Test public void api__ver__id___invalid() throws Exception {
    // given
    long id = -1L;

    // when
    MockHttpServletResponse response =
      mvc.perform(get("/api/0.0.1/" + id)).andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEqualTo("");

  }

}
