package ucn.afta.cl.rankeducn.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ucn.afta.cl.rankeducn.Activity.MainActivity;
import ucn.afta.cl.rankeducn.Activity.Profile;
import ucn.afta.cl.rankeducn.R;

/**
 * Creado por black el 13/04/2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Recivimos el mensaje desde firebase

        // comprobar si el mensaje contiene datos
        if (remoteMessage.getData().size() > 0) {
            // comprobado que el mensaje tiene algun dato
        }

        // comprobar si el mensaje contiene una notificacion
        if (remoteMessage.getNotification() != null) {
            // ejecutar
            SendNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getNotification().getTitle().equals("match")) { // si llega una victoria empate o derrota actualizamos los marcadores
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

            Call<ResponseActualizar> call = service.actualizarPuntajes(token);
            call.enqueue(new Callback<ResponseActualizar>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ResponseActualizar> call, Response<ResponseActualizar> response) {
                    if (response.isSuccessful()) {
                        ResponseActualizar responseActualizar = response.body();
                        salvarDatos(responseActualizar);
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
                public void onFailure(Call<ResponseActualizar> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Fallo al conectarse con el servidor", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void SendNotification(String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
        // sonido de la notificacion
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ranked UCN")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sonido)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0/*ID de la notificacion*/, notifiBuilder.build());

    }

    private void salvarDatos(final ResponseActualizar usuario) {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("puntaje", usuario.getPuntaje());
        editor.putInt("partidasJugadas", usuario.getPartidasJugadas());
        editor.putInt("partidasGanadas", usuario.getPartidasGanadas());
        editor.putInt("partidasPerdidas", usuario.getPartidasPerdidas());
        editor.apply();
    }
}
