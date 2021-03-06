package org.ibs.cds.gode.codegenerator.api;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ibs.cds.gode.codegenerator.api.usage.CodeGeneratorApi;
import org.ibs.cds.gode.entity.manager.AppManager;
import org.ibs.cds.gode.entity.manager.EntitySpecManager;
import org.ibs.cds.gode.entity.manager.RelationshipEntitySpecManager;
import org.ibs.cds.gode.entity.operation.Executor;
import org.ibs.cds.gode.entity.operation.Logic;
import org.ibs.cds.gode.entity.operation.Processor;
import org.ibs.cds.gode.entity.type.RelationshipEntitySpec;
import org.ibs.cds.gode.entity.type.StatefulEntitySpec;
import org.ibs.cds.gode.exception.KnownException;
import org.ibs.cds.gode.pagination.PageContext;
import org.ibs.cds.gode.pagination.PagedData;
import org.ibs.cds.gode.util.APIArgument;
import org.ibs.cds.gode.web.Request;
import org.ibs.cds.gode.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CodeGeneratorApi
@RequestMapping("/artifact")
@Api(tags={"Gode(e) Artifact endpoint"})
public class ArtifactEndpoint {

    @Autowired
    private EntitySpecManager entitySpecManager;

    @Autowired
    private RelationshipEntitySpecManager relationshipEntitySpecManager;

    @Autowired
    private AppManager appManager;

    @PostMapping(path="/entity")
    @ApiOperation(value = "Operation to create Entity")
    public Response<StatefulEntitySpec> createEntity(@RequestBody Request<StatefulEntitySpec> entityRequest){
        return Executor.run(Logic.savePure(), entityRequest, entitySpecManager,KnownException.SAVE_FAILED, "/entity");
    }

    @GetMapping(path="/entity")
    @ApiOperation(value = "Operation to get Entity")
    public Response<StatefulEntitySpec> queryEntity(Long artifactId){
        return Executor.run(Logic.findByIdPure(), artifactId, entitySpecManager,KnownException.QUERY_FAILED, "/entity");
    }

    @GetMapping(path="/entity/view")
    @ApiOperation(value = "Operation to view Entity")
    public Response<PagedData<StatefulEntitySpec>> queryEntity(@QuerydslPredicate(root = StatefulEntitySpec.class) Predicate predicate, @ModelAttribute APIArgument apiargs){
        return predicate == null ?
                Executor.run(Logic.findAllPure(), PageContext.fromAPI(apiargs), entitySpecManager,KnownException.QUERY_FAILED, "/entity/view")
                : Executor.run(Logic.findAllByPredicatePure(), PageContext.fromAPI(apiargs),predicate, entitySpecManager,KnownException.QUERY_FAILED, "/entity/view");
    }



    @GetMapping(path="/brief")
    @ApiOperation(value = "Operation to get brief package")
    public Response<List<Brief>> brief(BriefType type){
        String urlOrHandle = "/brief";
        switch (type){
            case APP: return Processor.successResponse(appManager.findTransform(BriefUtil::toBrief), type.name(), urlOrHandle);
            case RELATIONSHIP: return Processor.successResponse(relationshipEntitySpecManager.findTransform(BriefUtil::toBrief), type.name(), urlOrHandle);
            case ENTITY: default: return Processor.successResponse(entitySpecManager.findTransform(BriefUtil::toBrief), type.name(), urlOrHandle);
        }
    }

    @PostMapping(path="/relationship")
    @ApiOperation(value = "Operation to create relationship")
    public Response<RelationshipEntitySpec> createRelationshipEntity(@RequestBody Request<RelationshipEntitySpec> entityRequest){
        return Executor.run(Logic.savePure(), entityRequest, relationshipEntitySpecManager,KnownException.SAVE_FAILED, "/relationship");
    }

    @GetMapping(path="/relationship")
    @ApiOperation(value = "Operation to get Relationship")
    public Response<RelationshipEntitySpec> queryRelationshipEntity(Long artifactId){
        return Executor.run(Logic.findByIdPure(), artifactId, relationshipEntitySpecManager,KnownException.QUERY_FAILED, "/relationship");
    }

    @GetMapping(path="/relationship/view")
    @ApiOperation(value = "Operation to view Relationships")
    public Response<PagedData<RelationshipEntitySpec>> queryRelationshipEntity(@QuerydslPredicate(root = RelationshipEntitySpec.class) Predicate predicate, @ModelAttribute APIArgument apiargs){
        return predicate == null ?
                Executor.run(Logic.findAllPure(), PageContext.fromAPI(apiargs), relationshipEntitySpecManager,KnownException.QUERY_FAILED, "/entity/view")
                : Executor.run(Logic.findAllByPredicatePure(), PageContext.fromAPI(apiargs),predicate, relationshipEntitySpecManager,KnownException.QUERY_FAILED, "/entity/view");
    }
}
