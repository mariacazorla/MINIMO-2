package dsa.upc.edu.listapp.store;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    final static String URL = "http://10.0.2.2:8080/dsaApp/";
    private static Retrofit retrofit;
    private static StoreAPI storeAPI;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static StoreAPI getStoreAPI() {
        if (storeAPI == null) {
            storeAPI = getRetrofit().create(StoreAPI.class);
        }
        return storeAPI;
    }
}
