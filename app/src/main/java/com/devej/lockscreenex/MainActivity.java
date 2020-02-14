package com.devej.lockscreenex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BoundServActivity {

    Button btnLock, btnService;
    int setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(serviceIntent);

        //시간 선택에서 전달해줄 수 있는 기능 필요
        //TimePicker쓰던가 수연이랑 상의해서 커스텀으로 넣기
        //일단 임시로 30초만 넘겨주기로 함

        btnLock=(Button)findViewById(R.id.btnlock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actionIntent= new Intent("com.devej.lockscreenex.TIMER_ON");
                actionIntent.putExtra("setTime", 30);
                //setTime 설정해서 넘겨주면 됨
                //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(actionIntent);
                sendBroadcast(actionIntent);
                Log.d("LogTestBroadcast","Timer Broadcasted");
                //버튼을 누르면 액티비티를 여는게 아니라 서비스에 브로드캐스팅해서 서비스에서 액티비티 열어주는거
            }
        });
        btnService= (Button)findViewById(R.id.btnservice);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnService.getText().toString().equals("서비스 중지")){
                    toUnbindService();
                    stopService(serviceIntent);
                    btnService.setText("서비스 재개");
                }else if(btnService.getText().toString().equals("서비스 재개")){
                    startService(serviceIntent);
                    toBindService();
                    btnService.setText("서비스 중지");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
