package ucn.afta.cl.rankeducn.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.ApiServices;
import ucn.afta.cl.rankeducn.Utils.RequestMatch;
import ucn.afta.cl.rankeducn.Utils.ResponseMatch;
import ucn.afta.cl.rankeducn.Utils.ResponsePendientes;

public class Match extends AppCompatActivity {

    @BindView(R.id.busqueda)
    EditText ingresarRutMail;

    @BindView(R.id.cardView)
    CardView botonMatch;

    @BindView(R.id.lista)
    ListView listaUsuarios;

    private String tokenHeader;

    private ArrayList<ResponseMatch> usuariosFinal;

    private ResponseMatch itemFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);


        //Poner el list view
        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "NOTOKEN");

        if (token.equals("NOTOKEN")) {
            Intent intentReg = new Intent(Match.this, MainActivity.class);
            Match.this.startActivity(intentReg);
            finish();
        }
        tokenHeader = token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiServices services = retrofit.create(ApiServices.class);

        Call<ArrayList<ResponseMatch>> call = services.getJugadoresMatch(token);
        call.enqueue(new Callback<ArrayList<ResponseMatch>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseMatch>> call, Response<ArrayList<ResponseMatch>> response) {
                if (response.isSuccessful()) {
                    usuariosFinal = response.body();
                    ImagenAdapterMatch adapter = new ImagenAdapterMatch(Match.this, usuariosFinal);
                    listaUsuarios.setAdapter(adapter);
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
            public void onFailure(Call<ArrayList<ResponseMatch>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });

        //Confirmar la partida haciendo click en el listview
        listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ResponseMatch item = (ResponseMatch) listaUsuarios.getItemAtPosition(position);

                itemFinal = item;
                new SweetAlertDialog(Match.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("¿Estás seguro?")
                        .setContentText("Hacer un match con: " + item.getNombre() + " " + item.getApellido() + ", ¿Como oponente?")
                        .setCancelText("No, Cancelalo!!")
                        .setConfirmText("Si, Hacer Match!!")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                new SweetAlertDialog(Match.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("¿Quien Ganó?")
                                        .setContentText("¿Quien fue el ganador del encuentro?")
                                        .setCancelText("EL :c")
                                        .setConfirmText("YO!!")
                                        .setNeutralText("EMPATE!!")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(getString(R.string.BASE_URL))
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();
                                                final ApiServices services = retrofit.create(ApiServices.class);

                                                //validar si existe una sesion
                                                Context context = getApplicationContext();
                                                SharedPreferences sharedPref = context.getSharedPreferences(
                                                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                                                String rutMio = sharedPref.getString("rut", "Error");

                                                if (rutMio.equals("Error")) {
                                                    Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }

                                                RequestMatch requestMatch = RequestMatch.builder()
                                                        .rutGanador(rutMio)
                                                        .rutSegundo(item.getRut())
                                                        .build();

                                                Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                call.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            ResponseBody responsebody = response.body();

                                                            try {
                                                                JSONObject jObjError = new JSONObject(responsebody.string());
                                                                Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                Match.this.startActivity(intentAvatar);
                                                                finish();
                                                            } catch (Exception e) {
                                                                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
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
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(getString(R.string.BASE_URL))
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();
                                                final ApiServices services = retrofit.create(ApiServices.class);

                                                RequestMatch requestMatch = RequestMatch.builder()
                                                        .rutGanador(item.getRut())
                                                        .rutSegundo(item.getRut())
                                                        .build();

                                                Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                call.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            ResponseBody responsebody = response.body();

                                                            try {
                                                                JSONObject jObjError = new JSONObject(responsebody.string());
                                                                Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                Match.this.startActivity(intentAvatar);
                                                                finish();
                                                            } catch (Exception e) {
                                                                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
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
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                        .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(getString(R.string.BASE_URL))
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();
                                                final ApiServices services = retrofit.create(ApiServices.class);

                                                RequestMatch requestMatch = RequestMatch.builder()
                                                        .rutGanador(null)
                                                        .rutSegundo(item.getRut())
                                                        .build();

                                                Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                call.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            ResponseBody responsebody = response.body();

                                                            try {
                                                                JSONObject jObjError = new JSONObject(responsebody.string());
                                                                Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                Match.this.startActivity(intentAvatar);
                                                                finish();
                                                            } catch (Exception e) {
                                                                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
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
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }
        });

        botonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingreso = ingresarRutMail.getText().toString().toLowerCase().trim();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.BASE_URL))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final ApiServices services = retrofit.create(ApiServices.class);

                Call<ResponseMatch> call = services.buscarUsuario(tokenHeader, ingreso);
                call.enqueue(new Callback<ResponseMatch>() {
                    @Override
                    public void onResponse(Call<ResponseMatch> call, Response<ResponseMatch> response) {
                        if (response.isSuccessful()) {
                            final ResponseMatch usuario = response.body();

                            new SweetAlertDialog(Match.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("¿Estás seguro?")
                                    .setContentText("Hacer un match con: " + usuario.getNombre() + " " + usuario.getApellido() + ", ¿Como oponente?")
                                    .setCancelText("No, Cancelalo!!")
                                    .setConfirmText("Si, Hacer Match!!")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            new SweetAlertDialog(Match.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("¿Quien Ganó?")
                                                    .setContentText("¿Quien fue el ganador del encuentro?")
                                                    .setCancelText("EL :c")
                                                    .setConfirmText("YO!!")
                                                    .setNeutralText("EMPATE!!")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();

                                                            Retrofit retrofit = new Retrofit.Builder()
                                                                    .baseUrl(getString(R.string.BASE_URL))
                                                                    .addConverterFactory(GsonConverterFactory.create())
                                                                    .build();
                                                            final ApiServices services = retrofit.create(ApiServices.class);

                                                            //validar si existe una sesion
                                                            Context context = getApplicationContext();
                                                            SharedPreferences sharedPref = context.getSharedPreferences(
                                                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                                                            String rutMio = sharedPref.getString("rut", "Error");

                                                            if (rutMio.equals("Error")) {
                                                                Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_LONG).show();
                                                                finish();
                                                            }

                                                            RequestMatch requestMatch = RequestMatch.builder()
                                                                    .rutGanador(rutMio)
                                                                    .rutSegundo(usuario.getRut())
                                                                    .build();

                                                            Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                            call.enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                    if (response.isSuccessful()) {
                                                                        ResponseBody responsebody = response.body();

                                                                        try {
                                                                            JSONObject jObjError = new JSONObject(responsebody.string());
                                                                            Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                            Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                            Match.this.startActivity(intentAvatar);
                                                                            finish();
                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
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
                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();

                                                            Retrofit retrofit = new Retrofit.Builder()
                                                                    .baseUrl(getString(R.string.BASE_URL))
                                                                    .addConverterFactory(GsonConverterFactory.create())
                                                                    .build();
                                                            final ApiServices services = retrofit.create(ApiServices.class);

                                                            RequestMatch requestMatch = RequestMatch.builder()
                                                                    .rutGanador(usuario.getRut())
                                                                    .rutSegundo(usuario.getRut())
                                                                    .build();

                                                            Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                            call.enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                    if (response.isSuccessful()) {
                                                                        ResponseBody responsebody = response.body();

                                                                        try {
                                                                            JSONObject jObjError = new JSONObject(responsebody.string());
                                                                            Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                            Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                            Match.this.startActivity(intentAvatar);
                                                                            finish();
                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
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
                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();

                                                            Retrofit retrofit = new Retrofit.Builder()
                                                                    .baseUrl(getString(R.string.BASE_URL))
                                                                    .addConverterFactory(GsonConverterFactory.create())
                                                                    .build();
                                                            final ApiServices services = retrofit.create(ApiServices.class);

                                                            RequestMatch requestMatch = RequestMatch.builder()
                                                                    .rutGanador(null)
                                                                    .rutSegundo(usuario.getRut())
                                                                    .build();

                                                            Call<ResponseBody> call = services.hacerMatch(tokenHeader, requestMatch);
                                                            call.enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                    if (response.isSuccessful()) {
                                                                        ResponseBody responsebody = response.body();

                                                                        try {
                                                                            JSONObject jObjError = new JSONObject(responsebody.string());
                                                                            Toast.makeText(getApplicationContext(), "Match realizado espera confirmación", Toast.LENGTH_LONG).show();
                                                                            Intent intentAvatar = new Intent(Match.this, Profile.class);
                                                                            Match.this.startActivity(intentAvatar);
                                                                            finish();
                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
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
                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .show();
                                        }
                                    })
                                    .show();
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
                    public void onFailure(Call<ResponseMatch> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentReg = new Intent(Match.this, Profile.class);
        Match.this.startActivity(intentReg);
        finish();
    }
}
