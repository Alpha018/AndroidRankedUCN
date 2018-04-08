package ucn.afta.cl.rankeducn.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

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

    @Multipart
    @POST("/api/usuario/uploadAvatar")
    Call<ResponseBody> postAvatar(@Header("authorization") String header, @Part MultipartBody.Part imagen, @Part("name") RequestBody name);

}
