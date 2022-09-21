package com.example.criminalalertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT ,attack,theft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Email = (TextView)findViewById(R.id.txtem);
        LogOUT = (Button)findViewById(R.id.btnlogout);
        attack=(Button)findViewById(R.id.btnattack);
        theft=(Button)findViewById(R.id.btntheft);
        Intent intent = getIntent();

        // Receiving User Email Send By MainActivity.
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);
        // Setting up received email to TextView.
        Email.setText(Email.getText().toString()+ EmailHolder);
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Finishing current DashBoard activity on button click.
                finish();

                Toast.makeText(Profile.this,"Log Out Successful", Toast.LENGTH_LONG).show();

            }
        });
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, location.class);
                startActivity(intent);
            }
        });
        theft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, location.class);
                startActivity(intent);
            }
        });

    }
}