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
    private String deviceId;

    /**
     * RUT del {@link Usuario}
     */
    @Getter
    private String rut;

    /**
     * Password del {@link Usuario}
     */
    @Getter
    @Setter
    private String password;

    /**
     * Email del {@link Usuario}
     */
    @Getter
    @Setter
    private String email;

    /**
     * Nombre del {@link Usuario}
     */
    @Getter
    @Setter
    private String nombre;

    /**
     * Apellido del {@link Usuario}
     */
    @Getter
    private String apellido;

    /**
     * Token para enviar mensajes por android Firebase
     */
    @Getter
    private String tokenFirebase;
}
