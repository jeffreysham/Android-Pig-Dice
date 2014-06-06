package com.example.pigdiceapp.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by jsham on 6/3/14.
 */
public class DataService extends Service{

    private IBinder mBinder = new LocalBinder();
    int[][] scores; //TODO Make this an arrayList and create a model object to store this stuff in
    int count=0; //TODO why is this being counted

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder{
        public DataService getService(){
            scores=new int[10][2];
            return DataService.this;
        }
    }

    public void store(int playerScore, int compScore){
        scores[count][0]=playerScore;
        scores[count][1]=compScore;
        count++;
    }

    public int[][] getScores(){
        return scores;
    }

    public int getNumScores(){
        return count;
    }
}
