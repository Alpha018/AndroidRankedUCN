package ucn.afta.cl.rankeducn.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET("/api/usuario/top10")
    Call<ArrayList<ResponseTop10>> getTop10(@Header("authorization") String header);

    @POST("/api/usuario/actualizarTokenFirebase")
    Call<ResponseBody> changeTokenFirebase(@Header("authorization") String header, @Body RequestChangeToken requestChangeToken);

    @GET("/api/partida/getpartidaspendientes")
    Call<ArrayList<ResponsePendientes>> getPartidasPendientes(@Header("authorization") String header);

    @POST("/api/partida/acceptarmatch")
    Call<ResponseBody> acceptarMatch(@Header("authorization") String header, @Body ResponsePendientes match);

    @GET("/api/usuario/actualizar")
    Call<ResponseActualizar> actualizarPuntajes(@Header("authorization") String header);

    @GET("/api/usuario/usuariosmatch")
    Call<ArrayList<ResponseMatch>> getJugadoresMatch(@Header("authorization") String header);

    @POST("/api/partida/hacermatch")
    Call<ResponseBody> hacerMatch(@Header("authorization") String header, @Body RequestMatch requestMatch);

    @POST("/api/usuario/buscarusuario")
    Call<ResponseMatch> buscarUsuario(@Header("authorization") String header, @Body String rutmail);
}
