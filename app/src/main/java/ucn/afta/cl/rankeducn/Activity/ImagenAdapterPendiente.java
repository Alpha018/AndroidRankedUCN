package ucn.afta.cl.rankeducn.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.CircularTransform;
import ucn.afta.cl.rankeducn.Utils.ResponsePendientes;
import ucn.afta.cl.rankeducn.Utils.ResponseTop10;

/**
 * Creado por Tomás el 10/04/2018.
 */

public class ImagenAdapterPendiente extends BaseAdapter{

    private Activity activity;
    private ArrayList<ResponsePendientes> Pendientes;

    /**
     * Clase adapter que contiene los datos para el top10 de jugadores
     * @param activity
     * @param pendientes
     */
    public ImagenAdapterPendiente(Activity activity, ArrayList<ResponsePendientes> pendientes) {
        this.activity = activity;
        this.Pendientes = pendientes;
    }

    @Override
    public int getCount() {
        return Pendientes.size();
    }

    @Override
    public Object getItem(int position) {
        return Pendientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(convertView == null){
            LayoutInflater inf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inf.inflate(R.layout.contenedorpendiente, null);

        }

        String nombreArchivo = Pendientes.get(position).getUsuarioPrincipal().getAvatar();
        String nombre = Pendientes.get(position).getUsuarioPrincipal().getNombre() + " " + Pendientes.get(position).getUsuarioPrincipal().getApellido();
        String email = Pendientes.get(position).getUsuarioPrincipal().getEmail();
        String ganador;

        if (Pendientes.get(position).getGanador() == null) {
            ganador = "EMPATE";
        } else {
            ganador = Pendientes.get(position).getGanador().getNombre() + " " + Pendientes.get(position).getGanador().getApellido();
        }


        ImageView perfil = (ImageView)row.findViewById(R.id.perfil);
        TextView ganadorText = (TextView)row.findViewById(R.id.ganador);
        TextView nombreText = (TextView)row.findViewById(R.id.nombre);
        TextView correoText = (TextView)row.findViewById(R.id.email);
        TextView fecha = (TextView)row.findViewById(R.id.fecha);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String DateToStr = format.format(Pendientes.get(position).getFecha());

        Picasso.get()
                .load(activity.getString(R.string.BASE_URL) + "/public/img/" + nombreArchivo)
                .centerCrop()
                .fit()
                .transform(new CircularTransform())
                .into(perfil);

        fecha.setText("Fecha: " + DateToStr);
        ganadorText.setText("Ganador: " + ganador);
        nombreText.setText(nombre);
        correoText.setText(email);

        //validar si existe una sesion
        Context context = this.activity;
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String rut = sharedPref.getString("rut", "Error");
        if (Pendientes.get(position).getGanador() != null) {
            if (rut.equals(Pendientes.get(position).getGanador().getRut())) {
                ganadorText.setTextColor(Color.GREEN);
            } else {
                ganadorText.setTextColor(Color.RED);
            }
        } else {
            ganadorText.setTextColor(Color.WHITE);
        }


        return row;
    }
}
