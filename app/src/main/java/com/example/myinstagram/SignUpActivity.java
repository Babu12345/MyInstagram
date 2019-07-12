package com.example.myinstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {


    private EditText usernameInput;
    private EditText emailInput;
    private  EditText passwordInput;
    private EditText  FirstInput;
    private EditText  LastInput;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        usernameInput = findViewById(R.id.su_username_et);
        passwordInput = findViewById(R.id.su_password_et);
        emailInput = findViewById(R.id.su_email_et);
        signupBtn = findViewById(R.id.su_signupbtn);
        FirstInput = findViewById(R.id.su_First_et);
        LastInput = findViewById(R.id.su_Last_et);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String su_username = usernameInput.getText().toString();
                final String su_password = passwordInput.getText().toString();
                final String su_first = FirstInput.getText().toString();
                final String su_last = LastInput.getText().toString();

                final String su_email = emailInput.getText().toString();

                signup(su_username,su_password,su_email, su_first, su_last);
                finish();
            }
        });

    }



    private void signup(String username, String password, String email, String first, String last){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("FirstName", first);
        user.put("LastName", last);

        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("LoginActivity", "SignUp Successful");
                    Toast.makeText(getApplicationContext(), "SignUp Successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("LoginActivity", "Signup Failure");
                    Toast.makeText(getApplicationContext(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
