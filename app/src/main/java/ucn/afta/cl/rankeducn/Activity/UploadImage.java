package ucn.afta.cl.rankeducn.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import ucn.afta.cl.rankeducn.R;
import ucn.afta.cl.rankeducn.Utils.ApiServices;
import ucn.afta.cl.rankeducn.Utils.CircularTransform;

public class UploadImage extends AppCompatActivity {

    @BindView(R.id.volver)
    TextView botonVolver;

    @BindView(R.id.cardView)
    CardView botonSubir;

    @BindView(R.id.fileName)
    TextView nombreArchivo;

    @BindView(R.id.perfilPrev)
    ImageView imegenPreview;

    @BindView(R.id.textPrev)
    TextView textPreview;

    @BindView(R.id.cardView2)
    CardView sendPhoto;

    ProgressDialog progress;

    File file_send;

    String content_type_send;

    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        uploadButton();

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(UploadImage.this, Profile.class);
                UploadImage.this.startActivity(intentProfile);
                finish();
            }
        });

        sendPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validar si existe una sesion
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                String token = sharedPref.getString("token", "Error");

                if (token.equals("Error")) {
                    Toast.makeText(getApplicationContext(), "No existe el token, reinicie la sesión", Toast.LENGTH_LONG).show();
                    return;
                }

                progress = new ProgressDialog(UploadImage.this);
                progress.setTitle("Uploading");
                progress.setMessage("Please wait...");
                progress.show();

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.BASE_URL))
                        .client(client)
                        .build();
                final ApiServices service = retrofit.create(ApiServices.class);

                RequestBody reqFile = RequestBody.create(MediaType.parse(content_type_send), file_send);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file_send.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

                retrofit2.Call<ResponseBody> req = service.postAvatar(token, body, name);
                req.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        progress.dismiss();
                        if (response.isSuccessful()) {

                            ResponseBody responsebody = response.body();

                            try {
                                JSONObject jObjError = new JSONObject(responsebody.string());

                                Context context = getApplicationContext();
                                SharedPreferences sharedPref = context.getSharedPreferences(
                                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("avatar", jObjError.getString("avatar"));
                                editor.apply();

                                Intent regreso = new Intent(UploadImage.this, Profile.class);
                                UploadImage.this.startActivity(regreso);
                                finish();

                            } catch (Exception e) {
                                sweetAlertDialog = new SweetAlertDialog(UploadImage.this, SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Error");
                                sweetAlertDialog.setContentText(e.getMessage());
                                sweetAlertDialog.show();
                            }

                            Toast.makeText(getApplicationContext(), "Registrado Correctamente", Toast.LENGTH_LONG).show();

                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());

                                sweetAlertDialog = new SweetAlertDialog(UploadImage.this, SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Error en el servidor");
                                sweetAlertDialog.setContentText("Error: " + jObjError.getString("desc"));
                                sweetAlertDialog.show();

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error al enviar la petición", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void uploadButton() {
        botonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(UploadImage.this)
                        .withRequestCode(10)
                        .start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            uploadButton();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    file_send = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    content_type_send = getMimeType(file_send.getPath());

                    String file_path = file_send.getAbsolutePath();
                    String file_name = file_send.getName();
                    if (!content_type_send.equals("image/jpeg") || !content_type_send.equals("image/jpg") || !content_type_send.equals("image/png") || content_type_send == null) {
                        sweetAlertDialog = new SweetAlertDialog(UploadImage.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Error de formato");
                        sweetAlertDialog.setContentText("El archivo seleccionado no es una imagen valida");
                        sweetAlertDialog.show();
                    } else {
                        nombreArchivo.setText(file_name);
                        Toast.makeText(getApplicationContext(), file_path, Toast.LENGTH_LONG).show();

                        Picasso.get()
                                .load(file_send)
                                .centerCrop()
                                .fit()
                                .transform(new CircularTransform())
                                .into(imegenPreview);

                        textPreview.setText("Preview");
                        sendPhoto.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private String getMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }
}
