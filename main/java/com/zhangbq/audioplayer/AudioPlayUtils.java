package com.zhangbq.audioplayer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/9.
 * 用于播放录音原始数据也就是PCM
 */

public class AudioPlayUtils implements IPlayComplete {
    private int sampleRateInHz = 11025;//44100、16000 pcm与mp3混音后的音频需要用44100播放，否则声音会很怪异
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private final int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位,保证设备支持
    private final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private AudioTrack audioTrack;
    private PlayAudioThread playAudioThread;
    private int mPrimePlaySize;
    private boolean mThreadExitFlag = false;
    private int mPlayOffset;
    private byte[] mData;
    private String srcPath;//mp3文件
    private String dstPath;//pic格式文件
    protected File file;

    public AudioPlayUtils() {

    }

    private void initAudioTrack() {
        if (srcPath != null){
            MediaExtractor extractor = new MediaExtractor();
            try {
                //设置需要MediaExtractor解析的文件的路径
                extractor.setDataSource(srcPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaFormat format = extractor.getTrackFormat(0);
            //判断当前帧的文件类型是否为audio
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                //获取当前帧的采样率
                extractor.selectTrack(0);
                sampleRateInHz = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            }
        }
        int minBufSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        mPrimePlaySize = minBufSize * 2;
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                minBufSize,
                AudioTrack.MODE_STREAM);
    }

    public void setDataSource(String filePath, String dstPath) {
        srcPath = filePath;
        if (TextUtils.isEmpty(filePath)) {
            srcPath = null;
        }
        this.dstPath = dstPath;
        mData = getPCMData(dstPath);
    }

    public byte[] getPCMData(String filePath) {

        file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        byte[] data_pack = null;
        long size = file.length();
        data_pack = new byte[(int) size];
        try {
            inStream.read(data_pack);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return data_pack;
    }



    private void startThread() {
        if (playAudioThread == null) {
            playAudioThread = new PlayAudioThread();
        }
        mThreadExitFlag = false;
        playAudioThread.start();
    }

    public AudioTrack getAudioTrackInstance() {
        return audioTrack;
    }
    private void releaseAudioTrack() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }

    }

    public void pauseAudio() {
        stopThread();
    }

    public void resumeAudio() {
        startThread();
    }

    public void stopAudio() {
        mPlayOffset = 0;
        stopThread();
    }

    private void stopThread() {
        if (playAudioThread != null) {
            mThreadExitFlag = true;
        }
    }

    public void playAudio() {
        initAudioTrack();
        if (mData == null) {
            return;
        }
        startThread();
    }

    @Override
    public void onPlayComplete() {

    }

    /**
     * 在界面销毁时调用
     */
    public void release() {
        stopThread();
        if (playAudioThread != null) {
            playAudioThread = null;
        }
        releaseAudioTrack();
    }

    private class PlayAudioThread extends Thread {
        @Override
        public void run() {
            audioTrack.play();
            while (true) {
                if (mThreadExitFlag) {
                    break;
                }
                try {
                    audioTrack.write(mData, mPlayOffset, mPrimePlaySize);
                    mPlayOffset += mPrimePlaySize;
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    onPlayComplete();
                    break;
                }
                if (mPlayOffset >= mData.length) {
                    onPlayComplete();
                    break;
                }
            }
            audioTrack.stop();
        }
    }
}
