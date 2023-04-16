package com.zain.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.zain.game.adapter.ListViewAdapter;
import com.zain.game.service.Leaderboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar);
        setContentView(R.layout.activity_leaderboard);

        ListView listView = findViewById(R.id.listview);

        Leaderboard.getInstance().getLeaderBoard(100, 1000, 5, new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful()) {
                    List<Integer> randomNumbers = response.body();
                    ListViewAdapter<Integer> adapter = new ListViewAdapter<Integer>(this, R.layout.activity_leaderboard, randomNumbers);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(LeaderboardActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onFailure(Call<List<Integer>> call, Throwable t) {
//                Toast.makeText(LeaderboardActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//            }
        }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {

            }
        });
    }
}