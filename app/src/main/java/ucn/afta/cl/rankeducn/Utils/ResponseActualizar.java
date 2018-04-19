package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Creado por black el 18/04/2018.
 */
public class ResponseActualizar {
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
}
