package project.htrr.thrr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PersonalInfo extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private Context context;
    TextView newPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        firebaseAuth=FirebaseAuth.getInstance(); //initialize our firebase object
        user = firebaseAuth.getCurrentUser();
        String email= user.getEmail();
        newPsw=findViewById(R.id.newPsw);
        Button save_changes=(Button)findViewById(R.id.pInfo_save);
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }
   private void changePassword(){

        user.updatePassword(newPsw.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Password has been reset",Toast.LENGTH_LONG).show();
                            newPsw.setText("");
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Password change unsuccessful",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
