package com.devej.lockscreenex;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;


public class LockService extends Service {

    private final IBinder mBinder= new MyBinder();
    BroadcastReceiver lockReceiver;
    Boolean timerCheck= false;
    //여기서 timerCheck 선언하는거랑 onCreate에서 선언하는게 다른가??

    int runningTime, setTime;
    Handler handler;
    Thread stopwatch, timer;
    LockScreenActivity screen;


    class MyBinder extends Binder {
        LockService getService(){
            return LockService.this;
        }
    }

    public LockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LogTestService", "LockService onCreate");
        handler= new Handler(Looper.getMainLooper());
        screen= new LockScreenActivity();
        lockReceiver= new BroadcastReceiver() {

            public static final String ScreenOff= "android.intent.action.SCREEN_OFF";
            public static final String ScreenOn= "android.intent.action.SCREEN_ON";
            public static final String StopWatchOff= "com.devej.lockscreenex.STOPWATCH_OFF";
            public static final String TimerOn="com.devej.lockscreenex.TIMER_ON";
            public static final String TimerOff="com.devej.lockscreenex.TIMER_OFF";

            @Override
            public void onReceive(Context context, Intent intent) {
                String action= intent.getAction();

                if(action.equals(ScreenOff)){
                    Log.d("LogTestService", "ScreenOff Action detected");
                    if(!timerCheck){
                        Intent activityIntent= new Intent(getApplicationContext(), LockScreenActivity.class);
                        activityIntent.putExtra("timerState", timerCheck);
                        Log.d("LogTestService", "LockScreen started; timerState: "+timerCheck);
                        //액티비티에서 어떤 메소드를 호출시킬지 타이머 상태 데이터 전달
                        startActivity(activityIntent);
                    }else{
                        //timer가 돌아가고 있으면 작동하지 않음
                        Log.d("LodTestService", "timerState: "+timerCheck);
                    }

                }else if(action.equals(ScreenOn)){
                    Log.d("LogTestService", "ScreenOn Action detected");

                }else if(action.equals(StopWatchOff)){
                    Log.d("LogTestService", "StopwatchOff Action detected");
                    stopStopwatch();
                }else if(action.equals(TimerOn)){
                    Log.d("LogTestService", "TimerOn Action detected");
                    //타이머 작동 구현
                    timerCheck= true;
                    setTime= intent.getIntExtra("setTime", 0);
                    //얼마나 타이머 작동시킬지 데이터 받아옴
                    Intent activityIntent= new Intent(getApplicationContext(), LockScreenActivity.class);
                    activityIntent.putExtra("timerState", timerCheck);
                    Log.d("LogTestService", "LockScreen started; timerState: "+timerCheck+" and setTime: "+setTime);
                    startActivity(activityIntent);
                }else if(action.equals(TimerOff)){
                    timerCheck= false;
                    Log.d("LogTestService", "TimerOff Action detected");
                    stopTimer();
                }
            }
        };
        IntentFilter filter= new IntentFilter();
        filter.addAction("com.devej.lockscreenex.TIMER_ON");
        filter.addAction("com.devej.lockscreenex.TIMER_OFF");
        filter.addAction("com.devej.lockscreenex.STOPWATCH_OFF");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("LogTestService", "LockService onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LogTestService", "LockService onStart");


        return super.onStartCommand(intent, flags, startId);
    }

    public void startStopwatch(){
        stopwatch= new Thread(new Runnable() {
            @Override
            public void run() {
//                LockScreenActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
                //서비스에서 다른 액티비티는 직접 접근을 못하나
                runningTime= 0;
                String time;
                while (!stopwatch.isInterrupted()){
                    try {
                        Log.d("LogTestService", "stopwatch thread begins");
                        Thread.sleep(1000);
                        runningTime++;
                        Log.d("LogTestService", "running time "+runningTime);
                        //time= calculateTime(runningTime, false);
                        Intent actionIntent= new Intent("com.devej.lockscreenex.SET_TIMER");
                        //actionIntent.putExtra("timerText", time);
                        //actionIntent.putExtra("timerText", "running time: "+runningTime);
                        sendBroadcast(actionIntent);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                screen.timer.setText(""+runningTime);
//                                //일단 임시로 runningTime 표기
//                            }
//                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("LogTestService", "stopwatch interrupt detected");
                        return;
                    }
                }

            }
        });
        stopwatch.start();
    }

    public void stopStopwatch(){
        stopwatch.interrupt();
        Log.d("LogTestService", "stopwatch stopped");
        saveTime(runningTime);
    }

    public void saveTime(int time){
        //DB 저장 구현 필요
    }

    public void startTimer(){
        Log.d("LogTestService", "timer started");

    }

    public void pauseTimer(int runningTime){

    }

    public void stopTimer(){
        Log.d("LogTestService", "timer stopped");
    }

    public String calculateTime(int runningTime, Boolean timerCheck){
        int hour= 0, min= 0, sec= 0;
        if(runningTime % 60 == 0){
            if(runningTime==0){
                //러닝타임 0초
                min= 0;
                sec= 0;
            }else{

            }
        }
        return hour+" : "+min+" : "+sec;
    }

    public void finishTimer(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LogTestService", "LockService onDestroy");
        unregisterReceiver(lockReceiver);
    }
}
