package com.example.android.u2player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button rewindButton, pauseButton, playButton, forwardButton;
    private SeekBar seekBar;
    private TextView artistTextView, titleTextView, time1TextView, time2TextView;

    private MediaPlayer mMediaPlayer;

    private double startTime = 0;
    private double durationTime = 0;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rewindButton = (Button) findViewById(R.id.rewind_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        playButton = (Button) findViewById(R.id.play_button);
        forwardButton = (Button) findViewById(R.id.forward_button);

        // Pause isn't enable when no sound is playing
        pauseButton.setEnabled(false);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setClickable(false);

        artistTextView = (TextView) findViewById(R.id.artist_text_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        time1TextView = (TextView) findViewById(R.id.time1);
        time2TextView = (TextView) findViewById(R.id.time2);

        mHandler = new Handler();
        mMediaPlayer = MediaPlayer.create(this, R.raw.track01);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });
    }

    private void pauseMusic() {
        // pause the sound
        mMediaPlayer.pause();
        // disable puase button - sound is already paused
        pauseButton.setEnabled(false);
        // enable play button
        playButton.setEnabled(true);
    }

    private void playMusic() {
        // start playing sound
        mMediaPlayer.start();
        // get the duration time of the sound
        durationTime = mMediaPlayer.getDuration();

        // set the duration time to the textview
        time2TextView.setText(String.format("%d min %d s",
                TimeUnit.MILLISECONDS.toMinutes((long) durationTime),
                TimeUnit.MILLISECONDS.toSeconds((long) durationTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                durationTime))));

        // set progress to the seekbar
        //seekBar.setProgress((int)startTime);
        // set max value to the seekbar (max value is the duration of the sound)
        seekBar.setMax((int) durationTime);

        // update seekbar and textview while playing the sound
        updateProgress();

        // enable the pause button when playing sound
        pauseButton.setEnabled(true);
        // disable the play button - sound is already playing
        playButton.setEnabled(false);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    // update progress in seekBar and time1TextView
    private void updateProgress() {
        startTime = mMediaPlayer.getCurrentPosition();
        seekBar.setProgress((int)startTime);
        // set the current time to the textview
        time1TextView.setText(String.format("%d min %d s",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime))));
        mHandler.postDelayed(runnable, 100);
    }
}
