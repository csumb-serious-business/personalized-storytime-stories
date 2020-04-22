package storytime.websites;

import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import storytime.child.ChildService;
import storytime.child.StoryPreferencesService;
import storytime.parent.Parent;
import storytime.parent.ParentService;
import storytime.story.FullStoryService;
import storytime.story.Story;
import storytime.story.StoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSiteController.class)
class UserSiteControllerMVCTest {

	// predefined objects
//	private static final Story storyA = new Story(1L, "title A", "content A");
//	private static final Story storyB = new Story(2L, "title B", "content B");
	private static final Parent parentA = new Parent(1L, "Parent", "Passphrase",  new HashSet<>());
	private static final Parent parentB = new Parent(2L, "Parent2", "Passphrase2",  new HashSet<>());
	private static final Parent parentADuplicate = new Parent(1L, "Parent", "Passphrase",  new HashSet<>());


	// mocked dependencies
	@MockBean
	private StoryService storyService;
	@MockBean
	private ParentService parentService;
	@MockBean
	private ChildService childService;
	@MockBean
	private StoryPreferencesService storyPreferencesService;
	@MockBean
	private FullStoryService fullStoryService;

	// fixtures (for api testing)
	@Autowired
	private MockMvc mvc;
	private JacksonTester<Story> storyFixture;

	@BeforeEach
	void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@AfterEach
	void tearDown() {
	}

	@Test void get__user__website__root() throws Exception{
	    mvc.perform(get("/"))
	      .andExpect(status().isOk())
	      .andExpect(view().name("user/_index"));
  }
	
  @Test void get__user__parent__signup() throws Exception {
	    mvc.perform(get("/sign-up"))
	      .andExpect(status().isOk())
	      .andExpect(model().attribute("parent", hasProperty("id", is(0L))));
  }

//todo test valid sign up
@Test void  post__user__parent__signup__valid() throws Exception {
	 mvc.perform(post("/sign-up")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	 	.param("username", parentA.getUsername())
	 	.param("passphrase", parentA.getPassphrase())
		)
		.andExpect(status().is3xxRedirection())
//		.andExpect(redirectedUrl("/parent/"+parentA.getId()));
		.andExpect(redirectedUrl("redirect:"+"/parent/"+parentA.getId()));
	      
  }

@Test void  post__user__parent__signup__invalid() throws Exception {
	 mvc.perform(post("/sign-up")
		 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		 .param("username", "aa")
		 .param("passphrase", "aa")
		 )
		.andExpect(status().isOk())
		.andExpect(model().attributeHasFieldErrors("parent"));
 }
//todo test duplicate
@Test void  post__user__parent__signup__invalid__duplicate() throws Exception {
	 mvc.perform(post("/sign-up")
		 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		 .param("username", parentADuplicate.getUsername())
		 .param("passphrase", parentADuplicate.getPassphrase())
		 )
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("redirect:"+"/parent/"+parentADuplicate.getId()));
}
  @Test void post__user__parent__signin__valid() throws Exception {
	 mvc.perform(post("/sign-in")
		 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		 .param("username", "Parent")
		 .param("passphrase", "Passphrase")
		 )
		
	 	.andExpect(status().is3xxRedirection())
//		.andExpect(redirectedUrl("/parent/"+parentA.getId()));
		.andExpect(redirectedUrl("redirect: /parent/{}"+ parentA.getId()));
  }
  
  @Test void post__user__parent__signin__invalid() throws Exception {
	 mvc.perform(post("/sign-in")
			 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
			 .param("username", "aa")
			 .param("passphrase", "aa")
			 )
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("parent"));
  }
}