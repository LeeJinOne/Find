package com.find.find;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017-06-08.
 */

public class JinoneHttpTask extends AsyncTask<String, Void, String> {
    private Handler handler = null;
    private String flag = "";

    public JinoneHttpTask(Handler han){
        this.handler = han;
    }

    // Background에서 작업을 진행한다
    @Override
    protected String doInBackground(String... params) {
        String resultStr = "";
        HttpURLConnection conn = null;
        try{
            this.flag = params[0];
            String urlString = "http://10.0.2.2/" + this.flag;
            // 안드로이드 에뮬레이터와 서버가 같은 컴퓨터에서 동작하는 경우, ip주소를 10.0.2.2로 맞춰준다.
            URL url = new URL(urlString);

            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(false);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(3000);

            conn.setRequestMethod("POST");

            StringBuffer param = new StringBuffer("");

            param.append(params[1]);
            PrintWriter output = new PrintWriter(conn.getOutputStream());
            output.print(param.toString());
            output.close();

            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (;;) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "\n");
                br.close();
                conn.disconnect();
                br = null;
                conn = null;

                resultStr = sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultStr;
    }

    // Background 작업이 끝난 후 UI 작업을 진행한다
    protected void onPostExecute(String result) {
        Message message = new Message();
        message.obj = (this.flag + "|" + result.trim());
        this.handler.sendMessage(message);
    }
}
