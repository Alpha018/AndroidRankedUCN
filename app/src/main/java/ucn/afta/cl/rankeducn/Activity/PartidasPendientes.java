package ucn.afta.cl.rankeducn.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

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
import ucn.afta.cl.rankeducn.Utils.ResponsePendientes;

public class PartidasPendientes extends AppCompatActivity {

    @BindView(R.id.cardView)
    CardView volver;

    @BindView(R.id.lista)
    ListView listView;

    SweetAlertDialog sweetAlertDialog;

    ResponsePendientes itemFinal;

    String tokenHeader;

    ArrayList<ResponsePendientes> usuariosFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_pendientes);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        //Poner el list view
        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "NOTOKEN");

        if (token.equals("NOTOKEN")) {
            Intent intentReg = new Intent(PartidasPendientes.this, MainActivity.class);
            PartidasPendientes.this.startActivity(intentReg);
            finish();
        }
        tokenHeader = token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiServices services = retrofit.create(ApiServices.class);

        Call<ArrayList<ResponsePendientes>> call = services.getPartidasPendientes(token);
        call.enqueue(new Callback<ArrayList<ResponsePendientes>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponsePendientes>> call, Response<ArrayList<ResponsePendientes>> response) {
                if (response.isSuccessful()) {
                    usuariosFinal = response.body();
                    ImagenAdapterPendiente adapter = new ImagenAdapterPendiente(PartidasPendientes.this, usuariosFinal);
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
            public void onFailure(Call<ArrayList<ResponsePendientes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });


        //Confirmar la partida haciendo click en el listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ResponsePendientes item = (ResponsePendientes) listView.getItemAtPosition(position);
                //hacer confirmacion con sweet alert
                itemFinal = item;
                if (item.getGanador() == null) {
                    new SweetAlertDialog(PartidasPendientes.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Estás seguro?")
                            .setContentText("Dejarás la partida empate")
                            .setCancelText("No!!")
                            .setConfirmText("Si!!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Context context = getApplicationContext();
                                    SharedPreferences sharedPref = context.getSharedPreferences(
                                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                    String token = sharedPref.getString("token", "NOTOKEN");

                                    if (token.equals("NOTOKEN")) {
                                        Intent intentReg = new Intent(PartidasPendientes.this, MainActivity.class);
                                        PartidasPendientes.this.startActivity(intentReg);
                                        finish();
                                    }

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(getString(R.string.BASE_URL))
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    final ApiServices services = retrofit.create(ApiServices.class);

                                    Call<ResponseBody> call = services.acceptarMatch(token, itemFinal);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "La partida fue confirmada exitosamente!!", Toast.LENGTH_LONG).show();
                                                // ACTUALIZAMOS EL LIST VIEW
                                                if (usuariosFinal.remove(item)) {
                                                    ImagenAdapterPendiente adapter = new ImagenAdapterPendiente(PartidasPendientes.this, usuariosFinal);
                                                    listView.setAdapter(adapter);
                                                }
                                                if (usuariosFinal.size() == 0) {
                                                    Toast.makeText(getApplicationContext(), "No hay más partidas pendientes", Toast.LENGTH_LONG).show();
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
                } else {
                    new SweetAlertDialog(PartidasPendientes.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Estás seguro?")
                            .setContentText("Dejarás a: " + item.getGanador().getNombre() + " " + item.getGanador().getApellido() + " Como ganador")
                            .setCancelText("No, El no ganó!!")
                            .setConfirmText("Si, El ganó!!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Context context = getApplicationContext();
                                    SharedPreferences sharedPref = context.getSharedPreferences(
                                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                    String token = sharedPref.getString("token", "NOTOKEN");

                                    if (token.equals("NOTOKEN")) {
                                        Intent intentReg = new Intent(PartidasPendientes.this, MainActivity.class);
                                        PartidasPendientes.this.startActivity(intentReg);
                                        finish();
                                    }

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(getString(R.string.BASE_URL))
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    final ApiServices services = retrofit.create(ApiServices.class);

                                    Call<ResponseBody> call = services.acceptarMatch(token, itemFinal);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "La partida fue confirmada exitosamente!!", Toast.LENGTH_LONG).show();
                                                // ACTUALIZAMOS EL LIST VIEW
                                                if (usuariosFinal.remove(item)) {
                                                    ImagenAdapterPendiente adapter = new ImagenAdapterPendiente(PartidasPendientes.this, usuariosFinal);
                                                    listView.setAdapter(adapter);
                                                }
                                                if (usuariosFinal.size() == 0) {
                                                    Toast.makeText(getApplicationContext(), "No hay más partidas pendientes", Toast.LENGTH_LONG).show();
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
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(PartidasPendientes.this, Profile.class);
                PartidasPendientes.this.startActivity(intentReg);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentReg = new Intent(PartidasPendientes.this, Profile.class);
        PartidasPendientes.this.startActivity(intentReg);
        finish();
    }
}
