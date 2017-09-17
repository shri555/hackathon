package com.example.shrikrishna.coep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Info extends AppCompatActivity {
    Spinner spinclg,spintime;
    Button save,add;
    List<String> clg,time;
    CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        spinclg=(Spinner)findViewById(R.id.spinclg);
        spintime=(Spinner)findViewById(R.id.spintime);
        save=(Button)findViewById(R.id.saveinfo);
        cb=(CheckBox)findViewById(R.id.checkvehi);
        clg=new ArrayList<String>();
        clg.add("COEP");
        clg.add("Sinhagad");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clg);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinclg.setAdapter(dataAdapter);
        time=new ArrayList<String>();
        time.add("8 AM");
        time.add("9 AM");
        time.add("10 AM");
        time.add("11 AM");
        time.add("12 PM");
        time.add("1 PM");
        time.add("2 PM");
        time.add("3 PM");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spintime.setAdapter(dataAdapter2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.','_');
                FirebaseDatabase.getInstance().getReference().child(s).child("info").push().setValue(new Userinfo(s,spinclg.getSelectedItem().toString(), spintime.getSelectedItem().toString(),cb.isChecked()));
                SharedPreferences prefs = getSharedPreferences("COEP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("infosaved", "true");
                editor.putBoolean("vehicle",cb.isChecked());
                editor.putString("time",spintime.getSelectedItem().toString());
                editor.putString("clg",spinclg.getSelectedItem().toString());
                editor.commit(); //important, otherwise it wouldn't save.
                startActivity(new Intent(Info.this,MainActivity.class));
                finish();
            }
        });
    }
}
