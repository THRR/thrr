package project.htrr.thrr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class settings_language extends AppCompatActivity {


    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton enButton;
    RadioButton frButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);

        //assign each radio button to the english and frend radio

        enButton=findViewById(R.id.lang_english);
        frButton=findViewById(R.id.lang_french);
        //call the function to load everything at the create
        LoadLang();

        Button saveChanges=(Button)findViewById(R.id.lang_SaveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_register.changeLang=true;
                radioGroup=findViewById(R.id.lang_radioGroup);
                int radioId=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(radioId);
                RememberMe((String) radioButton.getText());
              //  Toast.makeText(getApplicationContext(),"selection: "+ radioButton.getText(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


    public void LoadLang(){

        radioGroup=findViewById(R.id.lang_radioGroup);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang=sharedPreferences.getString("language","English");
        if(lang.equals("French")){
            radioGroup.check(R.id.lang_french);
        }
        else{
//            Log.d("we are in english", "frrr");
            radioGroup.check(R.id.lang_english);
        }
    }

    public void RememberMe(String language){
        Log.d("language", language);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(settings_language.this.getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("language",language);
        editor.apply();
    }
}
