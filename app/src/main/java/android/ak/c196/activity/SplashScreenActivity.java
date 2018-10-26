package android.ak.c196.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.ak.c196.R;

public class SplashScreenActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(newIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
