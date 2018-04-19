package ucn.afta.cl.rankeducn.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Creado por black el 18/04/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestMatch {
    /**
     * Device ID para enviar a backend
     */
    @Getter
    private String rutSegundo;

    /**
     * RUT del {@link ucn.afta.cl.rankeducn.model.Usuario}
     */
    @Getter
    private String rutGanador;
}
