package com.example.app3_async;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int backCounter = 0;
    private int asyncCounter = 0;
    private Handler handler;
    private TextView textMain;
    private TextView textCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        textMain = findViewById(R.id.textMain);
        textCounter = findViewById(R.id.textCounter);
        textMain.setText(getResources().getString(R.string.onBack, backCounter++));

        Runnable counterRunner = new Runnable() {
            @Override
            public void run() {
                textCounter.setText(getResources().getString(R.string.counter, asyncCounter++));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(counterRunner);

        Button button = findViewById(R.id.buttonExit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        textMain = findViewById(R.id.textMain);
        textMain.setText(getResources().getString(R.string.onBack, backCounter++));
        // moveTaskToBack(true);
    }
}