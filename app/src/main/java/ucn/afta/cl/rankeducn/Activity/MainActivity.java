package ucn.afta.cl.rankeducn.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.ApiServices;
import ucn.afta.cl.rankeducn.Utils.RequestLogin;
import ucn.afta.cl.rankeducn.Utils.ResponseLogin;
import ucn.afta.cl.rankeducn.Utils.ResponseToken;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.registrar)
    TextView registrar;

    @BindView(R.id.cardView)
    CardView login;

    @BindView(R.id.email)
    EditText user;

    @BindView(R.id.password)
    EditText password;

    String usuario;

    String clave;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String valor = sharedPref.getString("token", "NOTOKEN");

        if (!valor.equals("NOTOKEN")) {
            Intent intentReg = new Intent(MainActivity.this, Profile.class);
            MainActivity.this.startActivity(intentReg);
            finish();
        }

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(intentReg);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = new ProgressDialog(MainActivity.this);
                progress.setTitle("Registrando");
                progress.setMessage("Por favor espere...");
                progress.show();

                usuario = user.getText().toString().trim();
                clave = password.getText().toString().trim();

                RequestLogin requestLogin = RequestLogin.builder()
                        .gettoken(false)
                        .password(clave)
                        .rut(usuario)
                        .build();

                //desde aqui se usa el retrofit para obtener los datos del usuario
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.BASE_URL))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final ApiServices service = retrofit.create(ApiServices.class);

                Call<ResponseLogin> call = service.login(requestLogin);
                call.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        progress.dismiss();
                        if (response.isSuccessful()) {

                            ResponseLogin responseLogin = response.body();

                            salvarDatos(responseLogin);

                            RequestLogin requestLoginAux = RequestLogin.builder()
                                    .gettoken(true)
                                    .password(clave)
                                    .rut(usuario)
                                    .build();

                            //desde aqui se usa el retrofit para obtener el token
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(getString(R.string.BASE_URL))
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            final ApiServices service = retrofit.create(ApiServices.class);

                            Toast.makeText(getApplicationContext(), "Bienvenido " + responseLogin.getNombre(), Toast.LENGTH_LONG).show();

                            Call<ResponseToken> token = service.token(requestLoginAux);
                            token.enqueue(new Callback<ResponseToken>() {
                                @Override
                                public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                                    if (response.isSuccessful()) {

                                        ResponseToken token = response.body();

                                        salvarToken(token);

                                        Intent intentReg = new Intent(MainActivity.this, Profile.class);
                                        MainActivity.this.startActivity(intentReg);
                                        finish();
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
                                public void onFailure(Call<ResponseToken> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                }
                            });


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
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void salvarDatos(final ResponseLogin usuario) {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nombre", usuario.getNombre());
        editor.putString("apellido", usuario.getApellido());
        editor.putString("email", usuario.getEmail());
        editor.putString("rut", usuario.getRut());
        editor.putString("avatar", usuario.getAvatar());
        editor.putInt("puntaje", usuario.getPuntaje());
        editor.putInt("partidasJugadas", usuario.getPartidasJugadas());
        editor.putInt("partidasGanadas", usuario.getPartidasGanadas());
        editor.putInt("partidasPerdidas", usuario.getPartidasPerdidas());
        editor.putString("tokenFirebase", usuario.getTokenFirebase());
        editor.apply();
    }

    private void salvarToken(final ResponseToken token) {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token.getToken());
        editor.apply();
    }
}
