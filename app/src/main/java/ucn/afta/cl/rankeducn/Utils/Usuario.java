package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Creado por black el 17/04/2018.
 */
public class Usuario {

    @SerializedName("rut")
    @Expose
    @Getter
    @Setter
    String rut;

    @SerializedName("email")
    @Expose
    @Getter
    @Setter
    String email;

    @SerializedName("nombre")
    @Expose
    @Getter
    @Setter
    String nombre;

    @SerializedName("apellido")
    @Expose
    @Getter
    @Setter
    String apellido;

    @SerializedName("avatar")
    @Expose
    @Getter
    @Setter
    String avatar;
}
