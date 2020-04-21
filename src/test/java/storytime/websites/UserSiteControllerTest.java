
package storytime.websites;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import storytime.child.Child;
import storytime.child.ChildService;
import storytime.child.StoryPreferencesService;
import storytime.parent.Parent;
import storytime.parent.ParentRepository;
import storytime.parent.ParentService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserSiteController.class)
@SpringBootTest
class UserSiteControllerTest {
	// predefined test objects
	private static final Parent parentTest = new Parent(0, "Parent", "passphrase", new HashSet<>());
	private static final Parent validParent = new Parent(0, "Parent", "passphrase", new HashSet<>());
	private static final Parent dupeNameParentA = new Parent(1, "Dupe Parent", "passphrase", new HashSet<>());
	private static final Parent dupeNameParentB = new Parent(2, "Dupe Parent", "passphrase", new HashSet<>());

	private static final Parent emptyParent = new Parent();

	// test subject
	private ParentService subject;

	@MockBean
	@Autowired
	private ParentService parentService;

	@MockBean
	@Autowired
	private ChildService childService;

	@MockBean
	@Autowired
	private StoryPreferencesService storyPreferencesService;

	@Autowired
	private MockMvc mvc;

	@Mock
	private ParentRepository parentRepository;

	private JacksonTester<Parent> json;

	@BeforeEach
	void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
		MockitoAnnotations.initMocks(this);

		subject = new ParentService(parentRepository);

		// mock parent-repo
		given(parentRepository.findById(0L)).willReturn(Optional.of(validParent));
		given(parentRepository.findById(-1L)).willReturn(Optional.empty());

		given(parentRepository.findByUsername("Parent")).willReturn(List.of(validParent));

		// note, username is enforced to be unique in entity, this is just a safeguard
		// check
		given(parentRepository.findByUsername("Dupe Parent")).willReturn(List.of(dupeNameParentA, dupeNameParentB));
		given(parentRepository.findByUsername("NOBODY")).willReturn(List.of());

		given(parentRepository.save(validParent)).willReturn(validParent);
	}

	@AfterEach
	void tearDown() {
	}

//	@Test
//	void testSignIn() throws Exception {
//		fail("Not yet implemented");
//	}

	// I followed the MultiplicationControllerTest.java in the Learn Microservices
	// with
	// Spring Boot book and the ParentServiceTest
	//
	@Test
	void testParentId() throws Exception {
//		given(parentService.getIdForUsername(parentTest.getUsername())).willReturn(validParent);
		given(parentRepository.findById(0L)).willReturn(Optional.of(validParent));
		// when
		MockHttpServletResponse response = mvc.perform(get("/parent/0").contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(json.write(validParent).getJson());

	}

//	@Test
//	void testNewChild() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testChildPreferences() {
//		fail("Not yet implemented");
//	}
//
}
