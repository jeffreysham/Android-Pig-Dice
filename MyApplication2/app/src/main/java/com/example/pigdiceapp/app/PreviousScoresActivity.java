package com.example.pigdiceapp.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.pigdiceapp.app.DataService.LocalBinder;
import java.util.ArrayList;

public class PreviousScoresActivity extends ActionBarActivity {


    DataService scoreService;
    ArrayList<ScoresObject> gameScores;
    private ListView scoreList;

    ServiceConnection scoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder)service;
            scoreService =binder.getService();
            gameScores = scoreService.getGameScores();
            displayScores();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            scoreService =null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_scores_list);
        scoreList = (ListView) findViewById(R.id.score_list);

    }

    public void displayScores(){
        ArrayAdapter<ScoresObject> arrayAdapter = new ArrayAdapter<ScoresObject>(
                this,
                android.R.layout.simple_list_item_1,
                gameScores);
        scoreList.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(PreviousScoresActivity.this,DataService.class);
        bindService(i, scoreServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(scoreServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.previous_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.back) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
