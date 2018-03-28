package ucn.afta.cl.rankeducn.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Tomas on 28/03/2018.
 */

// @EqualsAndHashCode(of = "id", callSuper = false)
public abstract class BaseModel extends com.raizlabs.android.dbflow.structure.BaseModel {

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

    }

}
