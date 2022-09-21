package com.example.criminalalertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT ,attack,theft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Email = (TextView)findViewById(R.id.txtem);
        attack=(Button)findViewById(R.id.btnattack);
        theft=(Button)findViewById(R.id.btntheft);
        LogOUT = (Button)findViewById(R.id.btnlogout);
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

                Toast.makeText(Dashboard.this,"Log Out Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);

            }
        });
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, location.class);
                startActivity(intent);
            }
        });
        theft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, location.class);
                startActivity(intent);
            }
        });

    }
}