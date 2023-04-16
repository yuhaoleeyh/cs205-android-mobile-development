package com.zain.game.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Leaderboard {
    private static final String BASE_URL = "https://www.randomnumberapi.com/api/v1.0/";
    private static final String ENDPOINT = "random";

    private static Leaderboard instance;
    private Retrofit retrofit;

    private Leaderboard() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    public void getLeaderBoard(int min, int max, int count, Callback<List<Integer>> callback) {
        RandomNumberApiService service = retrofit.create(RandomNumberApiService.class);
        Call<List<Integer>> call = service.getRandomNumbers(min, max, count);
        call.enqueue(callback);
    }

    public interface RandomNumberApiService {
        @GET(ENDPOINT)
        Call<List<Integer>> getRandomNumbers(@Query("min") int min, @Query("max") int max, @Query("count") int count);
    }
}
