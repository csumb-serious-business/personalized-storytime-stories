package storytime.child;


import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "STORY_PREFERENCES")
public class StoryPreferences {
    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @OneToOne
    private Child owner;

    @NotNull
    @Size(min = 3, max = 255)
    private String setting;

    @NotNull
    @Size(min = 3, max = 255)
    private String protagonistCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String momCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String dadCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String brotherCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String sisterCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String petCharacterName;

    @NotNull
    @Size(min = 3, max = 255)
    private String petCharacterSpecies;

    public StoryPreferences() {

    }

    public StoryPreferences(long id, Child owner, String setting, String protagonistCharacterName,
                            String momCharacterName, String dadCharacterName, String brotherCharacterName, String sisterCharacterName,
                            String petCharacterName, String petCharacterSpecies) {
        this.id = id;
        this.owner = owner;
        this.setting = setting;
        this.protagonistCharacterName = protagonistCharacterName;
        this.momCharacterName = momCharacterName;
        this.dadCharacterName = dadCharacterName;
        this.brotherCharacterName = brotherCharacterName;
        this.sisterCharacterName = sisterCharacterName;
        this.petCharacterName = petCharacterName;
        this.petCharacterSpecies = petCharacterSpecies;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getProtagonistCharacterName() {
        return protagonistCharacterName;
    }

    public void setProtagonistCharacterName(String protagonistCharacterName) {
        this.protagonistCharacterName = protagonistCharacterName;
    }

    public Child getOwner() {
        return owner;
    }

    public void setOwner(Child owner) {
        this.owner = owner;
    }

    public String getMomCharacterName() {
        return momCharacterName;
    }

    public void setMomCharacterName(String momCharacterName) {
        this.momCharacterName = momCharacterName;
    }

    public String getDadCharacterName() {
        return dadCharacterName;
    }

    public void setDadCharacterName(String dadCharacterName) {
        this.dadCharacterName = dadCharacterName;
    }

    public String getBrotherCharacterName() {
        return brotherCharacterName;
    }

    public void setBrotherCharacterName(String brotherCharacterName) {
        this.brotherCharacterName = brotherCharacterName;
    }

    public String getSisterCharacterName() {
        return sisterCharacterName;
    }

    public void setSisterCharacterName(String sisterCharacterName) {
        this.sisterCharacterName = sisterCharacterName;
    }

    public String getPetCharacterName() {
        return petCharacterName;
    }

    public void setPetCharacterName(String petCharacterName) {
        this.petCharacterName = petCharacterName;
    }

    public String getPetCharacterSpecies() {
        return petCharacterSpecies;
    }

    public void setPetCharacterSpecies(String petCharacterSpecies) {
        this.petCharacterSpecies = petCharacterSpecies;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + this.id + "}";
    }
}
