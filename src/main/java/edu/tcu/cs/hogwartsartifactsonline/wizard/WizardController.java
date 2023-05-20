package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> wizards = this.wizardService.findAllWizards();

        List<WizardDto> wizardDtos = wizards.stream().map(wizardToWizardDtoConverter::convert).toList();

        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }

    @GetMapping("/{id}")
    public Result findWizardById(@PathVariable Integer id) {
        Wizard wizard = this.wizardService.findWizardById(id);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(wizard);

        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard newWizard = this.wizardService.addWizard(wizard);
        WizardDto newWizardDto = this.wizardToWizardDtoConverter.convert(newWizard);

        return new Result(true, StatusCode.SUCCESS, "Add Success", newWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizardById(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard newWizard = this.wizardService.updateWizardById(wizardId, update);
        WizardDto newWizardDto = this.wizardToWizardDtoConverter.convert(newWizard);

        return new Result(true, StatusCode.SUCCESS, "Update Success", newWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizardById(@PathVariable Integer wizardId) {
        this.wizardService.deleteWizardById(wizardId);

        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);

        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }

}
