package storytime.websites;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import storytime.story.Story;
import storytime.story.StoryService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminSiteController.class) class AdminSiteControllerMVCTest {

  // predefined objects
  private static final Story storyA = new Story(1L, "title A", "content A");
  private static final Story storyB = new Story(2L, "title B", "content B");

  // mocked dependencies
  @MockBean private StoryService storyService;

  // fixtures (for api testing)
  @Autowired private MockMvc mvc;
  private JacksonTester<Story> storyFixture;

  @BeforeEach void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @AfterEach void tearDown() {
  }

  @Test void get___admin__stories___one() throws Exception {
    given(storyService.readAll()).willReturn(List.of(storyA));
    mvc.perform(get("/admin/stories"))
      .andExpect(status().isOk())
      .andExpect(model().attribute("stories", hasSize(1)));

  }

  @Test void get___admin__stories___two() throws Exception {
    given(storyService.readAll()).willReturn(List.of(storyA, storyB));
    mvc.perform(get("/admin/stories"))
      .andExpect(status().isOk())
      .andExpect(model().attribute("stories", hasSize(2)));

  }
  @Test void get___admin__stories___none() throws Exception {
    given(storyService.readAll()).willReturn(List.of());
    mvc.perform(get("/admin/stories"))
      .andExpect(status().isOk())
      .andExpect(model().attribute("stories", hasSize(0)));

  }

  @Test void get___admin__story__new_story___valid() throws Exception {
    mvc.perform(get("/admin/story/new-story"))
      .andExpect(status().isOk())
      .andExpect(model().attribute("story", hasProperty("id", is(0L))));

  }

  @Test void post___admin__story__new_story___valid() throws Exception {
    mvc.perform(post("/admin/story/new-story")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("title", storyA.getTitle())
      .param("content", storyA.getContent())
    )
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/admin/stories"));
  }

  @Test void post___admin__story__new_story___invalid() throws Exception {
    mvc.perform(post("/admin/story/new-story")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("title", "aa")
      .param("content", "aa")
    )
      .andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("story"));
  }


  @Test void get___admin__story__id___valid() {
  }

  @Test void get___admin__story__id___invalid() {
  }

  @Test void get___admin__story__id__edit__valid() {
  }

  @Test void get___admin__story__id__edit__invalid() {
  }

  @Test void post___admin__story__id__edit___valid() {
  }

  @Test void post___admin__story__id__edit___invalid() {
  }

  @Test void get___admin__story__id__delete___valid() {
  }

  @Test void get___admin__story__id__delete___invalid() {
  }
}
