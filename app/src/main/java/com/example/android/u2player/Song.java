package com.example.android.u2player;

/**
 * {@link Song} represents a song that is played in U2 Player.
 * It contains a ID, title and audio file for that song.
 */
public class Song {

    // ID of the song
    private int mId;

    // Title of the song
    private String mTitle;

    // Audio resource of the song
    private int mSongResourceId;

    /**
     * Create a new Song object.
     *
     * @param id is the id of the song
     * @param title is title of the song
     * @param songResourceId is the resource ID for the audio file associated with the song
     */
    public Song (int id, String title, int songResourceId){
        mId = id;
        mTitle = title;
        mSongResourceId = songResourceId;
    }

    /**
     * Return song's ID.
     */
    public int getId() {
        return mId;
    }

    /**
     * Return song's title.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Return the audio resource ID of the song.
     */
    public int getSongResourceId() {
        return mSongResourceId;
    }
}
