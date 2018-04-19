package ucn.afta.cl.rankeducn.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.CircularTransform;
import ucn.afta.cl.rankeducn.Utils.ResponseMatch;
import ucn.afta.cl.rankeducn.Utils.ResponseTop10;

/**
 * Creado por Tomás el 10/04/2018.
 */

public class ImagenAdapterMatch extends BaseAdapter{

    private Activity activity;
    private ArrayList<ResponseMatch> usuariosMatch;

    /**
     * Clase adapter que contiene los datos para el top10 de jugadores
     * @param activity
     * @param usuariosMatch
     */
    public ImagenAdapterMatch(Activity activity, ArrayList<ResponseMatch> usuariosMatch) {
        this.activity = activity;
        this.usuariosMatch = usuariosMatch;
    }

    @Override
    public int getCount() {
        return usuariosMatch.size();
    }

    @Override
    public Object getItem(int position) {
        return usuariosMatch.get(position);
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
            row = inf.inflate(R.layout.contenedormatch, null);

        }

        String nombreArchivo = usuariosMatch.get(position).getAvatar();
        String nombre = usuariosMatch.get(position).getNombre() + " " + usuariosMatch.get(position).getApellido();
        String email = usuariosMatch.get(position).getEmail();
        int puntos = usuariosMatch.get(position).getPuntaje();

        ImageView perfil = (ImageView)row.findViewById(R.id.perfil);
        TextView puntosText = (TextView)row.findViewById(R.id.puntos);
        TextView nombreText = (TextView)row.findViewById(R.id.nombre);
        TextView correoText = (TextView)row.findViewById(R.id.email);

        Picasso.get()
                .load(activity.getString(R.string.BASE_URL) + "/public/img/" + nombreArchivo)
                .centerCrop()
                .fit()
                .transform(new CircularTransform())
                .into(perfil);

        puntosText.setText("Pts: " + puntos);
        nombreText.setText(nombre);
        correoText.setText(email);

        return row;
    }
}
