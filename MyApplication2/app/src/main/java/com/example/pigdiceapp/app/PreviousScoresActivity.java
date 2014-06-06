package com.example.pigdiceapp.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PreviousScoresActivity extends ActionBarActivity { //TODO this needs to have a tableview


    DataService mService; //TODO lose the m, more descriptive
    TextView txt; //TODO more descriptive

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_scores);
        txt=(TextView)findViewById(R.id.the_previous_scores);
        mService=MainActivity.getmService();
        getScores();
    }

    public void getScores(){ //This needs to connect to an adapter for a tableview
        int[][] previousScores=mService.getScores();
        int numScores = mService.getNumScores();
        String output="";
        for(int i = 0; i<numScores;i++){
            output=output+"Game "+(i+1)+":\nPlayer Total: "+previousScores[i][0]+"\nComputer Total: "+previousScores[i][1]+"\n\n";
        }
        txt.setText(output);
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
            finish(); //TODO nitpick not needed
        }
        return super.onOptionsItemSelected(item);
    }

}
