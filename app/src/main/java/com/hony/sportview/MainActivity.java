package com.hony.sportview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hony.sportview.view.SportView;

public class MainActivity extends AppCompatActivity {

    private SportView sportView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sportView = (SportView) findViewById(R.id.sport);
        try {
            sportView.setProgress(3808);
            sportView.setStartAngle(-235);
            sportView.setEndAngle(55);
            sportView.setAvg(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sportView.startAnimation();
        sportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportView.startAnimation();
            }
        });
    }
}
