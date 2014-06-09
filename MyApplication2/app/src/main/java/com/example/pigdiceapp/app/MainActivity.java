package com.example.pigdiceapp.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigdiceapp.app.DataService.LocalBinder;

import java.util.Random;

public class MainActivity extends ActionBarActivity {

    DataService scoreService;
    Random rand = new Random();
    Button rollButton;
    Button holdButton;
    ImageView dicePicture;
    TextView currentScoreText, playerScoreText, compScoreText;
    int currentTotal;
    int playerTotal;
    int compTotal;
    Toast toastText;
    boolean playerTurn,compTurn;
    boolean gameOver;

    ServiceConnection scoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder)service;
            scoreService =binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            scoreService =null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerTurn=true;
        compTurn=false;
        gameOver=false;
        dicePicture =(ImageView) findViewById(R.id.dice_pic);
        currentScoreText =(TextView)findViewById(R.id.current_score_text);
        playerScoreText =(TextView)findViewById(R.id.player_text);
        compScoreText =(TextView)findViewById(R.id.comp_text);
        rollButton=(Button)findViewById(R.id.roll_button);
        holdButton=(Button)findViewById(R.id.hold_button);
        toastText = new Toast(getApplicationContext());
        currentTotal=0;
        playerTotal=0;
        compTotal =0;
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = getRandomNumber();
                setDice(num);
            }
        });
        holdButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holdDice();
                changeTurn();
            }
        });
    }

    public void changeTurn(){
        currentScoreText.setText(getString(R.string.current_score, currentTotal));
        if(playerTurn){
            compTurn=true;
            playerTurn=false;
            rollButton.setEnabled(false);
            holdButton.setEnabled(false);
            computersTurnToRoll();
        } else {
            playerTurn = true;
            compTurn = false;
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
        }
    }

    public void holdDice(){
        if(playerTurn) {
            playerTotal += currentTotal;
            playerScoreText.setText(getString(R.string.player_score, playerTotal));
        }
        else {
            compTotal += currentTotal;
            compScoreText.setText(getString(R.string.comp_score, compTotal));
        }
        currentTotal=0;
        currentScoreText.setText(getString(R.string.current_score, currentTotal));
        if(playerTotal>=100|| compTotal >=100){
            gameOver();
        }
    }

    public void setDice(int num){
        currentTotal+=num;
        if(num==1){
            dicePicture.setImageResource(R.drawable.dice_1);
            currentTotal=0;
            Log.i("run","it went to 1");
            changeTurn();
            return;
        } else if(num==2){
            dicePicture.setImageResource(R.drawable.dice_2);
        } else if(num==3){
            dicePicture.setImageResource(R.drawable.dice_3);
        } else if(num==4){
            dicePicture.setImageResource(R.drawable.dice_4);
        } else if(num==5){
            dicePicture.setImageResource(R.drawable.dice_5);
        } else {
            dicePicture.setImageResource(R.drawable.dice_6);
        }
        currentScoreText.setText(getString(R.string.current_score, currentTotal));
    }

    public void gameOver(){
        gameOver=true;
        if(playerTotal>=100){
            toastText.makeText(getApplicationContext(), "Player won", Toast.LENGTH_SHORT).show();
        } else {
            toastText.makeText(getApplicationContext(), "Computer won", Toast.LENGTH_SHORT).show();
        }
        scoreService.store(playerTotal, compTotal);
        Log.i("run",""+scoreService.getGameScores());
        newGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this,DataService.class);
        bindService(i, scoreServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(scoreServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.new_game) {
            toastText.makeText(getApplicationContext(), "New Game", Toast.LENGTH_SHORT).show();
            newGame();
        }
        if (id == R.id.previous) {
            Intent i = new Intent(this,PreviousScoresActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void newGame(){
        playerTotal=0;
        compTotal =0;
        currentTotal=0;
        currentScoreText.setText(getString(R.string.current_score, currentTotal));
        playerScoreText.setText(getString(R.string.player_score, playerTotal));
        compScoreText.setText(getString(R.string.comp_score, compTotal));
        if(playerTurn&&gameOver) {
            playerTurn = false;
            compTurn = true;
            gameOver=false;
        } else {
            playerTurn = true;
            compTurn = false;
            gameOver=false;
        }
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
    }

    public int getRandomNumber(){
        return rand.nextInt(6)+1;
    }

    public void computersTurnToRoll(){
        Log.i("run","computersTurnToRoll is now called");
        RunComputerTurn t = new RunComputerTurn();
        t.execute();
    }

    private class RunComputerTurn extends AsyncTask<Void,Void,Void>{
        int num;
        int count;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("run","onPostExecute is now called");
            runOnUiThread(new Runnable() {
                public void run() {
                    if(compTurn) {
                        setDice(num);
                    }
                }
            });

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            num = getRandomNumber();
            count=num%2;
            Log.i("run","doInBackground is now called");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.getStackTrace();
            }

            if(compTurn&&currentTotal>=20&&count==1) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        holdDice();
                        if(compTurn)
                            changeTurn();
                    }
                });
            }
            else if(compTurn&currentTotal>=10&&count==0){
                runOnUiThread(new Runnable() {
                    public void run() {
                        holdDice();
                        if(compTurn)
                            changeTurn();
                    }
                });
            }
            else if(compTurn){
                computersTurnToRoll();
            }
            return null;
        }
    }

}
