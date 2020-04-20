package storytime.child;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import storytime.parent.Parent;

import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class ChildServiceTest {
    // predefined test objects
    private static final Parent parent = new Parent(0, "Parent",
            "passphrase", Set.of());

    private static final Child validChild = new Child(1, "ChildA", parent,
            new StoryPreferences(), null, null);

    private static final Child emptyChild = new Child();

    // test subject
    private ChildService subject;

    @Mock
    private ChildRepository childRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        subject = new ChildService(childRepository);

        // mock child-repo
        given(childRepository.findById(1L)).willReturn(Optional.of(validChild));
        given(childRepository.findById(-1L)).willReturn(Optional.empty());

        given(childRepository.save(validChild)).willReturn(validChild);
        given(childRepository.save(emptyChild)).willThrow(new IllegalArgumentException());

    }

    @AfterEach
    void tearDown() {
    }

}