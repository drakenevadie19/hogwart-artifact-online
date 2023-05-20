package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAllWizards() {
        List<Wizard> wizards = this.wizardRepository.findAll();

        return wizards;
    }

    public Wizard findWizardById(Integer wizardId) {

        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

    }

    public Wizard addWizard(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    public Wizard updateWizardById(Integer id, Wizard updatedWizard) {

        return this.wizardRepository.findById(id).map(existedWizard -> {

            existedWizard.setName(updatedWizard.getName());

            return this.wizardRepository.save(existedWizard);
        }).orElseThrow(() -> new ObjectNotFoundException("wizard", id));

    }

    public void deleteWizardById(Integer wizardId) {
        Wizard w = this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // remove the owner of artifact belongs to w
        // if not it will cause error as artifact already have foreign key refer to w by ID
        w.deleteArtifacts();

        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        // Find artifact by Id from DB
        Artifact artifact = this.artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        // Find wizard by Id from DB
        Wizard wizard = this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // Artifact assignment
        // Need to check if artifact is already owned by some wizard

        if(artifact.getOwner() != null) {
            artifact.getOwner().removeArtifact(artifact);
        }

        wizard.addArtifact(artifact);

    }
}
