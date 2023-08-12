package com.example.dotconnect.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BitmapToFileConverter {

    private File file;
    private Context context;

    public BitmapToFileConverter(Context context) {
        this.context=context;
    }

    public File ConvertToFile(Bitmap bitmap,String file_name) throws InterruptedException {

        ExecutorService executorService= Executors.newSingleThreadExecutor();

        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
        tasks.add(Executors.callable(new Runnable() {
            @Override
            public void run() {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                file  = new File(context.getCacheDir(), file_name);
                try {
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }));
        executorService.invokeAll(tasks);
        Log.d("Retrofit", "onChanged: "+ System.currentTimeMillis()/1000);
        return file;
    }
}
