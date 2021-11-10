package com.intech.topfind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.intech.topfind.Activities.LogInActivity;
import com.intech.topfind.Activities.RegisterActivity;

public class MainActivity extends AppCompatActivity {
private Button GetStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetStarted = findViewById(R.id.Get_started);

        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            }
        });
    }
}