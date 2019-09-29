package com.zhangbq.testlib.wxcopy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * author : zhangbq on 2019/9/27 12:07
 * description :
 */

public class AudioDecoder {
    static LinkedList<File> amrList = new LinkedList<File>();
    static LinkedList<File> list = new LinkedList();
    public static void main(String[] args) {
        String audioType = "amr";//源音频文件类型，amr、aud、slk、silk
        String source = "E:\\audio\\audio." + audioType;//源音频文件
        String target = "F:\\zhangbq\\wxDir\\voicemp3";//目标音频文件
        String path = "F:\\zhangbq\\wxDir\\tools\\audio";//音频转换工具存放路径
        String pcmPath = "F:\\zhangbq\\wxDir\\voicemp4";
        String realPcmPath;
        StringBuilder inputStr = new StringBuilder();
        saveFile();
        for (File file : amrList) {//得到所有的
            realPcmPath = pcmPath + File.separator +file.getName().substring(0, file.getName().indexOf("."))+"1.pcm";
//            processPcm(path, file.getAbsolutePath(), realPcmPath);//先转成pcm格式


            File filePcm = new File(realPcmPath);
//            inputStr.append("file '")
//                    .append(realPcmPath)
//                    .append("'\n");

            if(filePcm.exists()) {
                processMp3(path, realPcmPath, target + File.separator + file.getName().substring(0, file.getName().indexOf("."))+ "2.mp3");//再由pcm转成mp3
            }
//            //将pcm删除
//            File deFile = new File(realPcmPath);
//            deFile.deleteOnExit();
        }
//        writeTxtFile(inputStr.toString());
//        //单个测试用
//        File file = amrList.get(1);
//        pcmPath = pcmPath + File.separator +file.getName()+".pcm";
//        processPcm(path, file.getAbsolutePath(), pcmPath);//先转成pcm格式
//        File filePcm = new File(pcmPath);
//        if(filePcm.exists()) {
//            processMp3(path, pcmPath, target + File.separator + file.getName().substring(0, file.getName().indexOf("."))+ ".wav");//再由pcm转成mp3
//        }

//        processFfmpegMp3(path, file.getAbsolutePath(), target + File.separator + file.getName()+ ".mp3");
//        File sourceFile = new File(source);
//        if(sourceFile.exists()) {
//            String pcmPath = path + "audio.pcm";
//            processFfmpegMp3(path, source, target);//直接转成mp3
////            processPcm(path, source, pcmPath);//先转成pcm格式
////            File file=new File(pcmPath);
////            if(file.exists()) {
////                processMp3(path, pcmPath, target);//再由pcm转成mp3
////            } else {
////                processFfmpegMp3(path, source, target);//直接转成mp3
////            }
//        } else {
//            System.out.println("源文件不存在");
//        }

    }
    static String pcmPath = "F:\\zhangbq\\wxDir\\voicemp4\\input.txt";
    private static void writeTxtFile(String str){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(pcmPath, false);
            // 创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出
            bw.write(str); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e1) {
            }
        }
    }
    public static void saveFile(){
        File file = new File("F:\\zhangbq\\wxDir\\voicesfilter2");
        if (!file.exists()) {
            System.out.println("文件不存在!");
            return;
        }

        if (file.listFiles() == null) {
            amrList.add(file);
        } else {
            list.addAll(Arrays.asList(file.listFiles()));
            //遍历该队列
            while (!list.isEmpty()) {

                File firstF = list.removeFirst();

                //这里不论是文件夹还是文件，只需判断是否以“.pdf”结尾
                if(firstF.getAbsolutePath().endsWith(".amr"))
                    amrList.add(firstF);

                File[] files = firstF.listFiles();

                if (null == files) {
                    continue;
                }
                for (File f : files) {
                    if (f.isDirectory()) {
                        list.add(f);
                    } else {
                        if(f.getAbsolutePath().endsWith(".amr"))
                            amrList.add(f);

                    }
                }
            }
        }

    }

    // 调用sile_v3_decoder.exe，转成pcm格式
    private static void processPcm(String path, String source, String pcmPath){
        List<String> commend = new ArrayList<String>();
        commend.add(path+File.separator+"silk_v3_decoder.exe");
        commend.add(source);
        commend.add(pcmPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p=builder.start();
//            p=Runtime.getRuntime().exec("taskkill -f -t -im silk_v3_decoder.exe");
//            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用ffmpeg，pcm转mp3
    private static void processMp3(String path, String pcmPath, String target) {
        //ffmpeg -y -f s16le -ar 24000 -ac 1 -i 源文件 目标文件
        List<String> commend = new ArrayList<String>();
        commend.add(path+File.separator+"ffmpeg.exe");
//        commend.add("-y");
//        commend.add("-f");
//        commend.add("s16le");
//        commend.add("-ar");
//        commend.add("11025");
//        commend.add("-ac");
//        commend.add("2");
//        commend.add("-i");
//        commend.add(pcmPath);
//        commend.add(target);

//        ffmpeg -f s16be -ar 8000 -ac 2 -acodec pcm_s16be -i input.raw output.wav
//        -y	允许覆盖
//                -f s16le	强制文件格式
//        -ac 2	双声道
//                -ar 16000	采样率
//                -acodec pcm_s16le	编码器
//        -i test.mp3
                commend.add("-y");
                commend.add("-f");
                commend.add("s16le");
                commend.add("-ar");
                commend.add("11025");
                commend.add("-ac");
                commend.add("2");
                commend.add("-acodec");
                commend.add("pcm_s16be");
                commend.add("-i");
                commend.add(pcmPath);
                commend.add(target);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p=builder.start();
//            p=Runtime.getRuntime().exec("taskkill -f -t -im ffmpeg.exe");
//            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用ffmpeg.exe，转MP3
    private static void processFfmpegMp3(String path, String source, String target){
        List<String> commend = new ArrayList<String>();
        commend.add(path+File.separator +"ffmpeg.exe");
        commend.add("-i");
        commend.add(source);
        commend.add(target);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p=builder.start();
            p=Runtime.getRuntime().exec("taskkill -f -t -im ffmpeg.exe");
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
