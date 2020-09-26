package com.example.app2_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inp1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
                String inp2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
                ((TextView) findViewById(R.id.textView)).setText(concatString(inp1, inp2));
            }
        });
    }

    public static String concatString(String inp1, String inp2) {
        return inp1 + " - " + inp2;
    }
}