package com.example.pigdiceapp.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsham on 6/3/14.
 */
public class DataService extends Service{

    private IBinder mBinder = new LocalBinder();

    List<ScoreRowItem> gameScores;

    @Override
    public void onCreate() {
        super.onCreate();
        gameScores = new ArrayList<ScoreRowItem>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder{
        public DataService getService(){

            return DataService.this;
        }
    }

    public void store(int playerScore, int compScore){
        ScoreRowItem temp = new ScoreRowItem(getString(R.string.player_score),getString(R.string.comp_score),playerScore,compScore);
        gameScores.add(temp);
    }

    public List<ScoreRowItem> getGameScores(){
        return gameScores;
    }

}
