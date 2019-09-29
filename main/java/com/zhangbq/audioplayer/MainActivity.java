package com.zhangbq.audioplayer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAudio();
            }
        });

    }
    private void startAudio() {
        String savePath = getApplicationContext().getFilesDir() + "/assets_music/";
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();
        //执行本地文件拷贝
        copyFilesFromAssets(this, "music", savePath);
        AudioPlayUtils audioPlayUtils = new AudioPlayUtils();
       // /data/user/0/com.zhangbq.audioplayer/files/assets_music//181222.amr.pcm
        audioPlayUtils.setDataSource("", savePath + "1812011.pcm");
        audioPlayUtils.playAudio();

    }

    public static void copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
