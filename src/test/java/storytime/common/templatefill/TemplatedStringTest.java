package storytime.common.templatefill;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TemplatedStringTest {

  // predefined test objects
  private static final String content_one_match = "a ${match} z";
  private static final String content_two_match = "a ${match1} m ${match2} z";
  private static final String content_has_dupe = "a ${match1} m ${match1} z";
  private static final String content_no_match = "a z";
  private static final Map<String, String> map =
    Map.of("match", "-m-", "match1", "-m1-", "match2", "-m2-");

  // test subject
  TemplatedString subject;

  @BeforeEach void setUp() {

  }

  @AfterEach void tearDown() {
  }

  @Test void captureIdentifiers__one_match() {
    subject = new TemplatedString(content_one_match);
    Set<String> actual = subject.getIdentifiers();
    Set<String> expect = Set.of("match");

    assertThat(actual).hasSameElementsAs(expect);
  }

  @Test void captureIdentifiers__two_match() {
    subject = new TemplatedString(content_two_match);
    Set<String> actual = subject.getIdentifiers();
    Set<String> expect = Set.of("match1", "match2");

    assertThat(actual).hasSameElementsAs(expect);
  }

  @Test void captureIdentifiers__has_dupe() {
    subject = new TemplatedString(content_has_dupe);
    Set<String> actual = subject.getIdentifiers();
    Set<String> expect = Set.of("match1");

    assertThat(actual).hasSameElementsAs(expect);
  }

  @Test void captureIdentifiers__no_match() {
    subject = new TemplatedString(content_no_match);
    Set<String> actual = subject.getIdentifiers();
    Set<String> expect = Set.of();

    assertThat(actual).hasSameElementsAs(expect);
  }

  @Test void fill__one() {
    subject = new TemplatedString(content_one_match);

    String actual = subject.fill(map);
    String expect = "a -m- z";

    assertThat(actual).isEqualTo(expect);
  }

  @Test void fill__two() {
    subject = new TemplatedString(content_two_match);

    String actual = subject.fill(map);
    String expect = "a -m1- m -m2- z";

    assertThat(actual).isEqualTo(expect);
  }

  @Test void fill__dupe() {
    subject = new TemplatedString(content_has_dupe);

    String actual = subject.fill(map);
    String expect = "a -m1- m -m1- z";

    assertThat(actual).isEqualTo(expect);
  }

  @Test void fill__none() {
    subject = new TemplatedString(content_no_match);

    String actual = subject.fill(map);
    String expect = "a z";

    assertThat(actual).isEqualTo(expect);
  }

  @Test void getContents() {
    subject = new TemplatedString(content_one_match);
    assertThat(subject.getContents()).isEqualTo(content_one_match);
  }

}
