package org.ibs.cds.gode.codegenerator.config;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.ibs.cds.gode.codegenerator.exception.CodeGenerationFailure;
import org.ibs.cds.gode.codegenerator.model.build.BuildModel;
import org.ibs.cds.gode.codegenerator.spec.YamlReadWriteUtil;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class EngineConfiguration {

    private File processPath;
    private String generatePath;
    private final Map<CodeGenerationComponent.ComponentName, ComponentConfiguration> componentConfiguration;
    private BuildComponentConfiguration buildConfiguration;

    public EngineConfiguration(BuildModel model) {
        this.buildConfiguration = getBuildComponentConfiguration(model);
        this.componentConfiguration = buildConfiguration.getConfiguration().stream()
                                    .collect(Collectors.toUnmodifiableMap(s->s.getType(), s->s));
    }

    private BuildComponentConfiguration getBuildComponentConfiguration(BuildModel model) {
        try {
            return YamlReadWriteUtil.readResource(BuildComponentConfiguration.getComponentConfigFile(model), BuildComponentConfiguration.getConfiguration(model));
        } catch (Exception e) {
            throw CodeGenerationFailure.SYSTEM_ERROR.provide(e);
        }
    }
}