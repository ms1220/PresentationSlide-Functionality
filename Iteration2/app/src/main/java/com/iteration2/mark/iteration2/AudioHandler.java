package com.iteration2.mark.iteration2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import java.io.IOException;
import java.util.ArrayList;
import static android.media.AudioManager.STREAM_MUSIC;

public class AudioHandler implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    public WifiManager.WifiLock wifiLock;
    public Activity activity;
    //Instantiate the media handlers
    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;
    //holds the URLs that the content is streamed from
    public ArrayList<String> URLs;
    //used to change the image of the play and mute buttons depending on their state
    public ImageButton playButton, muteButton;
    //used to control the position in the playlist
    public int CurrentSong = 0;
    public int num_songs;
    //used to ensure that the audio starts playing in the correct state
    public boolean AudioReady;
    public boolean playTrack = false;
    public PopupWindow popUpWindow;
    public Button popup;
    //sets up the mediaPlayer when the class is called
    AudioHandler(Activity calledFrom, ArrayList<String> songs){
        this.activity = calledFrom;
        setupButtons();
        setupMediaPlayer();
        setAudioFocus();
        setPlaylist(songs);
    }
    public void displayPopUp(View view){
        LayoutInflater layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = layoutInflater.inflate(R.layout.popup, null);
        popUpWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popUpWindow.setAnimationStyle(R.style.Animation);
        popUpWindow.showAtLocation(view,0,250,250);
        playButton = (ImageButton) popUpView.findViewById(R.id.playButton);
        muteButton = (ImageButton) popUpView.findViewById(R.id.muteButton);
    }
    public void dismissPopUp(){popUpWindow.dismiss();}
    //sets the playlist from the songs argument of the class
// and prepares the mediaPlayer to play the first song in the playlist
    public void setPlaylist(ArrayList<String> songs){
        URLs = songs;
//sets the number of songs in the playlist
        num_songs = URLs.size();
        setSong(URLs.get(0));
    }
    //declares the buttons that will be manipulated by the class
    public void setupButtons(){
        popup = (Button) this.activity.findViewById(R.id.popup);
    }
    //requests that the app is the main audio focus of the phone
    public void setAudioFocus(){
        audioManager = (AudioManager) this.activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }
    //sets the song source using the URL argument and
// asynchronously prepares the mediaPlayer to play music
    public void setSong(String URL){
        try{
            mediaPlayer.setDataSource(URL);
            AudioReady = false;
            mediaPlayer.prepareAsync();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    // instantiates the mediaPlayer
    public void setupMediaPlayer() {
//ensures that the hardware buttons control the music volume
        this.activity.setVolumeControlStream(STREAM_MUSIC);
//instantiates the mediaPlayer
        mediaPlayer = new MediaPlayer();
//sets the mediaPlayer to control the songs rather than other audio streams
        mediaPlayer.setAudioStreamType(STREAM_MUSIC);
//attaches a listener to the mediaPlayer that will run
// when the mediaPlayer is prepared, for further explanation see the state diagram at:
// https://developer.android.com/reference/android/media/MediaPlayer.html#setDataSource(java.lang.String)
        mediaPlayer.setOnPreparedListener(this);
//attaches a listener to the mediaPlayer that will run if an error occurs on the mediaPlayer,
// this will likely be because a method has been called in an impossible state, for further explanation see:
// https://developer.android.com/reference/android/media/MediaPlayer.html#setDataSource(java.lang.String)
        mediaPlayer.setOnErrorListener(this);
// this ensures that if the phone falls asleep the CPU will keep running
        mediaPlayer.setWakeMode(this.activity.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
//
// this ensures that if the phone falls asleep the phone will still have access to the wifi
        wifiLock = ((WifiManager)this.activity.getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL,"mylock");
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
// This method plays and pauses the music depending on the current state
// the mediaPlayer and on whether the mediaPlayer is prepared
    public void playMusic() {
// If the media is already playing then pause it and change the button symbol to reflect this
        if (mediaPlayer.isPlaying()) {
            playButton.setBackground(this.activity.getResources().getDrawable(android.R.drawable.ic_media_play));
            mediaPlayer.pause();
            wifiLock.release();
        }
// If the media is paused and the mediaPlayer is prepared then start playing
// and change the button symbol to reflect this, else do nothing
        else if(AudioReady){
            playButton.setBackground(this.activity.getResources().getDrawable(android.R.drawable.ic_media_pause));
            mediaPlayer.start();
            wifiLock.acquire();
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
// This method mutes and unmutes the music depending on the current state
// of the mediaPlayer and on whether the mediaPlayer is prepared
    public void mute(){
// If the mediaPlayer is muted then unmute it and change the button symbol to reflect this
        if(audioManager.getStreamVolume(STREAM_MUSIC) == 0) {
            audioManager.setStreamMute(STREAM_MUSIC, false);
            muteButton.setBackground(this.activity.getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off));
        }
// If the mediaPlayer is not muted then mute it and change the button symbol to reflect this
        else {
            audioManager.setStreamMute(STREAM_MUSIC, true);
            muteButton.setBackground(this.activity.getResources().getDrawable(android.R.drawable.ic_lock_silent_mode));
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
/*
This method does two things, if the current song is currently less than three seconds
into the song then the song starts from the beginning, otherwise the song before the current one in the playlist is selected.
If the mediaPlayer was already playing when the song was changed or restarted then the chosen song will begin playing automatically.
If not then the mediaPlayer will only begin playing when the play button is pressed
If the current song is the first in the playlist and the user wants to go to a previous
song the playlist will loop around so that the last song in the playlist is selected.
*/
    public void backTrack(){
//stores the required information from the mediaPlayer before the mediaPlayer is reset
        int position = mediaPlayer.getCurrentPosition();
// if the mediaPlayer was already playing when the button is pressed then set the playTrack field,
// as soon as the mediaPlayer is prepared this field tells the mediaPlayer to being playing,
// for further information see the onPrepared method
        playTrack = mediaPlayer.isPlaying();
        mediaPlayer.reset();
// if the song is already 3 seconds in
        if(position>3000)
            CurrentSong = (CurrentSong != 0) ? CurrentSong-- : num_songs-1;
//prepare the chosen song to play on the mediaPlayer
        setSong(URLs.get(CurrentSong));
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
/*
This method is used to change the selected song to be the next song in the playlist.
If the mediaPlayer was already playing when the song was changed or restarted then the chosen song will begin playing automatically.
If not then the mediaPlayer will only begin playing when the play button is pressed
If the current song is the last in the playlist and the user wants to go to the next
song the playlist will loop around so that the first song in the playlist is selected.
*/
    public void forwardTrack(){
// if the mediaPlayer was already playing when the button is pressed then set the playTrack field,
// as soon as the mediaPlayer is prepared this field tells the mediaPlayer to being playing,
// for further information see the onPrepared method
        playTrack = mediaPlayer.isPlaying();
        mediaPlayer.reset();
//if the current song is not the last one in the list loop to the first one in the list
        CurrentSong = (CurrentSong < (num_songs-1)) ? CurrentSong++ : 0;
//prepare the mediaPlayer to play the current song
        setSong(URLs.get(CurrentSong));
    }
    //releases the mediaPlayer completely
    public void stopMedia(){
        wifiLock.release();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
    @Override
//This method is called when the mediaPlayer is prepared to play music
    public void onPrepared(MediaPlayer mp) {
//if playTrack is high then the mediaPlayer should start playing immediately
        if(playTrack)
            mediaPlayer.start();
//resets playTrack, if playTrack was already false this will obviously have no effect
        playTrack = false;
//used in other methods to check if the mediaPlayer is ready to play,
// this is reset when the mediaPlayer is told to prepare itself
        AudioReady = true;
    }
    @Override
//this method is called when the mediaPlayer encounters an error, it simply resets the mediaPlayer currently
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }
    @Override
//this method is called when there is a change in audio focus and handles that change
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
//if the app gains audio focus
            case AudioManager.AUDIOFOCUS_GAIN:
                if(mediaPlayer == null){
                    setupMediaPlayer();
                }
                else if(!mediaPlayer.isPlaying()){
                    if(AudioReady){
                        mediaPlayer.start();
                        mediaPlayer.setVolume(1.0f,1.0f);
                    }
                }
                break;
//if the app loses audio focus
            case AudioManager.AUDIOFOCUS_LOSS:
                if(mediaPlayer.isPlaying())
                    stopMedia();
                break;
//if the app temporarily loses audio focus
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
//if the app is ducked
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if(mediaPlayer.isPlaying())
                    mediaPlayer.setVolume(0.1f,0.1f);
                break;
        }
    }
}