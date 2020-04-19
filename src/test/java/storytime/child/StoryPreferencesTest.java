package storytime.child;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// note: these tests are just for coverage,
// since this is a Model entity with no logic
@SpringBootTest
class StoryPreferencesTest {

    // test subject
    private StoryPreferences subject;

    @BeforeEach
    void setUp() {
        subject = new StoryPreferences();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_entity() {
        long id = 1L;
        subject.setId(id);
        assertThat(subject.getId()).isEqualTo(id);

        Child owner = new Child();
        subject.setOwner(owner);
        assertThat(subject.getOwner()).isEqualTo(owner);

        String setting = "setting";
        subject.setSetting(setting);
        assertThat(subject.getSetting()).isEqualTo(setting);

        String protagonist = "protagonist";
        subject.setProtagonistCharacterName(protagonist);
        assertThat(subject.getProtagonistCharacterName()).isEqualTo(protagonist);

        String mom = "mom";
        subject.setMomCharacterName(mom);
        assertThat(subject.getMomCharacterName()).isEqualTo(mom);

        String dad = "dad";
        subject.setDadCharacterName(dad);
        assertThat(subject.getDadCharacterName()).isEqualTo(dad);

        String bro = "bro";
        subject.setBrotherCharacterName(bro);
        assertThat(subject.getBrotherCharacterName()).isEqualTo(bro);

        String sis = "sis";
        subject.setSisterCharacterName(sis);
        assertThat(subject.getSisterCharacterName()).isEqualTo(sis);

        String pet = "pet";
        subject.setPetCharacterName(pet);
        assertThat(subject.getPetCharacterName()).isEqualTo(pet);

        String species = "species";
        subject.setPetCharacterSpecies(species);
        assertThat(subject.getPetCharacterSpecies()).isEqualTo(species);

        assertThat(subject.toString()).isEqualTo("StoryPreferences{1}");
        assertThat(subject).isEqualToComparingFieldByField(
                new StoryPreferences(id, owner, setting, protagonist,
                        mom, dad, bro, sis, pet, species));
    }
}