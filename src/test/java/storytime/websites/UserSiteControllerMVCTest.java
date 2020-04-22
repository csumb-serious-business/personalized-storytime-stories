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
import storytime.child.ChildService;
import storytime.child.StoryPreferencesService;
import storytime.parent.Parent;
import storytime.parent.ParentService;
import storytime.story.FullStoryService;
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
  private static final Parent validParent = new Parent(1L, "aaa", "aaa", new HashSet<>());
  private static final Parent emptyParent = new Parent();


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
      .andExpect(model().attribute("parent", hasProperty("id", is(0L))));
  }

  @Test void post___sign_up___valid() throws Exception {

    given(parentService.create(any(Parent.class))).willReturn(Optional.of(validParent));
    given(parentService.read(any(Long.class))).willReturn(Optional.of(validParent));

    mvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", validParent.getUsername())
      .param("passphrase", validParent.getPassphrase())).andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/parent/" + validParent.getId()));

  }

  @Test void post___sign_up___invalid() throws Exception {
    mvc.perform(
      post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", "aa")
        .param("passphrase", "aa")).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void post___sign_up___username_taken() throws Exception {
    // CrudService returns false on create for duplicate unique field
    given(parentService.create(any(Parent.class))).willReturn(Optional.empty());
    mvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", validParent.getUsername())
      .param("passphrase", validParent.getPassphrase())).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void get___sign_in() throws Exception {
    mvc.perform(get("/sign-in")).andExpect(status().isOk())
      // 0L from the bean
      .andExpect(model().attribute("parent", hasProperty("id", is(0L))));
  }

  @Test void post___sign_in___valid() throws Exception {
    given(parentService.getIdForUsername(any(String.class)))
      .willReturn(Optional.of(validParent.getId()));

    mvc.perform(post("/sign-in").contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("username", "Parent").param("passphrase", "Passphrase"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/parent/" + validParent.getId()));
  }

  @Test void post___sign_in___invalid() throws Exception {

    mvc.perform(
      post("/sign-in").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", "aa")
        .param("passphrase", "aa")).andExpect(status().isOk())
      .andExpect(model().attributeHasFieldErrors("parent"));
  }

  @Test void get___parent__id___valid() throws Exception{
    given(parentService.read(validParent.getId())).willReturn(Optional.of(validParent));
    mvc.perform(get("/parent/" + validParent.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(1L))));
  }

  @Test void get___parent__id___invalid()  throws Exception{
    given(parentService.read(-1L)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}", -1L))
      .andExpect(status().is4xxClientError());
  }

  @Test void get___parent__id__edit___valid() throws Exception {
    given(parentService.read(validParent.getId())).willReturn(Optional.of(validParent));
    mvc.perform(get("/parent/{id}/edit", validParent.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(1L))));
  }

  @Test void get___parent__id__edit___invalid() throws Exception {
    given(parentService.read(-1L)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}/edit", -1L))
      .andExpect(status().is4xxClientError());

  }

  @Test void get___parent__id__new_child___valid()  throws Exception{
    given(parentService.read(validParent.getId())).willReturn(Optional.of(validParent));
    mvc.perform(get("/parent/{id}/new-child", validParent.getId()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("parent", hasProperty("id", is(1L))))
      .andExpect(model().attribute("child", hasProperty("id", is(0L))));

  }

  @Test void get___parent__id__new_child___invalid() throws Exception {
    given(parentService.read(-1L)).willReturn(Optional.empty());
    mvc.perform(get("/parent/{id}/new-child", -1L))
      .andExpect(status().is4xxClientError());
  }

  @Test void post___parent__id__new_child___valid()  throws Exception{

  }

  @Test void post___parent__id__child___invalid()  throws Exception{

  }

  @Test void post___parent__id__new_child___errors()  throws Exception{

  }

  @Test void get___parent__pid__child__cid___valid()  throws Exception{

  }

  @Test void get___parent__pid__child__cid___invalid() throws Exception {

  }

  @Test void post___parent__pid__child__cid___valid()  throws Exception{

  }

  @Test void post___parent__pid__child__cid___invalid() throws Exception {

  }

  @Test void post___parent__pid__child__cid___errors() throws Exception {

  }


  @Test void get___parent__pid__stories___valid()  throws Exception{

  }

  @Test void get___parent__pid__stories___invalid()  throws Exception{

  }

  @Test void get___parent__pid__story__sid___valid()  throws Exception{

  }

  @Test void get___parent__pid__story__sid___invalid() throws Exception {

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_all()  throws Exception{

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_parent_child__invalid_story()  throws Exception{

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_parent_story__invalid_child()  throws Exception{

  }

  @Test void get___parent__pid__child__cid__read__sid___valid_child_story__invalid_parent()  throws Exception{

  }

  @Test void get___parent__pid__child__cid__read__sid___invalid_all()  throws Exception{

  }



}
