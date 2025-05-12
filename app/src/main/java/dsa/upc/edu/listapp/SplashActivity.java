package dsa.upc.edu.listapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView logo = findViewById(R.id.lottieAnimationView);
        logo.playAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            String usuarioRecordado = prefs.getString("usuarioRecordado", null);

            if (usuarioRecordado != null) {
                // El usuario eligió recordar
                Intent intent = new Intent(SplashActivity.this, PartidasMenuActivity.class);
                intent.putExtra("nombreUsu", usuarioRecordado);
                startActivity(intent);
            } else {
                // No hay usuario recordado, ir al login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }
}
