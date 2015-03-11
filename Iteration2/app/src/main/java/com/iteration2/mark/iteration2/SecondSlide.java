package com.iteration2.mark.iteration2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class SecondSlide extends ActionBarActivity {

    Boolean auto;
    Timer timer;

    public void runTimer(){
        timer = new Timer();
        timer.schedule(timerTask,5000);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(SecondSlide.this, ThirdSlide.class);
            intent.putExtra("auto", auto);
            startActivity(intent);
        }};

    public void nextSlide(View view){
        if(auto == true){
            timer.cancel();
        }
        Intent intent = new Intent(SecondSlide.this, ThirdSlide.class);
        startActivity(intent);
    }

    public void previousSlide(View view){
        if(auto == true){
            timer.cancel();
        }
        Intent intent = new Intent(SecondSlide.this, MainJava.class);
        startActivity(intent);
    }

    public void exitPresentation(View view){
        if(auto == true){
            timer.cancel();
        }
        Intent intent = new Intent(SecondSlide.this, ThirdSlide.class);
        startActivity(intent);
    }


    public void restartPresentation(View view){
        if(auto == true){
            timer.cancel();
        }
        Intent intent = new Intent(SecondSlide.this, MainJava.class);
        startActivity(intent);
    }

    public void extraInfo(View view){
        if(auto == true){
            timer.cancel();
        }
        Intent intent = new Intent(SecondSlide.this, MainJava.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondslide);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            auto = extras.getBoolean("auto");
        }
        if (auto == true){
            runTimer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_java, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
