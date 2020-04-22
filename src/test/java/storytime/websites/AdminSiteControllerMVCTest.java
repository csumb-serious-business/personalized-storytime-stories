package storytime.websites;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminSiteController.class) class AdminSiteControllerMVCTest {

  // predefined objects
  private static final Story storyA = new Story(1L, "title A", "content A");
  private static final Story storyB = new Story(2L, "title B", "content B");
  private static final Story emptyStory = new Story();

  // mocked dependencies
  @MockBean private StoryService storyService;

  // fixtures (for api testing)
  @Autowired private MockMvc mvc;
  private JacksonTester<Story> storyFixture;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);
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
    ).andExpect(status().is3xxRedirection())
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


  @Test void get___admin__story__id___valid() throws Exception{
    given(storyService.read(storyA.getId())).willReturn(Optional.of(storyA));
    mvc.perform(get("/admin/story/" + storyA.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("story", hasProperty("id", is(storyA.getId()))));
  }

  @Test void get___admin__story__id___invalid() throws Exception {
    given(storyService.read(-1L)).willReturn(Optional.empty());
    mvc.perform(get("/admin/story/" + -1))
      .andExpect(status().is4xxClientError());
  }

  @Test void get___admin__story__id__edit__valid() throws Exception {
    given(storyService.read(storyA.getId())).willReturn(Optional.of(storyA));
    mvc.perform(get("/admin/story/" + storyA.getId() + "/edit"))
      .andExpect(status().isOk())
      .andExpect(model().attribute("story", hasProperty("id", is(storyA.getId()))));
  }

  @Test void get___admin__story__id__edit__invalid()  throws Exception {
    given(storyService.read(-1L)).willReturn(Optional.empty());
    mvc.perform(get("/admin/story/" + -1 + "/edit"))
      .andExpect(status().is4xxClientError());
  }

  @Test void post___admin__story__id__edit___valid()  throws Exception {
    given(storyService.update(storyA)).willReturn(Optional.of(storyA));
    mvc.perform(post("/admin/story/" + storyA.getId() + "/edit")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("title", storyA.getTitle())
      .param("content", storyA.getContent())
    ).andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/admin/stories"));

  }

  @Test void post___admin__story__id__edit___invalid()  throws Exception {
    given(storyService.update(emptyStory)).willReturn(Optional.empty());
    mvc.perform(get("/admin/story/" + -1 + "/edit"))
      .andExpect(status().is4xxClientError());
  }

  @Test void post___admin__story__id__edit___errors()  throws Exception {
    mvc.perform(post("/admin/story/" + storyA.getId() + "/edit")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("title", "aa")
      .param("content", "aa")
    )
      .andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("story"));

  }

  @Test void get___admin__story__id__delete___valid()  throws Exception {
    given(storyService.delete(storyA)).willReturn(true);
    mvc.perform(get("/admin/story/" + storyA.getId() + "/delete"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/admin/stories"));
  }

  @Test void get___admin__story__id__delete___invalid()  throws Exception {
    given(storyService.delete(storyA)).willReturn(false);
    mvc.perform(get("/admin/story/" + storyA.getId() + "/delete"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/admin/stories"));
  }
}
