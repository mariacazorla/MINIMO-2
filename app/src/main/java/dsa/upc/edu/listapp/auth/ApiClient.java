package dsa.upc.edu.listapp.auth;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/dsaApp/";

    public static Retrofit getClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
                        String token = prefs.getString("token", null);

                        Request.Builder requestBuilder = original.newBuilder();
                        if (token != null) {
                            requestBuilder.header("Authorization", "Bearer " + token);
                        }

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
