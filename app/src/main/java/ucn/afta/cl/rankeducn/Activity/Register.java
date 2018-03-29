package ucn.afta.cl.rankeducn.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.ApiServices;
import ucn.afta.cl.rankeducn.Utils.DeviceUtils;
import ucn.afta.cl.rankeducn.Utils.RequestRegister;
import ucn.afta.cl.rankeducn.Utils.ResponseRegister;


public class Register extends AppCompatActivity {

    @BindView(R.id.rut)
    TextView rutView;

    @BindView(R.id.nombre)
    TextView nombreView;

    @BindView(R.id.apellido)
    TextView apellidoView;

    @BindView(R.id.email)
    TextView emailView;

    @BindView(R.id.password)
    TextView passwordView;

    @BindView(R.id.password2)
    TextView password2View;

    @BindView(R.id.cardView)
    CardView registrar;

    @BindView(R.id.volver)
    TextView volver;

    public final String BASE_URL = "http://192.168.1.148:8000";
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        final String deviceID = DeviceUtils.getDeviceId(getApplicationContext());

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rut = rutView.getText().toString().trim();
                String nombre = nombreView.getText().toString().trim();
                String apellido = apellidoView.getText().toString().trim();
                String email = emailView.getText().toString().trim();
                String password = passwordView.getText().toString().trim();
                String password2 = password2View.getText().toString().trim();

                if (Objects.equals(rut, "") || !rut.matches("[0-9]+[-|‐]?[0-9kK]")) {
                    Toast.makeText(getApplicationContext(), "El RUT es requerido o no es valido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Objects.equals(nombre, "")) {
                    Toast.makeText(getApplicationContext(), "El Nombre es requerido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Objects.equals(apellido, "")) {
                    Toast.makeText(getApplicationContext(), "El Apellido es requerido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Objects.equals(email, "") || !email.matches("[a-zA-Z0-9.!#$%&’*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*")) {
                    Toast.makeText(getApplicationContext(), "El Email es requerido o no es valido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Objects.equals(password, "")) {
                    Toast.makeText(getApplicationContext(), "La Password es requerido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 5) {
                    Toast.makeText(getApplicationContext(), "La Password debe tener 5 caracteres o más", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Objects.equals(password, password2)) {
                    Toast.makeText(getApplicationContext(), "Las Password no son iguales", Toast.LENGTH_LONG).show();
                    return;
                }

                final RequestRegister usuario = RequestRegister.builder()
                        .rut(rut)
                        .nombre(nombre)
                        .apellido(apellido)
                        .email(email)
                        .password(password)
                        .deviceID(deviceID)
                        .build();

                //desde aqui se usa el retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final ApiServices service = retrofit.create(ApiServices.class);

                Call<ResponseRegister> call = service.request(usuario);
                call.enqueue(new Callback<ResponseRegister>() {
                    @Override
                    public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {

                        if (response.isSuccessful()) {

                            ResponseRegister tokenResponse = response.body();
                            Toast.makeText(getApplicationContext(), tokenResponse.getId(), Toast.LENGTH_LONG).show();
                            finish();

                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());

                                if (jObjError.getString("message").contains("duplicate")) {
                                    String[] errores = jObjError.getString("message").split(":");
                                    sweetAlertDialog = new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.setTitleText(jObjError.getString("desc"));
                                    sweetAlertDialog.setContentText("El Dato: " + errores[2].split("_")[0].toUpperCase() + " Ya existe '" + errores[4].split("\"")[1] + "'");
                                    sweetAlertDialog.show();
                                }

                                Toast.makeText(getApplicationContext(), jObjError.getString("desc"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseRegister> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error al enviar la petición", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
