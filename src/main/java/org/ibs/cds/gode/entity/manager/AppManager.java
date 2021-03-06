package org.ibs.cds.gode.entity.manager;

import org.ibs.cds.gode.entity.repo.AppRepository;
import org.ibs.cds.gode.entity.type.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AppManager extends PureEntityManager<App, Long> {


    @Autowired
    public AppManager(AppRepository storeEntityRepo) {
        super(storeEntityRepo, null);
    }

    public App find(String name, Long version){
        AppRepository repo = repository.get();
        return repo.findByNameAndVersion(name, version);
    }

    public <T> List<T> findTransform(Function<App,T> transformer){
        AppRepository repo = repository.get();
        List<App> all = repo.findAll();
        return all == null ? Collections.emptyList() : all.stream().map(transformer).collect(Collectors.toList());
    }
}
