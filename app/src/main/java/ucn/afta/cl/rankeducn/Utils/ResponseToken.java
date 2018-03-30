package ucn.afta.cl.rankeducn.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tomás on 29/03/2018.
 */

public class ResponseToken {
    @SerializedName("token")
    @Expose
    @Getter
    @Setter
    String token;
}
