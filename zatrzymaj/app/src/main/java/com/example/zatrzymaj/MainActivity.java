package com.example.zatrzymaj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Button stop;
    private TextView licznik;
    private TextView komunikat;
    private TextView trafienia;
    private int sekundy = 0;
    private int liczbaTrafien = 0;
    private boolean isRunning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Przeniesione na poziom klasy, aby mieć dostęp w onDestroy()
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            licznik.setText(String.valueOf(sekundy));

            if (isRunning) {
                sekundy = (sekundy + 1) % 10;
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        start = findViewById(R.id.button);
        stop = findViewById(R.id.button2);
        licznik = findViewById(R.id.textView3);
        komunikat = findViewById(R.id.textView);
        trafienia = findViewById(R.id.textView2);

        licznik.setText("0");
        komunikat.setText("");
        trafienia.setText("Trafienia: 0");
        stop.setEnabled(false);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    sekundy = 0;
                    komunikat.setText("");

                    start.setEnabled(false);
                    stop.setEnabled(true);

                    handler.post(timerRunnable);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;
                    handler.removeCallbacks(timerRunnable);

                    start.setEnabled(true);
                    stop.setEnabled(false);

                    String zatrzymanaCyfra = licznik.getText().toString();

                    if (zatrzymanaCyfra.equals("7")) {
                        liczbaTrafien++;
                        komunikat.setText("Trafienie!");
                        trafienia.setText("Trafienia: " + liczbaTrafien);
                    } else {
                        komunikat.setText("Pudło! Zatrzymano na " + zatrzymanaCyfra);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}
