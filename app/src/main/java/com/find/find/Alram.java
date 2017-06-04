package com.find.find;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by louis on 2017-05-29.
 */

public class Alram extends Fragment{
    public Alram() {

    }
    private  static int timer;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    Calendar calendar = Calendar.getInstance();
    private long curTime = System.currentTimeMillis();
    int hour = (int) (curTime/3600);
    int minute = hour/60;
    int second =hour%60;
    SharedPreferences.Editor ePref;
    Boolean bool = true;
    TextView tv,fail;
    String text,Str;
    SharedPreferences prefs;
    int t;
    String pk,index,ppl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_alram, container, false);
        new AlramHATT(this.getActivity().getApplicationContext()).Timer();
        tv = (TextView)v.findViewById(R.id.tv);
        fail=(TextView)v.findViewById(R.id.fail);
        prefs = this.getActivity().getSharedPreferences("Save", MODE_PRIVATE);
        t = prefs.getInt("t",t);

        text = "30";
        t = Integer.parseInt(text);
        sum();
        Str = String.format("%02d시간 %02d분 %02d초", hour, minute, second);
        tv.setText(Str);
        bool = true;
        getDB();
        return v;
    }

    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    private void getDB(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref",MODE_PRIVATE);
        pk = pref.getString("pk",null);
        index=pref.getString("index",null);
        ppl=pref.getString("ppl",null);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(pk=="apc"||pk=="bpc"){
            text = "10";
        }
        else if(pk=="ccafe"||pk=="dcafe"){
            text = "20";
        }
        else if(pk=="ebillider"||pk=="fbillider"){
            text = "30";
        }
        else
            text = "40";
        t = Integer.parseInt(text);
        sum();
        Str = String.format("%02d시간 %02d분 %02d초", hour, minute, second);
        tv.setText(Str);


        bool = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


        ePref = prefs.edit();
        ePref.putInt("t",t);
        ePref.commit();
    }

    public class thread extends Thread{
        public void run(){
            while(bool){
                handler.sendEmptyMessage(0);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){}
            }
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what ==0){
                if(t>0){
                    Log.d("fureun","XD");
                    t--;
                    sum();
                    Str = String.format("%02d시간 %02d분 %02d초",hour,minute,second);
                    tv.setText(Str);

                    if(t==0){

                        bool=false;
                        Str ="Enough";
                        fail.setText(Str);
                    }
                }
            }
        }
    };
    public void sum(){
        hour =  t/3600;
        minute = (t%3600)/60;
        second = (t%3600)%60;
    }
    public class AlramHATT{
        private Context context;
        public AlramHATT(Context context){
            this.context=context;

        }

        public void Timer(){
            int delay=0;
            if(pk=="apc"||pk=="bpc"){
                delay=10;
            }
            else if(pk=="ccafe"||pk=="dcafe"){
                delay=20;
            }
            else if(pk=="ebillider"||pk=="fbillider"){
                delay=30;
            }
            else
                delay=40;
            AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), BroadcastT.class);
            PendingIntent sender = PendingIntent.getBroadcast(getActivity(),0,intent,0);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),hour, minute, second+delay);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }

}
