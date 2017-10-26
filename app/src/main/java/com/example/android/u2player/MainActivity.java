package com.example.android.u2player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private final int FORWARD = 5000;
    private final int BACKWARD = 5000;

    private Button rewindButton, pauseButton, playButton, forwardButton, backButton, nextButton;
    private SeekBar seekBar;
    private TextView artistTextView, titleTextView, time1TextView, time2TextView;

    private MediaPlayer mMediaPlayer;

    private double startTime = 0;
    private double durationTime = 0;

    private Handler mHandler;

    private int id = 0;

    // Create an ArrayList of songs
    final ArrayList<Song> songs = new ArrayList<>();

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMedia();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rewindButton = (Button) findViewById(R.id.rewind_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        playButton = (Button) findViewById(R.id.play_button);
        forwardButton = (Button) findViewById(R.id.forward_button);
        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);

        // Pause isn't enable when no sound is playing
        pauseButton.setEnabled(false);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(this);

        artistTextView = (TextView) findViewById(R.id.artist_text_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        time1TextView = (TextView) findViewById(R.id.time1);
        time2TextView = (TextView) findViewById(R.id.time2);

        mHandler = new Handler();

        // Add songs to the ArrayList
        songs.add(new Song(1, "I Will Follow", R.raw.track01));
        songs.add(new Song(2, "Twilight", R.raw.track02));
        songs.add(new Song(3, "An Cat Dubh", R.raw.track03));
        songs.add(new Song(4, "Into The Heart", R.raw.track04));
        songs.add(new Song(5, "Out of Control", R.raw.track05));
        songs.add(new Song(6, "Stories For Boys", R.raw.track06));
        songs.add(new Song(7, "The Ocean", R.raw.track07));
        songs.add(new Song(8, "A Day Without Me", R.raw.track08));
        songs.add(new Song(9, "Another Time, Another Place", R.raw.track09));
        songs.add(new Song(10, "The Electric Co.", R.raw.track10));
        songs.add(new Song(11, "Shadows And Tall Trees", R.raw.track11));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start to play with the first song from the list.
                playMusic(songs.get(0).getTitle(), songs.get(0).getSongResourceId());
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward();
            }
        });
        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewind();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the id is equal to 0 tell user that there is no previous song to play.
                if (id == 0) {
                    Toast.makeText(getApplicationContext(), "You cannot go to previous song anymore :(",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Otherwise decrement id
                    id--;
                }

                // Play previous song.
                playPrevious(id);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the id is equal to 10 tell user that there is no next song to play.
                if (id == 10) {
                    Toast.makeText(getApplicationContext(), "You cannot go to next song anymore :(",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Otherwise increment id.
                    id++;
                }

                // Play next song.
                playNext(id);
            }
        });
    }

    /**
     * This method is called when rewindButton is clicked.
     */
    private void rewind() {
        // If the time we want to jump to is grater than 0 then jump to it.
        if((mMediaPlayer.getCurrentPosition() - BACKWARD) > 0){
            startTime = startTime - BACKWARD;
            mMediaPlayer.seekTo((int) startTime);
        }else{
            // Otherwise tell user it isn't possible to jump backward.
            Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when forwardButton is clicked.
     */
    private void forward() {
        // If the time we want to jump to is less or equal to the duration of the song the jump to it.
        if((mMediaPlayer.getCurrentPosition() + FORWARD) <= durationTime){
            startTime = startTime + FORWARD;
            mMediaPlayer.seekTo((int) startTime);
        }else{
            // Otherwise tell user it isn't possible to jump forward.
            Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void playMusic(String songTitle, int songID) {
            // If MediaPlayer is empty create it
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, songID);
                seekBar.setEnabled(true);

                // Set the title of the song.
                titleTextView.setText(songTitle);
            }

            // If MediaPlayer is playing then pause it
            if (mMediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                // Otherwise start playing
                mMediaPlayer.start();

                // Setup a listener on the media player, so that we can stop and release the
                // media player once the sound has finished playing.
                mMediaPlayer.setOnCompletionListener(mCompletionListener);

                // Set duration of the sound
                durationTime = mMediaPlayer.getDuration();
                // Set the duration time to the textview
                time2TextView.setText(String.format("%d min %d s",
                        TimeUnit.MILLISECONDS.toMinutes((long) durationTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) durationTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        durationTime))));

                // Set the maximum value to the seekbar
                seekBar.setMax(mMediaPlayer.getDuration());
                // Enable the pause button when playing sound
                pauseButton.setEnabled(true);
                // Disable the play button - sound is already playing
                playButton.setEnabled(false);

                // Update seekbar and textview while playing the sound
                updateProgress();
            }
    }

    private void pauseMusic() {
        // Pause the sound
        mMediaPlayer.pause();
        // Disable puase button - sound is already paused
        pauseButton.setEnabled(false);
        // Enable play button
        playButton.setEnabled(true);
    }

    /**
     * This method is called when backButton is clicked.
     */
    private void playPrevious(int songId) {
        releaseMedia();

        // Create a new Song object which represents current song.
        Song currentSong = songs.get(songId);

        // Play previous song.
        playMusic(currentSong.getTitle(), currentSong.getSongResourceId());
    }

    /**
     * This method is called when nextButton is clicked.
     */
    private void playNext(int songId) {
        releaseMedia();

        // Create a new Song object which represents current song.
        Song currentSong = songs.get(songId);

        // Play next song.
        playMusic(currentSong.getTitle(), currentSong.getSongResourceId());
    }

    /**
     * Release media player's resources.
     */
    private void releaseMedia() {
        if (mMediaPlayer != null) {
            // Release media player's resources because they are no longer needed.
            mMediaPlayer.release();

            // Set the media player back to null. A media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    // Update progress in seekBar and time1TextView
    private void updateProgress() {
        // Get data from the MediaPlayer
        startTime = mMediaPlayer.getCurrentPosition();
        // Update seekbar
        seekBar.setProgress((int)startTime);
        // Update textview with the current time of the sound
        time1TextView.setText(String.format("%d min %d s",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime))));
        mHandler.postDelayed(runnable, 100);
    }


    // This method is called when user touches the seekbar and changes its position
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            if (mMediaPlayer.isPlaying() || mMediaPlayer != null) {
                if (fromUser)
                    // go to the position from the seekbar
                    mMediaPlayer.seekTo(progress);
            } else if (mMediaPlayer == null) {
                // Inform user any sound is playing now
                Toast.makeText(getApplicationContext(), "You're not playing any song now",
                        Toast.LENGTH_SHORT).show();
                seekBar.setProgress(0);
            }
        } catch (Exception e) {
            Log.e("Problem with seekbar ", "" + e);
            seekBar.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
