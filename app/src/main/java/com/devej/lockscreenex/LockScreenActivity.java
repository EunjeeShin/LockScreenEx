package com.devej.lockscreenex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenActivity extends BoundServActivity {

    TextView timer;
    Button exitBtn;
    Boolean timerState;
    BroadcastReceiver timerReceiver;
    Chronometer chTimer;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        Log.d("LogTestActivity", "LockScreen onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        timer=(TextView)findViewById(R.id.timerText);
        exitBtn=(Button)findViewById(R.id.exitbtn);
//        chTimer=(Chronometer)findViewById(R.id.chronometer);
//        chTimer.setFormat("%s:%s:%s");
        //time= new String.format("%d: %d: %d");

        Intent gotIntent=getIntent();
        timerState= gotIntent.getBooleanExtra("timerState", false);
        if(timerState){
            //timerState==true; 타이머 작동시킴
//            while(mServ==null){
//                Log.d("LogTestService", "waiting for the connection");
//            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                            Thread.sleep(300);
                            //Log.d("LogTestActivity", "연결 기다리는중");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LockScreenActivity.this.mServ.startTimer();
//                    chTimer.setBase(0);
//                    chTimer.setCountDown();
                    Log.d("LogTestService", "mServ is "+mServ);
                }
            }).start();
        }else{
            //timerState==false; 타이머 작동 안함
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        //Log.d("LogTestActivity", "연결 기다리는중");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LockScreenActivity.this.mServ.startStopwatch();
                    Log.d("LogTestService", "mServ is "+mServ);
                }
            }).start();
        }

        timerReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action= intent.getAction();
                if(action.equals("com.devej.lockscreenex.SET_TIMER")){
                    Log.d("LogTestActivity", "running time in String "+intent.getStringExtra("timerText"));
                    //timer.setText(intent.getStringExtra("timerText"));
                }
            }
        };
        IntentFilter filter= new IntentFilter();
        filter.addAction("com.devej.lockscreenex.SET_TIMER");
        registerReceiver(timerReceiver, filter);


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timerState){
                    //timerState==true; 타이머 작동중
                    //스톱워치와 다르게 정말로 종료할 것인지 물어봐야 함
                    //예를 들어 코인 소비 등
                    Intent actionIntent= new Intent("com.devej.lockscreenex.TIMER_OFF");
                    //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(actionIntent);
                    sendBroadcast(actionIntent);
                    //mServ.stopTimer();
                    finish();
                }else{
                    //timerState==false; 타이머 작동x 스톱워치 작동중
                    Intent actionIntent= new Intent("com.devej.lockscreenex.STOPWATCH_OFF");
                    //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(actionIntent);
                    sendBroadcast(actionIntent);
                    //mServ.stopStopwatch();
                    finish();
                }
            }
        });
        //전화왔을 때 타이머 pause기능 필요; 나중에 구현할 것
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("LogTestActivity", "BackPressed called");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d("LogTestActivity", "onUserLeaveHint called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timerReceiver);
    }
}
