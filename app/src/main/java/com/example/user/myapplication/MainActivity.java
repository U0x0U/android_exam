package com.example.user.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
//import androidx.fragment.app.FragmentActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView result;
    Button btn;
    EditText input;
    TextView tv_showanswer;
    TextView history;
    int[] answer = new int[4];
    Button showanswer;
    Button answerreset;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(MainActivity.this);
        result = (TextView) findViewById(R.id.tv_result);
        input = (EditText) findViewById(R.id.et_input);
        btn = (Button) findViewById(R.id.btn_str);
        btn.setOnClickListener(calcResult);
        showanswer = (Button) findViewById(R.id.showanswer);
        showanswer.setOnClickListener(sans);
        answerreset = (Button) findViewById(R.id.answerreset);
        answerreset.setOnClickListener(ansreset);
        tv_showanswer = findViewById(R.id.tv_showanswer);
        history = findViewById(R.id.guess_history);
        GenerateAnswer();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng tw = new LatLng(24.16, 120.45);
        mMap.addMarker(new MarkerOptions().position(tw).title("Marker in TW"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tw));
    }

    private void GenerateAnswer() {
        for (int i = 0; i < 4; i++) {
            boolean breakflag = true;
            do {
                breakflag = true;
                answer[i] = (int) (Math.random() * 10);
                for (int j = 0; j < i; j++) {
                    if (answer[i] == answer[j]) {
                        breakflag = false;
                        break;
                    }
                }
            } while (!breakflag);
        }
    }

    private View.OnClickListener sans = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String answer_str = "答案為：";
            for (int i = 0; i < 4; i++) {
                answer_str += answer[i];
                tv_showanswer.setText(answer_str);
            }
        }
    };
    private View.OnClickListener ansreset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input.setText("");
            history.setText("");
            result.setText("");
            tv_showanswer.setText("");
            GenerateAnswer();
            Toast.makeText(MainActivity.this,
                    R.string.ansre,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private final View.OnClickListener calcResult = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String input_str = input.getText().toString();
            input.setText("");
            if (input_str.length() != 4) {
                Toast.makeText(MainActivity.this,
                        R.string.input_error,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int inputmath = Integer.parseInt(input_str);
            int[] inputarray = new int[4];
            inputarray[0] = inputmath / 1000;
            inputarray[1] = (inputmath % 1000) / 100;
            inputarray[2] = (inputmath % 100) / 10;
            inputarray[3] = inputmath % 10;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < i; j++) {
                    if (inputarray[i] == inputarray[j]) {
                        Toast.makeText(MainActivity.this,
                                R.string.input_error2,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            result.setText("猜測結果為：" + Compare(input_str));
            history.append(input_str + "　　" + Compare(input_str) + "\n");
        }

        private String Compare(String input_str) {
            String result = "";
            int guess = Integer.parseInt(input_str);
            int[] guessarray = new int[4];
            guessarray[0] = guess / 1000;
            guessarray[1] = (guess % 1000) / 100;
            guessarray[2] = (guess % 100) / 10;
            guessarray[3] = guess % 10;
            int counta = 0, countb = 0;
            String ans = "";
            for (int x = 0; x < 4; x++) {
                ans = ans + answer[x];
            }
            for (int i = 0; i < 4; i++) {
                if (guessarray[i] == answer[i]) counta++;
            }
            for (int n = 0; n < 4; n++) {
                for (int m = 0; m < 4; m++) {
                    if (n == m) continue;
                    if (guessarray[n] == answer[m]) countb++;
                }
            }
            if (counta != 4) {
                result = result + counta + "A" + countb + "B";
                return result;
            } else {
                result = result + counta + "A" + countb + "B" + "\n答案是：" + ans;
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.shoot2);
                mp.start();
                return result;
            }
        }
    };
}