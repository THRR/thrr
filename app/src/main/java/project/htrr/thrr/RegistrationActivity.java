package project.htrr.thrr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView Fname;
    private TextView Lname;
    private TextView email;
    private  TextView psw;
    private  TextView rePsw;

    //define the firebase object
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance(); //initialize our firebase object

        Fname=(TextView)findViewById(R.id.rgstrName);
        Lname=(TextView)findViewById(R.id.rgstLastName);
        email=(TextView)findViewById(R.id.rgstEmail);
        psw=(TextView)findViewById(R.id.rgstPsw);
        rePsw=(TextView)findViewById(R.id.rgstRePsw);

        Button rgstBtn=(Button) findViewById(R.id.rgstBtn);

        rgstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Registration in progress");
                progressDialog.show();
                RegisterMe();

            }
        });




    }


    private void RegisterMe(){
        if(CheckPassword()){
            Toast.makeText(this,"Password do not match",Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),psw.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registration complete",Toast.LENGTH_SHORT).show();
                                SaveUserInfo();
                                progressDialog.hide();
                                Intent toHomePage=new Intent(getApplicationContext(),Homepage.class);
                                toHomePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //so when we press the back button it wont open the login again
                                startActivity(toHomePage);
                                finish();
                            }
                            else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), "User already exist please use another email", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            }
                        }
                    });
        }

    }

    private void SaveUserInfo(){
        user = firebaseAuth.getCurrentUser();
        //database=FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.child(user.getUid()).push().setValue(Fname.getText().toString() + Lname.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Value was set. ", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

                               Toast.makeText(getApplicationContext(), "User profile updated.",Toast.LENGTH_SHORT).show();


//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(Fname.getText().toString()+Lname.getText().toString())
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "User profile updated.",Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private boolean CheckPassword(){
        if(psw.equals(rePsw))
            return true;
        else
            return false;
    }

}
