package org.ibs.cds.gode.codegenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.ibs.cds.gode.codegenerator.artefact.Buildable;
import org.ibs.cds.gode.codegenerator.config.CodeGenerationComponent;
import org.ibs.cds.gode.codegenerator.exception.CodeGenerationFailure;
import org.ibs.cds.gode.codegenerator.model.build.BuildModel;
import org.ibs.cds.gode.codegenerator.spec.UsageSpec;
import org.ibs.cds.gode.entity.type.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CodeApp extends Specification implements Buildable, CodeGenerationComponent {
    private Set<CodeEntity> entities;
    private Set<CodeEntityRelationship> relationships;
    private CodeAppFunctionNode appFunction;
    private UsageSpec usage;
    private App model;
    private BuildModel buildModel;
    private Set<CodeEntity> dependencies;
    private boolean secure;
    private boolean systemQueue;

    public CodeApp(App model, BuildModel buildModel) {
        this.buildModel = buildModel;
        this.model = model;
        Map<Long, EntityStorePolicy> entityStorePolicyMap = resolveEntityStorePolicy(buildModel);
        this.entities = model.getEntities().stream().map(entity -> new CodeEntity(entity, buildModel, entityStorePolicyMap)).collect(Collectors.toSet());
        this.appFunction = new CodeAppFunctionNode(model, buildModel);
        this.usage = model.getUsage();
        this.dependencies = model.getDependencies().stream().map(entity -> new CodeEntity(entity, buildModel, entityStorePolicyMap)).collect(Collectors.toSet());
        this.setName(model.getName());
        this.setDescription(model.getDescription());
        this.setVersion(model.getVersion());
        this.secure = buildModel.isSecure();
        this.systemQueue = buildModel.isSystemQueue();
        List<RelationshipEntitySpec> relationships = model.getRelationships();
        if(CollectionUtils.isEmpty(buildModel.getRelationshipStorePolicy()) && CollectionUtils.isNotEmpty(relationships)){
            throw CodeGenerationFailure.SYSTEM_ERROR.provide("Relationship store policy undefined");
        }
        Map<Long, RelationshipStorePolicy> map =buildModel.getRelationshipStorePolicy().stream().collect(Collectors.toMap(s->s.getRelationship().getArtifactId(), s->s));
        this.relationships = CollectionUtils.isEmpty(relationships) ? Collections.emptySet() :
                relationships.stream().map(k-> new CodeEntityRelationship(k,map.get(k.getArtifactId()), buildModel)).collect(Collectors.toSet());
    }

    public Map<Long,EntityStorePolicy> resolveEntityStorePolicy(BuildModel buildModel){
        return buildModel.getEntityStorePref().stream().collect(Collectors.toMap(s->s.getEntity().getArtifactId(),s->s));
    }

    @Override
    public ComponentName getComponentName() {
        return ComponentName.APP;
    }

    @JsonIgnore
    public List<CodeObjectField> getObjectFields() {
        List<CodeEntity> entities = new ArrayList<>();
        entities.addAll(this.entities);
        entities.addAll(dependencies);
        return entities.stream().flatMap(e -> e.getFields().stream())
                .distinct()
                .map(CodeEntityField::getObjectField).filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }
}
