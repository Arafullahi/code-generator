
package org.ibs.cds.gode.entity.type;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class DataPipelineConf extends ManagedEntity
{
    public DataPipelineConf() {
        this.pipelines = new ArrayList();
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pipeline> pipelines = null;
    private final static long serialVersionUID = 3449967976714433108L;

}
