package com.example.pigdiceapp.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

/**
 * Created by jsham on 6/3/14.
 */
public class DataService extends Service{

    private IBinder mBinder = new LocalBinder();

    ArrayList<ScoresObject> gameScores;

    @Override
    public void onCreate() {
        super.onCreate();
        gameScores = new ArrayList<ScoresObject>();
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
        ScoresObject temp = new ScoresObject(playerScore,compScore);
        gameScores.add(temp);
    }

    public ArrayList<ScoresObject> getGameScores(){
        return gameScores;
    }

}
