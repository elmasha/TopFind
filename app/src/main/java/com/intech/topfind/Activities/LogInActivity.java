package com.intech.topfind.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.intech.topfind.MainActivity;
import com.intech.topfind.R;

import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {
private TextView toRegister,toMain;

    FirebaseAuth mAuth;
    private String email,password;
    private TextInputLayout InputEmail,InputPassword;
    private Button LoginBtn;

//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    CollectionReference TopFindRef = db.collection("TopFind_Clients");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        InputEmail = findViewById(R.id.email_login);
        InputPassword = findViewById(R.id.password_login);
        LoginBtn = findViewById(R.id.Btn_login);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()){

                }else {
                LoginUser();
                }
            }
        });




        toMain = findViewById(R.id.BackToMain2);
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });
        toRegister = findViewById(R.id.ToRegister);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), RegisterActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });
    }


    private ProgressDialog progressDialog;
    private void LoginUser() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        password = InputPassword.getEditText().getText().toString();
        email = InputEmail.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            UpdateDeviceToken(mAuth.getCurrentUser().getUid());
                        }else {
                            ToastBack(task.getException().getMessage());
                        }
                    }
                });

    }


    private void UpdateDeviceToken(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference TopFindRef = db.collection("TopFind_Clients");

        String token_Id = FirebaseInstanceId.getInstance().getToken();

        HashMap<String,Object> updates = new HashMap<>();
        updates.put("device_token",token_Id);
        TopFindRef.document(uid).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ToastBack("Authentication Succeeded ");
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainViewActivity.class));

                }else {

                    ToastBack(task.getException().getMessage());
                    progressDialog.dismiss();

                }
            }
        });


    }


    private boolean validation(){
        password = InputPassword.getEditText().getText().toString();
        email = InputEmail.getEditText().getText().toString().trim();

        if (email.isEmpty()){
            InputEmail.setError("Provide your Email");
            return false;
        }else if (password.isEmpty()){
            InputPassword.setError("Provide a Password");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            InputEmail.setError("Please enter a Valid email");
            return false;
        }
        else{

            return true;
        }

    }

    private Toast backToast;
    private void ToastBack(String message){


        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        View view = backToast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#062D6E"), PorterDuff.Mode.SRC_IN);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#2BB66A"));
        backToast.show();
    }
}