package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {
    LottieAnimationView lottie ;
    TextView appname ;
    ImageView splashimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashimage = findViewById(R.id.splashimage);

        lottie = findViewById(R.id.lottie);
        appname=findViewById(R.id.appname);

        lottie.animate().translationY(-1800).setDuration(1000).setStartDelay(4000);
        appname.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

        splashimage.animate().translationY(1600).setDuration(1000).setStartDelay(4000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        { @Override
        public void run()
        {
            Intent intent = new Intent(getApplicationContext(), Sign_in.class);
            startActivity(intent);
        }
        },5000);

    }


}