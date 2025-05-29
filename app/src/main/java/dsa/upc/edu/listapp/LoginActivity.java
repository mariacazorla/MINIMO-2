package dsa.upc.edu.listapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.auth.ApiService;
import dsa.upc.edu.listapp.TokenResponse;
import dsa.upc.edu.listapp.auth.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private CheckBox checkboxMostrarPassword, cbRememberMe;
    private Button btnLogin, btnGoToRegister;
    private ProgressBar progressBar;
    private ApiService api;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1) Referenciar vistas
        etLoginUsername       = findViewById(R.id.etLoginUsername);
        etLoginPassword       = findViewById(R.id.etLoginPassword);
        checkboxMostrarPassword = findViewById(R.id.checkboxMostrarPassword);
        cbRememberMe          = findViewById(R.id.cbRememberMe);
        btnLogin              = findViewById(R.id.btnLogin);
        btnGoToRegister       = findViewById(R.id.btnGoToRegister);
        progressBar           = findViewById(R.id.progressBar);

        // 2) Construir API
        api   = ApiClient.getClient(this).create(ApiService.class);
        prefs = getSharedPreferences("auth", MODE_PRIVATE);

        // 3) Toggle visibilidad contraseña
        checkboxMostrarPassword.setOnCheckedChangeListener((button, checked) -> {
            if (checked) {
                etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etLoginPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
            }
            etLoginPassword.setSelection(etLoginPassword.length());
        });

        // 4) Botón de login
        btnLogin.setOnClickListener(v -> {
            String username = etLoginUsername.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Usuario y contraseña obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            api.loginUser(new Usuario(username, password))
                    .enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> resp) {
                            progressBar.setVisibility(View.GONE);
                            if (resp.isSuccessful() && resp.body() != null) {
                                // Guardamos token y nombre de usuario
                                String token = resp.body().getToken();
                                prefs.edit()
                                        .putString("token", token)
                                        .putString("nombreUsu", username)
                                        .apply();

                                // Abrimos PartidasMenuActivity
                                startActivity(new Intent(LoginActivity.this, PartidasMenuActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 5) Botón para ir al registro
        btnGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }
}
