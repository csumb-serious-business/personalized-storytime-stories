package storytime.parent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import storytime.child.Child;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
// note: these tests are just for coverage,
// since this is a Model entity with no logic

@SpringBootTest
class ParentTest {

    // test subject
    private Parent subject;

    @BeforeEach
    void setUp() {
        subject = new Parent();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_entity() {

        subject.setId(1L);
        assertThat(subject.getId()).isEqualTo(1L);

        subject.setUsername("Parent");
        assertThat(subject.getUsername()).isEqualTo("Parent");

        subject.setPassphrase("passphrase");
        assertThat(subject.getPassphrase()).isEqualTo("passphrase");

        Set<Child> children = Set.of(new Child(), new Child());
        subject.setChildren(children);
        assertThat(subject.getChildren()).isEqualTo(children);

        assertThat(subject.toString()).isEqualTo("Parent{1}");
    }
}