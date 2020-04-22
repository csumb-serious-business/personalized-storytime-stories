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
import storytime.child.Child;
import storytime.child.ChildService;
import storytime.child.StoryPreferences;
import storytime.child.StoryPreferencesService;
import storytime.parent.Parent;
import storytime.parent.ParentService;
import storytime.story.FullStoryService;
import storytime.story.Story;
import storytime.story.StoryService;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSiteController.class) class UserSiteControllerMVCTest {

  // predefined objects
  private static final Parent parent = new Parent(1L, "parent", "passphrase", new HashSet<>());
  private static final Child child =
    new Child(2L, "child", parent, null, null, null);
  private static final StoryPreferences preferences = new StoryPreferences(3L, null, "setting", "protagonist", "mom", "dad",
    "brother", "sister", "pet", "set-species");

    private static final Story story = new Story(4L, "title", "content");

  private static final String fullStory = "FULL STORY";
  private static final long DEFAULT_ID = 0L;
  private static final long NOT_FOUND_ID = -1L;
  private static final String INVALID_STR = "aa";
  private static final String WRONG_PASS = "wrong_passphrase";


  // mocked dependencies
  @MockBean private StoryService storyService;

  @MockBean private ParentService parentService;

  @MockBean private ChildService childService;

  @MockBean private StoryPreferencesService storyPreferencesService;

  @MockBean private FullStoryService fullStoryService;

  // fixtures (for api testing)
  @Autowired private MockMvc mvc;
  private JacksonTester<Parent> parentFixture;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @AfterEach void tearDown() {
  }

  @Test void get___website_root() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("user/_index"));
  }

  @Test void get___sign_up() throws Exception {
    mvc.perform(get("/sign-up")).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(DEFAULT_ID))));
  }

  @Test void post___sign_up___valid() throws Exception {
    given(parentService.create(any(Parent.class))).willReturn(Optional.of(parent));
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));

    mvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", parent.getUsername()).param("passphrase", parent.getPassphrase()))
      .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/parent/" + parent.getId()));

  }

  @Test void post___sign_up___invalid() throws Exception {
    mvc.perform(
      post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", INVALID_STR)
        .param("passphrase", INVALID_STR)).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void post___sign_up___username_taken() throws Exception {
    // CrudService returns false on create for duplicate unique field
    given(parentService.create(any(Parent.class))).willReturn(Optional.empty());
    mvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", parent.getUsername()).param("passphrase", parent.getPassphrase()))
      .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void get___sign_in() throws Exception {
    mvc.perform(get("/sign-in")).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(DEFAULT_ID))));
  }

  @Test void post___sign_in___valid() throws Exception {
    given(parentService.getIdForUsername(any(String.class)))
      .willReturn(Optional.of(parent.getId()));

    mvc.perform(post("/sign-in").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", "Parent").param("passphrase", parent.getPassphrase()))
      .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/parent/" + parent.getId()));
  }

  @Test void post___sign_in___invalid() throws Exception {
    given(parentService.loginAttempt(any(Parent.class)))
      .willReturn(Optional.of("fail"));

    mvc.perform(
      post("/sign-in").contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("username", parent.getUsername())
        .param("passphrase", WRONG_PASS)).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void post___sign_in___errors() throws Exception {
    mvc.perform(
      post("/sign-in").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", INVALID_STR)
        .param("passphrase", INVALID_STR)).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void get___parent__id___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    mvc.perform(get("/parent/{id}", parent.getId())).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))));
  }

  @Test void get___parent__id___invalid() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}", NOT_FOUND_ID)).andExpect(status().is4xxClientError());
  }

  @Test void get___parent__id__edit___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    mvc.perform(get("/parent/{id}/edit", parent.getId())).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))));
  }

  @Test void get___parent__id__edit___invalid() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}/edit", NOT_FOUND_ID)).andExpect(status().is4xxClientError());

  }

  @Test void get___parent__id__new_child___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    mvc.perform(get("/parent/{id}/new-child", parent.getId())).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))))
      .andExpect(model().attribute("child", hasProperty("id", is(DEFAULT_ID))));

  }

  @Test void get___parent__id__new_child___invalid() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}/new-child", NOT_FOUND_ID)).andExpect(status().is4xxClientError());
  }

  @Test void post___parent__id__new_child___valid() throws Exception {
    given(childService.create(any(Child.class))).willReturn(Optional.of(child));
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));

    mvc.perform(post("/parent/{id}/new-child", parent.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", child.getName()))
      .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/parent/" + parent.getId()));

  }

  @Test void post___parent__id__child___invalid() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.empty());

    mvc.perform(post("/parent/{id}/new-child", parent.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", child.getName())
      .param("name", INVALID_STR)).andExpect(status().is4xxClientError());

  }

  @Test void post___parent__id__new_child___errors() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));

    mvc.perform(post("/parent/{id}/new-child", parent.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", INVALID_STR))
      .andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("child"));
  }

  @Test void get___parent__pid__child__cid__prefs___valid() throws Exception {
    given(childService.read(child.getId())).willReturn(Optional.of(child));
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));

    mvc.perform(get("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))))
      .andExpect(model().attribute("child", hasProperty("id", is(child.getId()))));

  }

  @Test void get___parent__pid__child__cid__prefs___invalid_all() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.empty());
    given(childService.read(any(Long.class))).willReturn(Optional.empty());

    mvc.perform(get("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", child.getName())
      .param("name", INVALID_STR)).andExpect(status().is4xxClientError());

  }

  @Test void get___parent__pid__child__cid__prefs___invalid_parent() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.empty());
    given(childService.read(any(Long.class))).willReturn(Optional.of(child));

    mvc.perform(get("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", child.getName())
      .param("name", INVALID_STR)).andExpect(status().is4xxClientError());

  }

  @Test void get___parent__pid__child__cid__prefs___invalid_child() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));
    given(childService.read(any(Long.class))).willReturn(Optional.empty());

    mvc.perform(get("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", child.getName())
      .param("name", INVALID_STR)).andExpect(status().is4xxClientError());

  }

  @Test void post___parent__pid__child__cid__prefs___valid() throws Exception {
    given(childService.read(any(Long.class))).willReturn(Optional.of(child));
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));
    given(storyPreferencesService.create(any(StoryPreferences.class))).willReturn(Optional.of(preferences));

    mvc.perform(post("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("setting", preferences.getSetting())
      .param("protagonistCharacterName", preferences.getProtagonistCharacterName())
      .param("momCharacterName", preferences.getMomCharacterName())
      .param("dadCharacterName", preferences.getDadCharacterName())
      .param("brotherCharacterName", preferences.getBrotherCharacterName())
      .param("sisterCharacterName", preferences.getSisterCharacterName())
      .param("petCharacterName", preferences.getPetCharacterName())
      .param("petCharacterSpecies", preferences.getPetCharacterSpecies())
    )
      .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/parent/" + parent.getId()));

  }

  @Test void post___parent__pid__child__cid__prefs___invalid_all() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.empty());
    given(childService.read(any(Long.class))).willReturn(Optional.empty());

    mvc.perform(post("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("setting", preferences.getSetting())
        .param("protagonistCharacterName", preferences.getProtagonistCharacterName())
        .param("momCharacterName", preferences.getMomCharacterName())
        .param("dadCharacterName", preferences.getDadCharacterName())
        .param("brotherCharacterName", preferences.getBrotherCharacterName())
        .param("sisterCharacterName", preferences.getSisterCharacterName())
        .param("petCharacterName", preferences.getPetCharacterName())
        .param("petCharacterSpecies", preferences.getPetCharacterSpecies())
      ).andExpect(status().is4xxClientError());

  }
@Test void post___parent__pid__child__cid__prefs___invalid_child() throws Exception {
  given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));
  given(childService.read(any(Long.class))).willReturn(Optional.empty());

  mvc.perform(post("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    .param("setting", preferences.getSetting())
    .param("protagonistCharacterName", preferences.getProtagonistCharacterName())
    .param("momCharacterName", preferences.getMomCharacterName())
    .param("dadCharacterName", preferences.getDadCharacterName())
    .param("brotherCharacterName", preferences.getBrotherCharacterName())
    .param("sisterCharacterName", preferences.getSisterCharacterName())
    .param("petCharacterName", preferences.getPetCharacterName())
    .param("petCharacterSpecies", preferences.getPetCharacterSpecies())
  ).andExpect(status().is4xxClientError());
  }

  @Test void post___parent__pid__child__cid__prefs___invalid_parent() throws Exception {
    given(parentService.read(any(Long.class))).willReturn(Optional.empty());
    given(childService.read(any(Long.class))).willReturn(Optional.of(child));

    mvc.perform(post("/parent/{pid}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("setting", preferences.getSetting())
      .param("protagonistCharacterName", preferences.getProtagonistCharacterName())
      .param("momCharacterName", preferences.getMomCharacterName())
      .param("dadCharacterName", preferences.getDadCharacterName())
      .param("brotherCharacterName", preferences.getBrotherCharacterName())
      .param("sisterCharacterName", preferences.getSisterCharacterName())
      .param("petCharacterName", preferences.getPetCharacterName())
      .param("petCharacterSpecies", preferences.getPetCharacterSpecies())
    ).andExpect(status().is4xxClientError());
  }

  @Test void post___parent__pid__child__cid__prefs___errors() throws Exception {
    given(childService.read(any(Long.class))).willReturn(Optional.of(child));
    given(parentService.read(any(Long.class))).willReturn(Optional.of(parent));
    given(storyPreferencesService.create(any(StoryPreferences.class))).willReturn(Optional.of(preferences));

    mvc.perform(post("/parent/{id}/child/{cid}/prefs", parent.getId(), child.getId())
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("setting", INVALID_STR)
      .param("protagonistCharacterName", INVALID_STR)
      .param("momCharacterName", INVALID_STR)
      .param("dadCharacterName", INVALID_STR)
      .param("brotherCharacterName", INVALID_STR)
      .param("sisterCharacterName", INVALID_STR)
      .param("petCharacterName", INVALID_STR)
      .param("petCharacterSpecies", INVALID_STR)
    )
      .andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("storyPreferences"));
  }


  @Test void get___parent__pid__stories___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    mvc.perform(get("/parent/{id}/stories", parent.getId())).andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))));
  }

  @Test void get___parent__pid__stories___invalid() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}/stories", NOT_FOUND_ID)).andExpect(status().is4xxClientError());

  }

  @Test void get___parent__pid__story__sid___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(storyService.read(story.getId())).willReturn(Optional.of(story));
    mvc.perform(get("/parent/{pid}/story/{sid}", parent.getId(), story.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))))
      .andExpect(model().attribute("story", hasProperty("id", is(story.getId()))));

  }

  @Test void get___parent__pid__story__sid___invalid_all() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    given(storyService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{pid}/story/{sid}", NOT_FOUND_ID, NOT_FOUND_ID))
      .andExpect(status().is4xxClientError());
  }
  @Test void get___parent__pid__story__sid___invalid_parent() throws Exception {
    given(parentService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    given(storyService.read(story.getId())).willReturn(Optional.of(story));
    mvc.perform(get("/parent/{pid}/story/{sid}", NOT_FOUND_ID, story.getId()))
      .andExpect(status().is4xxClientError());

  }
  @Test void get___parent__pid__story__sid___invalid_story() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(storyService.read(NOT_FOUND_ID)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{pid}/story/{sid}", parent.getId(), NOT_FOUND_ID))
      .andExpect(status().is4xxClientError());

  }

  @Test void get___parent__pid__child__cid__read__sid___valid() throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(childService.read(child.getId())).willReturn(Optional.of(child));
    given(storyPreferencesService.getStoryPreferencesForOwner(child)).willReturn(Optional.of(preferences));
    given(storyService.read(story.getId())).willReturn(Optional.of(story));
    given(fullStoryService.getFullStory(story.getId(), preferences.getId())).willReturn(Optional.of(fullStory));

    mvc.perform(get("/parent/{pid}/child/{cid}/read/{sid}", parent.getId(), child.getId(), story.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(parent.getId()))))
      .andExpect(model().attribute("story", hasProperty("id", is(story.getId()))))
      .andExpect(model().attribute("fullstory", is(fullStory)));

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_parent_child__invalid_story()
    throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(childService.read(child.getId())).willReturn(Optional.of(child));
    given(storyPreferencesService.getStoryPreferencesForOwner(child)).willReturn(Optional.of(preferences));
    given(storyService.read(story.getId())).willReturn(Optional.empty());
    given(fullStoryService.getFullStory(story.getId(), preferences.getId())).willReturn(Optional.of(fullStory));

    mvc.perform(get("/parent/{pid}/child/{cid}/read/{sid}", parent.getId(), child.getId(), story.getId()))
      .andExpect(status().is4xxClientError());

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_parent_story__invalid_child()
    throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(childService.read(child.getId())).willReturn(Optional.empty());
    given(storyPreferencesService.getStoryPreferencesForOwner(child)).willReturn(Optional.of(preferences));
    given(storyService.read(story.getId())).willReturn(Optional.of(story));
    given(fullStoryService.getFullStory(story.getId(), preferences.getId())).willReturn(Optional.of(fullStory));

    mvc.perform(get("/parent/{pid}/child/{cid}/read/{sid}", parent.getId(), child.getId(), story.getId()))
      .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/parent/" + parent.getId()));

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_child_story__invalid_parent()
    throws Exception {
        given(parentService.read(parent.getId())).willReturn(Optional.empty());
      given(childService.read(child.getId())).willReturn(Optional.of(child));
      given(storyPreferencesService.getStoryPreferencesForOwner(child)).willReturn(Optional.of(preferences));
      given(storyService.read(story.getId())).willReturn(Optional.of(story));
      given(fullStoryService.getFullStory(story.getId(), preferences.getId())).willReturn(Optional.of(fullStory));

      mvc.perform(get("/parent/{pid}/child/{cid}/read/{sid}", parent.getId(), child.getId(), story.getId()))
        .andExpect(status().is4xxClientError());
  }

  @Test void get___parent__pid__child__cid__read__sid___valid_child_story__invalid_prefs()
    throws Exception {
    given(parentService.read(parent.getId())).willReturn(Optional.of(parent));
    given(childService.read(child.getId())).willReturn(Optional.of(child));
    given(storyPreferencesService.getStoryPreferencesForOwner(child)).willReturn(Optional.empty());
    given(storyService.read(story.getId())).willReturn(Optional.of(story));
    given(fullStoryService.getFullStory(story.getId(), preferences.getId())).willReturn(Optional.of(fullStory));

    mvc.perform(get("/parent/{pid}/child/{cid}/read/{sid}", parent.getId(), child.getId(), story.getId()))
      .andExpect(status().is4xxClientError());
  }

}
