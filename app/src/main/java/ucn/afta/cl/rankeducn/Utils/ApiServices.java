package ucn.afta.cl.rankeducn.Utils;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Interfaze que implementa los request a la API de la aplicación
 * Created by Tomás on 29/03/2018.
 */

public interface ApiServices {

    @PUT("/api/usuario/register")
    Call<ResponseRegister> request(@Body RequestRegister requestRegister);

    @POST("/api/usuario/login")
    Call<ResponseLogin> login(@Body RequestLogin requestLogin);

    @POST("/api/usuario/login")
    Call<ResponseToken> token(@Body RequestLogin requestLogin);

}
