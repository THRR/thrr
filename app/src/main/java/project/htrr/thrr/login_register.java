package project.htrr.thrr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class login_register extends AppCompatActivity {


    public static  boolean changeLang=false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(changeLang==true) {
           LoadLang();
           this.recreate();
            changeLang=false;
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadLang();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        if(isNetworkAvailable()){
            Log.d("status","available");
            Toast.makeText(this,"Network is available",Toast.LENGTH_SHORT);
        }else{
            Log.d("status","available");
            Toast.makeText(this,"Network is available",Toast.LENGTH_SHORT);
        }


        Button lrSignInBtn=(Button) findViewById(R.id.lrSignInBtn);
        lrSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignIn=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goToSignIn);
            }
        });

        Button lrSignUpBtn=(Button) findViewById(R.id.lrSignUpBtn);
        lrSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUn=new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(goToSignUn);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.exit){
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            finish();
        }
        if(id==R.id.language_sett){
            Intent goToLangSettings=new Intent(getApplicationContext(),settings_language.class);
            startActivity(goToLangSettings);
        }
        return super.onOptionsItemSelected(item);
    }


    public void LoadLang(){
        String lang_code;
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang=sharedPreferences.getString("language","English");
        if(lang.equals("French")){
            lang_code="fr";
        }else{
            lang_code="en";
        }

        Resources res= getApplicationContext().getResources();
        android.content.res.Configuration configuration=res.getConfiguration();
        configuration.setLocale(new Locale(lang_code));
        res.updateConfiguration(configuration,res.getDisplayMetrics());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
