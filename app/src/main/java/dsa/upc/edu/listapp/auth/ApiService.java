package dsa.upc.edu.listapp.auth;

import java.util.List;

import dsa.upc.edu.listapp.TokenResponse;
import dsa.upc.edu.listapp.auth.Producto;
import dsa.upc.edu.listapp.auth.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("usuarios/register")
    Call<Void> registerUser(@Body Usuario usuario);

    @POST("usuarios/login")
    Call<TokenResponse> loginUser(@Body Usuario usuario);



}