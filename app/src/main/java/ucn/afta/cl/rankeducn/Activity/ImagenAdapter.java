package ucn.afta.cl.rankeducn.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
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
import ucn.afta.cl.rankeducn.Utils.ResponseTop10;

/**
 * Creado por Tomás el 10/04/2018.
 */

public class ImagenAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<ResponseTop10> usuariosTop10;

    /**
     * Clase adapter que contiene los datos para el top10 de jugadores
     * @param activity
     * @param usuariosTop10
     */
    public ImagenAdapter(Activity activity, ArrayList<ResponseTop10> usuariosTop10) {
        this.activity = activity;
        this.usuariosTop10 = usuariosTop10;
    }

    @Override
    public int getCount() {
        return usuariosTop10.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            row = inf.inflate(R.layout.contenedor, null);

        }

        String nombreArchivo = usuariosTop10.get(position).getAvatar();
        String nombre = usuariosTop10.get(position).getNombre() + " " + usuariosTop10.get(position).getApellido();
        String email = usuariosTop10.get(position).getEmail();
        int puntos = usuariosTop10.get(position).getPuntaje();

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
