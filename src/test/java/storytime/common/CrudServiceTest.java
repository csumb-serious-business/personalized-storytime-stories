package storytime.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@SpringBootTest class CrudServiceTest {
  // predefined test objects
  private static final FakeString validFakeString = new FakeString("valid");
  private static final FakeString emptyFakeString = new FakeString();

  @Mock private static FakeRepo fakeRepo;

  // test subject
  private CrudService<FakeString, FakeRepo> subject;

  @BeforeEach void setUp() {
    MockitoAnnotations.initMocks(this);
    subject = new CrudService<>(fakeRepo) {
      /* use super's impls */
    };

    given(fakeRepo.save(validFakeString)).willReturn(validFakeString);
    given(fakeRepo.save(emptyFakeString)).willThrow(new IllegalArgumentException());

    //// no given for delete valid case since method is void
    doThrow(IllegalArgumentException.class).when(fakeRepo).delete(emptyFakeString);

    given(fakeRepo.findById(0L)).willReturn(Optional.of(validFakeString));
    given(fakeRepo.findById(-1L)).willReturn(Optional.empty());

  }

  @AfterEach void tearDown() {
  }

  @Test void create__valid() {
    assertThat(subject.create(validFakeString)).isEqualTo(Optional.of(validFakeString));
  }

  @Test void create__invalid() {
    assertThat(subject.create(emptyFakeString)).isEqualTo(Optional.empty());
  }

  @Test void read__valid() {
    Optional<FakeString> actual = subject.read(0L);
    Optional<FakeString> expect = Optional.of(validFakeString);

    assertThat(actual).isEqualTo(expect);

  }

  @Test void read__not_found() {
    Optional<FakeString> actual = subject.read(-1L);
    Optional<FakeString> expect = Optional.empty();

    assertThat(actual).isEqualTo(expect);

  }

  @Test void update__valid() {
    assertThat(subject.update(validFakeString)).isEqualTo(Optional.of(validFakeString));
  }

  @Test void update__invalid() {
    assertThat(subject.update(emptyFakeString)).isEqualTo(Optional.empty());
  }

  @Test void delete__valid() {
    assertTrue(subject.delete(validFakeString));
  }

  @Test void delete__invalid() {
    assertFalse(subject.delete(emptyFakeString));
  }

  private interface FakeRepo extends JpaRepository<FakeString, Long> {
  }


  @Entity private static class FakeString implements HasId {
    @Id @GeneratedValue Long id;
    String s;

    public FakeString() {

    }

    public FakeString(String s) {
      this.s = s;
    }

    @Override public Long getId() {
      return id;
    }
  }
}
