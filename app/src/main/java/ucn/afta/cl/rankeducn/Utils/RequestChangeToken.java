package ucn.afta.cl.rankeducn.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Creado por black el 13/04/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestChangeToken {

    /**
     * Nuevo token de firebase para el dispositivo
     */
    @Getter
    private String token;
}
