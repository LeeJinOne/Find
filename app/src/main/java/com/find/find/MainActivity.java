package com.find.find;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentAlram, new Alram());
        fragmentTransaction.commit();


        context = this;

        ImageButton mvToMap = (ImageButton)findViewById(R.id.mvToMap);
        Button serviceCenterBtn = (Button)findViewById(R.id.serviceCenterBtn);

        mvToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapsActivity.class));
            }
        });

        serviceCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ServiceCenterActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        Log.e("A", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e("A", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("A", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e("A", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("A", "onDestroy");
        super.onDestroy();
    }
}
