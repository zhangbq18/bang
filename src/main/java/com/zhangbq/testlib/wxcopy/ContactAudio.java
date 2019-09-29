package com.zhangbq.testlib.wxcopy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author : zhangbq on 2019/9/27 19:02
 * description :
 */

public class ContactAudio {
    static String path = "F:\\zhangbq\\wxDir\\tools\\audio";//音频转换工具存放路径
    static String pcmPath = "F:\\zhangbq\\wxDir\\voicemp4\\input.txt";
    static String targetPath = "F:\\zhangbq\\wxDir\\voicemp4\\out.pcm";
    public static void main(String []args){
        //ffmpeg -f concat -i input.txt put.mp4

        contact(path, pcmPath, targetPath);
    }

    // 调用ffmpeg，pcm转mp3
    private static void contact(String path, String pcmPath, String target) {
        //ffmpeg -y -f s16le -ar 24000 -ac 1 -i 源文件 目标文件-c copy output.mkv
        //ffmpeg -i input1.wav -i input2.wav -filter_complex '[0:0][1:0]concat=n=2:v=0:a=1[out]' -map '[out]' output.wav
        List<String> commend = new ArrayList<String>();
        commend.add(path+File.separator+"ffmpeg.exe");
//        commend.add("-f");
        commend.add("-i");
        commend.add("F:\\zhangbq\\wxDir\\voicemp4\\input\\1812161.pcm");
        commend.add("F:\\zhangbq\\wxDir\\voicemp4\\input\\1812171.pcm");
        commend.add("-filter_complex '[0:0][1:0]concat=n=2:v=0:a=1[out]' -map '[out]'");
        //        commend.add(pcmPath);
//        commend.add("-acodec");
//        commend.add("copy");
        commend.add(target);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p=builder.start();
//            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
