package project.htrr.thrr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class settings_language extends AppCompatActivity {


    RadioGroup radioGroup;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);


        Button saveChanges=(Button)findViewById(R.id.lang_SaveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioGroup=findViewById(R.id.lang_radioGroup);
                int radioId=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(radioId);
                Toast.makeText(getApplicationContext(),"selection: "+ radioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
