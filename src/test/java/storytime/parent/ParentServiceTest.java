package storytime.parent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest class ParentServiceTest {

  // predefined test objects
  private static final Parent validParent = new Parent(0, "Parent", "passphrase", new HashSet<>());
  private static final Parent modifiedParent =
    new Parent(0, "Parent", "new passphrase", new HashSet<>());
  private static final Parent dupeNameParentA =
    new Parent(1, "Dupe Parent", "passphrase", new HashSet<>());
  private static final Parent dupeNameParentB =
    new Parent(2, "Dupe Parent", "passphrase", new HashSet<>());

  private static final Parent emptyParent = new Parent();

  // test subject
  private ParentService subject;

  @Mock private ParentRepository parentRepository;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new ParentService(parentRepository);

    // mock parent-repo
    given(parentRepository.findById(0L)).willReturn(Optional.of(validParent));
    given(parentRepository.findById(-1L)).willReturn(Optional.empty());

    given(parentRepository.findByUsername("Parent")).willReturn(List.of(validParent));

    // note, username is enforced to be unique in entity, this is just a safeguard check
    given(parentRepository.findByUsername("Dupe Parent"))
      .willReturn(List.of(dupeNameParentA, dupeNameParentB));
    given(parentRepository.findByUsername("NOBODY")).willReturn(List.of());

    given(parentRepository.save(validParent)).willReturn(validParent);
    given(parentRepository.save(emptyParent)).willThrow(new IllegalArgumentException());
  }

  @AfterEach void tearDown() {

  }

  @Test void getIdForUsername__found_one() {
    Optional<Long> actual = subject.getIdForUsername("Parent");
    Optional<Long> expect = Optional.of(validParent.getId());

    assertThat(actual).isEqualTo(expect);
  }

  @Test void getIdForUsername__found_none() {
    Optional<Long> actual = subject.getIdForUsername("NOBODY");
    Optional<Long> expect = Optional.empty();

    assertThat(actual).isEqualTo(expect);
  }

  @Test void getIdForUsername__found_two() {
    Optional<Long> actual = subject.getIdForUsername("Dupe Parent");
    Optional<Long> expect = Optional.of(dupeNameParentA.getId());

    assertThat(actual).isEqualTo(expect);
  }

  @Test void loginAttempt__valid() {
    Optional<String> actual = subject.loginAttempt(validParent);
    Optional<String> expect = Optional.empty();

    assertThat(actual).isEqualTo(expect);
  }

  @Test void loginAttempt__does_not_exist() {
    Optional<String> actual = subject.loginAttempt(emptyParent);
    Optional<String> expect = Optional.of(ParentService.LOGIN_FAIL_MESSAGE);

    assertThat(actual).isEqualTo(expect);
  }

  @Test void loginAttempt__wrong_pasphrase() {
    Optional<String> actual = subject.loginAttempt(modifiedParent);
    Optional<String> expect = Optional.of(ParentService.LOGIN_FAIL_MESSAGE);

    assertThat(actual).isEqualTo(expect);
  }

}
