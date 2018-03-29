package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tomás on 29/03/2018.
 */

public class ResponseRegister {

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

    @SerializedName("password")
    @Expose
    @Getter
    @Setter
    String password;

    @SerializedName("rut")
    @Expose
    @Getter
    @Setter
    String rut;

    @SerializedName("deviceId")
    @Expose
    @Getter
    @Setter
    String deviceId;

    @SerializedName("avatar")
    @Expose
    @Getter
    @Setter
    String avatar;

    @SerializedName("_id")
    @Expose
    @Getter
    @Setter
    String id;
}
