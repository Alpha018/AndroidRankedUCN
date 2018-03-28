package ucn.afta.cl.rankeducn.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ucn.afta.cl.rankeducn.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.registrar)
    TextView registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cuchillo con mantequilla !
        ButterKnife.bind(this);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(intentReg);
            }
        });
    }
}
