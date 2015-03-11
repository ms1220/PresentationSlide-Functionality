/*

Claritas: ‘Clarity through innovation’

Project: SocBox

Module: Presentation module

Code File Name: Iteration2 (will change when both code halfs are collated)

Description: This is the functionality of the presentation slides without being in the correct layouts.
This wil be added to the layout files when they are complete due to delays with the integration tests.

Initial Authors: Mark Stonehouse

Change History:

Version: 0.1

Author: Mark Stonehouse

Change: Created original version

Date: 11/03//2015

Traceabilty:

Tag: U/PS/04/1

Requirement: the user can go through the presentation by clicks or automatically

Tag: U/PS/05/1

Requirement: the user can click certain views to move to extra infomation

Tag: U/PS/06/1

Requirement:the user can controll the video and audio oon the slides

Tag: U/PS/07/1

Requirement: after teh presentation the user will move to the society home page

Tag: U/PS/08/1

Requirement: the user can exit or restart the presentation

Other Information:

Note: This file is still work in progress.

 Todo: U/PS/01

    add this code to the user stories U/PS/01-03/01

*/
package com.iteration2.mark.iteration2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainJava extends ActionBarActivity {

    String auto = new String();
    public ArrayList<String> musicURLs = new ArrayList<>();
    public AudioHandler audioHandler;


    public void displayPopUp(View view){audioHandler.displayPopUp(view);}
    public void dismissPopUp(View view){audioHandler.dismissPopUp();}
    public void backTrack(View view){audioHandler.backTrack();}
    public void forwardTrack(View view){audioHandler.forwardTrack();}
    public void playMusic(View view){audioHandler.playMusic();}
    public void mute(View view){audioHandler.mute();}



    public void nextSlide(View view){
        Intent intent = new Intent(MainJava.this, SecondSlide.class);
        intent.putExtra("auto", false);
        startActivity(intent);
        }
    public void runTimer(View view){
        Timer timer = new Timer();
        timer.schedule(timerTask,5000);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(MainJava.this, SecondSlide.class);
            intent.putExtra("auto", true);
            startActivity(intent);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        musicURLs.add(0, "http://android.programmerguru.com/wp-content/uploads/2013/04/hosannatelugu.mp3");
        audioHandler = new AudioHandler(this, musicURLs);
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
