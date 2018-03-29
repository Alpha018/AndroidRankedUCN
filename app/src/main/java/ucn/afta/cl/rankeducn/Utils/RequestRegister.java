package ucn.afta.cl.rankeducn.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucn.afta.cl.rankeducn.model.Usuario;

/**
 * Created by Tomás on 29/03/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestRegister {

    /**
     * Device ID para enviar a backend
     */
    @Getter
    String deviceID;

    /**
     * RUT del {@link Usuario}
     */
    @Getter
    String rut;

    /**
     * Password del {@link Usuario}
     */
    @Getter
    @Setter
    String password;

    /**
     * Email del {@link Usuario}
     */
    @Getter
    @Setter
    String email;

    /**
     * Nombre del {@link Usuario}
     */
    @Getter
    @Setter
    String nombre;

    /**
     * Apellido del {@link Usuario}
     */
    @Getter
    String apellido;
}
