package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by black on 29/03/2018.
 */

public class ResponseLogin {
    @SerializedName("puntaje")
    @Expose
    @Getter
    @Setter
    int puntaje;

    @SerializedName("partidasJugadas")
    @Expose
    @Getter
    @Setter
    int partidasJugadas;

    @SerializedName("apellido")
    @Expose
    @Getter
    @Setter
    String apellido;

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

    @SerializedName("rut")
    @Expose
    @Getter
    @Setter
    String rut;

    @SerializedName("avatar")
    @Expose
    @Getter
    @Setter
    String avatar;

    @SerializedName("partidasGanadas")
    @Expose
    @Getter
    @Setter
    int partidasGanadas;

    @SerializedName("partidasPerdidas")
    @Expose
    @Getter
    @Setter
    int partidasPerdidas;

    @SerializedName("_id")
    @Expose
    @Getter
    @Setter
    String id;

    @SerializedName("tokenFirebase")
    @Expose
    @Getter
    @Setter
    String tokenFirebase;
}
