package project.htrr.thrr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView language=(TextView)findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLang=new Intent(getApplicationContext(),settings_language.class);
                startActivity(goToLang);
            }
        });

        TextView pInfo=(TextView)findViewById(R.id.pInfo);
        pInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goTopInfo=new Intent(getApplicationContext(),PersonalInfo.class);
                startActivity(goTopInfo);
            }
        });
    }
}
