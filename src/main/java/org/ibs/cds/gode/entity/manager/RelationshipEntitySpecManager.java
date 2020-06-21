package org.ibs.cds.gode.entity.manager;

import org.ibs.cds.gode.entity.repo.RelationshipEntitySpecRepository;
import org.ibs.cds.gode.entity.type.RelationshipEntitySpec;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RelationshipEntitySpecManager extends EntityManager<RelationshipEntitySpec, RelationshipEntitySpec, Long> {


    public RelationshipEntitySpecManager(RelationshipEntitySpecRepository repo) {
        super(repo, null);
    }

    public <T> List<T> findTransform(Function<RelationshipEntitySpec,T> transformer){
        RelationshipEntitySpecRepository repo = this.repository.get();
        List<RelationshipEntitySpec> all = repo.findAll();
        return all == null ? Collections.emptyList() : all.stream().map(transformer).collect(Collectors.toList());
    }

    @Override
    public Optional<RelationshipEntitySpec> transformEntity(Optional<RelationshipEntitySpec> entitySpec) {
        return entitySpec;
    }

    @Override
    public Optional<RelationshipEntitySpec> transformView(Optional<RelationshipEntitySpec> entity) {
        return entity;
    }
}
