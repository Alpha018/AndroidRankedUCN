package ucn.afta.cl.rankeducn.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Tomás on 29/03/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestLogin {

    @Getter
    @Setter
    private String rut;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Boolean gettoken;
}
