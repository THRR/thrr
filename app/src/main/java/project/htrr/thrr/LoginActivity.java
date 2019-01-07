package project.htrr.thrr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    // UI references.
    public static boolean CanISaveDataOffline=false; //I am a flag for homepage activity
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox rememberMe;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;



    protected void onResume() {
        super.onResume();
        if(CanISaveDataOffline==true) {
            CanISaveDataOffline=false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CanISaveDataOffline=false;


        progressDialog = new ProgressDialog(this);

        // Set up the login form.
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();

        //so this is how we will proceed, if the use is not online we will check if we have any shared preference saved
        //if we do that means it is 100% sure that accounts exists and we can load it
        LoadLogin();


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attemptLogin()) {
                    progressDialog.show();
                    if (!isNetworkAvailable()) {
                        if (logInState()) {
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Proceesing in offline mode", Toast.LENGTH_LONG).show();
                            Intent toHomePage = new Intent(getApplicationContext(), Homepage.class);
                            startActivity(toHomePage);
                        } else {
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Impossible proceeding in offline mode, please try again later", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        //check if we can login or not to DB using firebase instance
                        firebaseAuth.signInWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.hide();
                                            //so as we know the login has been successfull we can save the login credential and use them to log in again
                                            if (rememberMe.isChecked()) {
                                                RememberMe();
                                            } else {
                                                ResetLogin();
                                            }
                                            CanISaveDataOffline=true;
                                            Intent toHomePage = new Intent(getApplicationContext(), Homepage.class);
                                            startActivity(toHomePage);
                                        } else {
                                            progressDialog.hide();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        //    finish();
                    }
                }
            }
        });

    }

    public void LoadLogin() {
        SharedPreferences savedInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String email = savedInfo.getString("email", "missing");
        String psw = savedInfo.getString("password", "missing");
        if (!(email.equals("missing")) && !(psw.equals("missing"))) {
            mEmailView.setText(email);
            mPasswordView.setText(psw);
            rememberMe.setChecked(true);
        }
    }

    //if we have email and password saved in shared preference we can login
    public boolean logInState() {
        SharedPreferences savedInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String email = savedInfo.getString("email", "missing");
        String psw = savedInfo.getString("password", "missing");
        if ((email.equals("missing")) || (psw.equals("missing"))) {
            return false;
        } else if(mEmailView.getText().toString().equals(email) && mPasswordView.getText().toString().equals(psw) ){
            return true;
        }else return true;
    }


    public void RememberMe() {

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", mEmailView.getText().toString());
        editor.putString("password", mPasswordView.getText().toString());
        editor.apply();

    }

    public void ResetLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", "missing");
        editor.putString("password", "missing");
        editor.apply();
    }

    /**
     * Callback received when a permissions request has been completed.
     */


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }/* else if (!TextUtils.isEmpty(password) && !isPasswordAlphaNumeric(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password_alphaNumeric));
            focusView = mPasswordView;
            cancel = true;
        }*/

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        //here will go the code for the intent and sign in as here is where we login ----------->

        return cancel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if (password.length() > 7) return false;
        else return true;//for now set it to 7 as we set the requirment as 8
    }

  /* //develop to check the number but need to fix it
    private boolean isPasswordAlphaNumeric(String password) {
        //TODO: Replace this with your own logic
            return Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9])",password); //regex to check if there is at least one letter and one number
    }
*/
    /**
     * Shows the progress UI and hides the login form.
     */


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

