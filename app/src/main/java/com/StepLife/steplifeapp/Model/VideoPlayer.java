package com.StepLife.steplifeapp.Model;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends VideoView implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;


    public VideoPlayer(Context context, AttributeSet attributes) {
        super(context,attributes);
        this.setOnPreparedListener(this);
        this.setOnCompletionListener(this);
        this.setOnErrorListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.seekTo(1);
            mediaPlayer.setVolume(0f, 0f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d("VideoCheck", e.toString());
        }
    }
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.seekTo(1);
            mediaPlayer.setVolume(0f, 0f);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d("VideoCheck", e.toString());
        }
    }


    public void mute() {
        this.setVolume(0);
    }

    public void unmute() {
        this.setVolume(100);
    }

    private void setVolume(int amount) {
        final int max = 100;
        final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(max)));
        this.mediaPlayer.setVolume(volume, volume);
    }
}
