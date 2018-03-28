package ucn.afta.cl.rankeducn.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.model.Usuario;

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

    @BindView(R.id.registrar)
    Button registrar;

    @BindView(R.id.password2)
    TextView password2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rut = rutView.getText().toString();
                String nombre = nombreView.getText().toString();
                String apellido = apellidoView.getText().toString();
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
                String password2 = password2View.getText().toString();

                if (Objects.equals(rut, "")) {
                    Toast.makeText(getApplicationContext(), "El RUT es requerido", Toast.LENGTH_LONG).show();
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
                if (Objects.equals(email, "")) {
                    Toast.makeText(getApplicationContext(), "El Email es requerido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Objects.equals(password, "")) {
                    Toast.makeText(getApplicationContext(), "La Password es requerido", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Objects.equals(password, password2)) {
                    Toast.makeText(getApplicationContext(), "Las Password no son iguales", Toast.LENGTH_LONG).show();
                    return;
                }

                final Usuario usuario = Usuario.builder()
                        .rut(rut)
                        .nombre(nombre)
                        .apellido(apellido)
                        .email(email)
                        .password(password)
                        .build();

                Toast.makeText(getApplicationContext(), usuario.getRut(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
