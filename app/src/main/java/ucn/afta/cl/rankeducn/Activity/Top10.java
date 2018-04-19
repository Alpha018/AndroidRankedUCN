package ucn.afta.cl.rankeducn.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.ApiServices;
import ucn.afta.cl.rankeducn.Utils.ResponseTop10;

public class Top10 extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lista)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        setActionBar(toolbar);

        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "NOTOKEN");

        if (token.equals("NOTOKEN")) {
            Intent intentReg = new Intent(Top10.this, MainActivity.class);
            Top10.this.startActivity(intentReg);
            finish();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiServices services = retrofit.create(ApiServices.class);

        Call<ArrayList<ResponseTop10>> call = services.getTop10(token);
        call.enqueue(new Callback<ArrayList<ResponseTop10>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseTop10>> call, Response<ArrayList<ResponseTop10>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ResponseTop10> usuarios = response.body();
                    ImagenAdapter adapter = new ImagenAdapter(Top10.this, usuarios);
                    listView.setAdapter(adapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("desc"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseTop10>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
