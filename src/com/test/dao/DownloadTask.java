package com.test.dao;


import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<String, Double, String> {

    private DownloadTaskListener taskListener;

    public interface DownloadTaskListener {
        public void onProgressUpdate(DownloadTask task, Double progress);
        public void onFinishTask(DownloadTask task, String path);
    }

    public DownloadTaskListener getTaskListener() {
        return taskListener;
    }

    public void setTaskListener(DownloadTaskListener taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            URL url = new URL(params[0]);
            String filePath = "";
//            String filePath = Globals_final.FILE_PATH_CACHE + HashUtil.md5(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.connect();
            int contentLength = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                Double progress = Double.valueOf(total) / Double.valueOf(contentLength);
                publishProgress(new Double[]{progress});
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return filePath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        if (taskListener != null) {
            taskListener.onFinishTask(this, result);
        }
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        // TODO Auto-generated method stub
        if (taskListener != null) {
            taskListener.onProgressUpdate(this, values[0]);
        }
    }
}
