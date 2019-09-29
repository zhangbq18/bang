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
    static String pcmPath = "F:\\zhangbq\\wxDir\\voicemp3\\input.txt";
    static String targetPath = "F:\\zhangbq\\wxDir\\voicemp3\\out5.mp3";
    static String sample1 = "F:\\zhangbq\\wxDir\\voicemp3\\1811282.mp3";
    static String sample2 = "F:\\zhangbq\\wxDir\\voicemp3\\1811292.mp3";
    public static void main(String []args){
        //ffmpeg -f concat -i input.txt put.mp4

        contact(path, pcmPath, targetPath);
    }

    // 调用ffmpeg，pcm转mp3
    private static void contact(String path, String pcmPath, String target) {
        //ffmpeg -y -f s16le -ar 24000 -ac 1 -i 源文件 目标文件-c copy output.mkv
       // -filter_complex '[0:0] [1:0] concat=n=2:v=0:a=1 [a]' -map [a] j5.mp3
        //两种方法：1.直接在命令行执行 shift+右键  执行命令 .\ffmpeg -f concat -safe 0  -i list.txt -c copy "out.mp3" ok.
        //2.用此方法(经验：调此类方法时候先在命令行中执行，能看到报错日志，如果有用户的提示选择yes或no，则说明文件已存在，先删除已存在文件，再用方法调。)
        List<String> commend = new ArrayList<String>();
        commend.add(path+File.separator+"ffmpeg.exe");
        commend.add("-f");
        commend.add("concat");
        commend.add("-safe");
        commend.add("0");
        commend.add("-i");
        commend.add(pcmPath);
        commend.add("-c");
        commend.add("copy");
        commend.add(target);


//        commend.add(path + File.separator + "ffmpeg.exe");
//        commend.add("-i");
//        commend.add(sample1);
//        commend.add("-i");
//        commend.add(sample2);
//        commend.add("-filter_complex '[0:0] [1:0] concat=n=2:v=0:a=1 [a]' -map [a]");
//        commend.add(target);
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
