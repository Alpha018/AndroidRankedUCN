package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Creado por black el 10/04/2018.
 */

public class ResponseTop10 {

    @SerializedName("puntaje")
    @Expose
    @Getter
    @Setter
    int puntaje;

    @SerializedName("partidasPerdidas")
    @Expose
    @Getter
    @Setter
    int partidasPerdidas;

    @SerializedName("partidasGanadas")
    @Expose
    @Getter
    @Setter
    int partidasGanadas;

    @SerializedName("partidasJugadas")
    @Expose
    @Getter
    @Setter
    int partidasJugadas;

    @SerializedName("rut")
    @Expose
    @Getter
    @Setter
    String rut;

    @SerializedName("nombre")
    @Expose
    @Getter
    @Setter
    String nombre;

    @SerializedName("email")
    @Expose
    @Getter
    @Setter
    String email;

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
