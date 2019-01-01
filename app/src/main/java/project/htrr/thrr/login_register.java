package project.htrr.thrr;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class login_register extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);



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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
