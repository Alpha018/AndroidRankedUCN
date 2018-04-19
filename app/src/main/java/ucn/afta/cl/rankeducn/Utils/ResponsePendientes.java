package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Creado por black el 17/04/2018.
 */
public class ResponsePendientes {
    @SerializedName("_id")
    @Expose
    @Getter
    @Setter
    private String id;

    @SerializedName("usuarioPrincipal")
    @Expose
    @Getter
    @Setter
    private Usuario usuarioPrincipal;

    @SerializedName("usuarioSegundario")
    @Expose
    @Getter
    @Setter
    private Usuario usuarioSecundario;

    @SerializedName("fecha")
    @Expose
    @Getter
    @Setter
    private Date fecha;

    @SerializedName("ganador")
    @Expose
    @Getter
    @Setter
    private Usuario ganador;

    @SerializedName("confirmado")
    @Expose
    @Getter
    @Setter
    private Boolean confirmado;

}
