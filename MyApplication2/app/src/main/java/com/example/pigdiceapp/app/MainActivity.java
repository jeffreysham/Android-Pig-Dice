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

    static DataService mService;
    boolean mBound;
    Random rand = new Random();
    Button rollButton;
    Button holdButton;
    ImageView image;
    TextView current,player, comp;
    int currentTotal;
    int playerTotal;
    int compTotal;
    Toast t;
    boolean turn;//True for player turn, false for computer turn
    boolean gameOver;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            LocalBinder binder = (LocalBinder)service;
            mService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turn=true;
        gameOver=false;
        image=(ImageView) findViewById(R.id.imageView);
        current=(TextView)findViewById(R.id.textView);
        player=(TextView)findViewById(R.id.textView2);
        comp =(TextView)findViewById(R.id.textView3);
        rollButton=(Button)findViewById(R.id.button);
        holdButton=(Button)findViewById(R.id.button2);
        t = new Toast(getApplicationContext());
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
        current.setText("Current Total:             "+currentTotal);
        if(turn){
            turn=false;
            Log.i("run","turn is now false");
            decide();
        } else {
            turn = true;
            Log.i("run","turn is now true");
        }
    }

    public void holdDice(){
        if(turn) {
            playerTotal += currentTotal;
            player.setText("Player Total:               " + playerTotal);
        }
        else {
            compTotal += currentTotal;
            comp.setText("Computer Total:         " + compTotal);
            Log.i("run","comp score = "+compTotal);
        }
        currentTotal=0;
        current.setText("Current Total:             "+currentTotal);
        if(playerTotal>=100|| compTotal >=100){
            gameOver();
        }
    }

    public void setDice(int num){
        currentTotal+=num;
        if(num==1){
            image.setImageResource(R.drawable.dice_1);
            currentTotal=0;
            Log.i("run","it went to 1");
            changeTurn();
            return;
        } else if(num==2){
            image.setImageResource(R.drawable.dice_2);
        } else if(num==3){
            image.setImageResource(R.drawable.dice_3);
        } else if(num==4){
            image.setImageResource(R.drawable.dice_4);
        } else if(num==5){
            image.setImageResource(R.drawable.dice_5);
        } else {
            image.setImageResource(R.drawable.dice_6);
        }
        current.setText("Current Total:             "+currentTotal);
    }

    public void gameOver(){
        gameOver=true;
        if(playerTotal>=100){
            t.makeText(getApplicationContext(), "Player 1 won", Toast.LENGTH_SHORT).show();
        } else {
            t.makeText(getApplicationContext(),"Player 2 won", Toast.LENGTH_SHORT).show();
        }
        mService.store(playerTotal, compTotal);
        newGame();
    }

    public static DataService getmService(){
        return mService;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this,DataService.class);
        bindService(i, mConnection, BIND_AUTO_CREATE);
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
            t.makeText(getApplicationContext(), "New Game", Toast.LENGTH_SHORT).show();
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
        current.setText("Current Total:             "+currentTotal);
        player.setText("Player Total:               " + playerTotal);
        comp.setText("Computer Total:         " + compTotal);
        if(turn&&gameOver) {
            turn = false;
            gameOver=false;
        } else
            turn=true;
    }

    public int getRandomNumber(){
        return rand.nextInt(6)+1;
    }

    public void decide(){
        Log.i("run","decide is now called");
        ComputerTurn t = new ComputerTurn();
        t.execute();

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
    }

    private class ComputerTurn extends AsyncTask<Void,Void,Void>{
        int num;
        int count;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rollButton.setEnabled(false);
            holdButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("run","onPostExecute is now called");
            try {
                Thread.sleep(1000);
                setDice(num);

                if(!turn&&currentTotal>=20&&count==1) {
                    holdDice();
                    if(!turn)
                        changeTurn();
                }
                else if(!turn&currentTotal>=10&&count==0){
                    holdDice();
                    if(!turn)
                        changeTurn();
                }
                else if(!turn){
                    decide();
                }
                Thread.sleep(1000);
                Log.i("run","it completed the onPostExecute");
            } catch (InterruptedException i){
                i.getStackTrace();
            }
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
            return null;
        }
    }

}
