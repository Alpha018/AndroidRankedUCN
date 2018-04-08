package ucn.afta.cl.rankeducn.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.CircularTransform;

public class Profile extends AppCompatActivity {

    @BindView(R.id.perfil)
    ImageView perfil;

    @BindView(R.id.nombre)
    TextView nombre;

    @BindView(R.id.rut)
    TextView rut;

    @BindView(R.id.jugadas)
    TextView jugadas;

    @BindView(R.id.ganadas)
    TextView ganadas;

    @BindView(R.id.perdidas)
    TextView perdidas;

    @BindView(R.id.puntaje)
    TextView puntaje;

    @BindView(R.id.top)
    CardView top10;

    @BindView(R.id.match)
    CardView match;

    @BindView(R.id.logout)
    CardView logout;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.changeAvatar)
    CardView cambiarAvatar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        //validar si existe una sesion
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        nombre.setText(sharedPref.getString("nombre", "Error") + " " + sharedPref.getString("apellido", "Error"));
        rut.setText(sharedPref.getString("rut", "Error"));
        jugadas.setText(sharedPref.getInt("partidasJugadas", 0) + "");
        ganadas.setText(sharedPref.getInt("partidasGanadas", 0)+ "");
        perdidas.setText(sharedPref.getInt("partidasPerdidas", 0)+ "");
        puntaje.setText(sharedPref.getInt("puntaje", 0) + "");
        email.setText(sharedPref.getString("email", "Error"));

        Picasso.get()
                .load(getString(R.string.BASE_URL) + "/public/img/" + sharedPref.getString("avatar", "default.png"))
                .centerCrop()
                .fit()
                .transform(new CircularTransform())
                .into(perfil);

        top10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();

                Intent intentReg = new Intent(Profile.this, MainActivity.class);
                Profile.this.startActivity(intentReg);
                finish();
            }
        });

        cambiarAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAvatar = new Intent(Profile.this, UploadImage.class);
                Profile.this.startActivity(intentAvatar);
                finish();
            }
        });
    }


}
