package com.example.android.u2player;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button rewindButton, pauseButton, playButton, forwardButton;
    private SeekBar seekBar;
    private TextView artistTextView, titleTextView, time1TextView, time2TextView;

    private MediaPlayer mMediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

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

        mMediaPlayer = MediaPlayer.create(this, R.raw.track01);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start playing sound
                mMediaPlayer.start();

                // get the duration time of the sound
                finalTime = mMediaPlayer.getDuration();

                // get the current time of the sound
                startTime = mMediaPlayer.getCurrentPosition();

                // set the current time to the textview
                time1TextView.setText(String.format("%d min %d s",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime))));

                // set the duration time to the textview
                time2TextView.setText(String.format("%d min %d s",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime))));

                // set progress to the seekbar
                seekBar.setProgress((int)startTime);

                // enable the pause button when playing sound
                pauseButton.setEnabled(true);

                // disable the play button - sound is already playing
                playButton.setEnabled(false);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause the sound
                mMediaPlayer.pause();

                // disable puase button - sound is already paused
                pauseButton.setEnabled(false);

                // enable play button
                playButton.setEnabled(true);
            }
        });

    }
}
