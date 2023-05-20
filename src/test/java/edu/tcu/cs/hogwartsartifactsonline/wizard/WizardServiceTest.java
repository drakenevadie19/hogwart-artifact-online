package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);

        this.wizards = new ArrayList<>();
        wizards.add(w1);
        wizards.add(w2);
        wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllWizardsSuccess() {
        // Given
        given(wizardRepository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> wizards = this.wizardService.findAllWizards();

        // Then
        assertThat(wizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void findWizardByIdSuccess() {
        // Given
        Wizard exist = this.wizards.get(0);
        given(wizardRepository.findById(1)).willReturn(Optional.of(exist));

        // When
        Wizard wizard = this.wizardService.findWizardById(1);

        // Then
        assertThat(wizard.getId()).isEqualTo(wizard.getId());
        assertThat(wizard.getName()).isEqualTo(wizard.getName());
        assertThat(wizard.getNumberOfArtifacts()).isEqualTo(wizard.getNumberOfArtifacts());
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void findWizardByIdNotFound() {
        // Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.findWizardById(1);
        });

        // Then
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void addWizardSuccess() {
        // Given
        Wizard wiz = new Wizard();
        wiz.setName("Harry Potter2");

        given(this.wizardRepository.save(wiz)).willReturn(wiz);

        // When
        Wizard newWizard = this.wizardRepository.save(wiz);

        // Then
        assertThat(newWizard.getName()).isEqualTo(wiz.getName());
        verify(this.wizardRepository, times(1)).save(wiz);

    }

    @Test
    void testUpdateWizardByIdSuccess() {
        // Given
        Wizard update = new Wizard();
        update.setName("John Doe");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizards.get(0)));
        given(this.wizardRepository.save(wizards.get(0))).willReturn(wizards.get(0));

        // When
        Wizard updatedWizard = this.wizardService.updateWizardById(1, update);

        // Then
        assertThat(updatedWizard.getName()).isEqualTo(updatedWizard.getName());

        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(wizards.get(0));
    }

    @Test
    void testUpdateWizardByIdNotFound() {
        // Given
        Wizard update = new Wizard();
        update.setName("John Doe");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> wizardService.updateWizardById(1, update));

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(0)).save(wizards.get(0));
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = wizards.get(0);
        List<Artifact> artifacts = wizard.getArtifacts();

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        // When
        this.wizardService.deleteWizardById(1);

        // Then
        assertThat(artifacts.get(0).getOwner()).isEqualTo(null);
        verify(this.wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.wizardRepository.findById(9)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.deleteWizardById(9);
        });

        // Then
        // only check this when element is not found and throw exception
        verify(this.wizardRepository, times(1)).findById(9);
    }

    @Test
    void testAssignArtifactSuccess() {
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));

        // When
        this.wizardService.assignArtifact(3, "1250808601744904192");
        // Then
        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(a);
        assertThat(w2.getArtifacts()).doesNotContain(a);
    }

    @Test
    void testAssignArtifactNotFoundWizardId() {
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo(2);

    }

    @Test
    void testAssignArtifactNotFoundArtifactId() {
        // Given
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
    }
}