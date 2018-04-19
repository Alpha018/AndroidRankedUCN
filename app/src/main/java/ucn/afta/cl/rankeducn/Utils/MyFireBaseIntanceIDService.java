package ucn.afta.cl.rankeducn.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.R;

/**
 * Creado por black el 13/04/2018.
 */
public class MyFireBaseIntanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Update token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // tenemos el nuevo token

        // salvar el token actualizado
        RequestChangeToken request = RequestChangeToken.builder()
                .token(refreshedToken)
                .build();

        //desde aqui se usa el retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiServices service = retrofit.create(ApiServices.class);

        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String token = sharedPref.getString("token", "Error");

        if (token.equals("Error")) {
            Toast.makeText(getApplicationContext(), "Error al cambiar el token de Firebase", Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseBody> call = service.changeTokenFirebase(token, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    ResponseBody responsebody = response.body();

                    try {
                        JSONObject jObjError = new JSONObject(responsebody.string());

                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("tokenFirebase", jObjError.getString("token"));
                        editor.apply();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: al guardar el nuevo token de Firebase", Toast.LENGTH_LONG).show();
                    }
                } else {

                    try {

                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), "Error: " + jObjError.getString("desc"), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo al conectarse con el servidor", Toast.LENGTH_LONG).show();
            }
        });

    }
}
