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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StoryRestController.class)
class StoryRestControllerMVCTest {

    // predefined test objects
    private static final Story story =
            new Story(1L, "title", "content");

    @MockBean
    private StoryService storyService;

    @Autowired
    private MockMvc mvc;

    // subject (for api testing)
    private JacksonTester<Story> subject;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void api__getStoryById__valid() throws Exception {
        // input
        Story attempt = new Story();
        long id = 1L;
        attempt.setId(id);

        // expected
        given(storyService.getStoryById(attempt.getId()))
                .willReturn(Optional.of(story));

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/api/0.0.1/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subject.write(attempt).getJson())
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(subject.write(story).getJson());

    }

    @Test
    void api__getStoryById__invalid() throws Exception {
        // input
        Story attempt = new Story();
        long id = -1L;
        attempt.setId(id);

        // expected
        given(storyService.getStoryById(attempt.getId()))
                .willReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/api/0.0.1/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subject.write(attempt).getJson())
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString())
                .isEqualTo("");

    }
}