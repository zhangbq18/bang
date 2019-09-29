package com.zhangbq.testlib.wxcopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * author : zhangbq on 2019/9/26 17:11
 * description :
 * msg_061832 012919 1a7e6bb 1e1c100
 */

public class ExportVideo {
    static LinkedList<File> list = new LinkedList();

    //保存所有pdf文件的对象
    static LinkedList<File> amrList = new LinkedList<File>();
    public static void main(String args[]){
        //1.读取磁盘文件c
        //2.遍历F:\zhangbq\wxDir\voice2\目录文件，将.amr扩展名的文件复制到F:\zhangbq\wxDir\voices
        saveFile();
        System.out.println("总共语音文件个数： " + amrList.size());
//        for (File file : amrList) {//得到所有的
//            copyAmr(file);
//        }
        //时间过滤的
        filterAmr();


    }

    private static void filterAmr() {
        for (File file : amrList) {
            String fileName = file.getName();
            String timeStr = fileName.substring(10, 16);//六位时间码
            StringBuilder sb = new StringBuilder();
            sb.append(timeStr.substring(4));
            sb.append(timeStr.substring(0,2));
            sb.append(timeStr.substring(2,4));
            String userName = fileName.substring(16, 23);//用户名称
            if (userName.equals("1a7e6bb")) {//宇哥音频
                copyAmr2(file, sb.toString());
            }
        }
    }

    public static void saveFile(){
        File file = new File("F:\\zhangbq\\wxDir\\voice2");
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

    private static void copyAmr(File file) {
        String path = file.getAbsolutePath();
        if(path.endsWith(".amr")){
            //copy文件到指定目录中
            System.out.println(path);
            try {
                String targetPath = "F:\\zhangbq\\wxDir\\voices";
                File targetFile = new File(targetPath);
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                File saveFile = new File(targetPath + File.separator +file.getName());
                copy(file, saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void copyAmr2(File file, String name) {
        String path = file.getAbsolutePath();
        if(path.endsWith(".amr")){
            //copy文件到指定目录中
            System.out.println(path);
            try {
                String targetPath = "F:\\zhangbq\\wxDir\\voicesfilter2";
                File targetFile = new File(targetPath);
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                File saveFile = new File(targetPath + File.separator +name + ".amr");
                copy(file, saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copy(final File source, final File target) throws IOException {
        if (!source.exists()) {
            return;
        }
        final long length = source.length();
        final int bufsize = adjust((int) length, 1024, 512 * 1024);

        final byte[] buf = new byte[bufsize];
        int l = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            ins = new FileInputStream(source);
            outs = new FileOutputStream(target);
            for (l = ins.read(buf); l > -1; l = ins.read(buf)) {
                outs.write(buf, 0, l);
            }
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (final IOException ex) {
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (final IOException ex) {
                }
            }
        }
    }

    private static int adjust(final int value, final int min, final int max) {
        return Math.min(Math.max(min, value), max);
    }
}
