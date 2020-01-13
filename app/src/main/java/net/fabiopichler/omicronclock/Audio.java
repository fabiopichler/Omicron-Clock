package net.fabiopichler.omicronclock;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Audio implements MediaPlayer.OnCompletionListener {

    private final MediaPlayer mMediaPlayer = new MediaPlayer();
    private boolean mPlayMinute = true;

    private final AssetManager mAssetManager;
    private final String mPath;

    private final String mHour;
    private final String mMinute;

    Audio(AssetManager assetManager, String path) {
        mAssetManager = assetManager;
        mPath = path;

        Date date = new Date();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

        mHour = hourFormat.format(date);
        mMinute = minuteFormat.format(date);

        mMediaPlayer.setOnCompletionListener(this);

        play(String.format("/HRS%s%s.mp3", mHour, (mMinute.equals("00") ? "_O" : "")));
    }

    public void release() {
        if (mMediaPlayer.isPlaying()) {
            mPlayMinute = false;
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayMinute && !mMinute.equals("00"))
            play(String.format("/MIN%s.mp3", mMinute));

        mPlayMinute = false;
    }

    private void play(String fileName) {
        try {
            mMediaPlayer.reset();

            AssetFileDescriptor descriptor = mAssetManager.openFd("voices/" + mPath + fileName);

            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

            descriptor.close();

            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
