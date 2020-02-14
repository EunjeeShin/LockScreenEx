package com.devej.lockscreenex;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BoundServActivity extends AppCompatActivity {

    LockService mServ;
    Intent serviceIntent;
    boolean mIsBound;
    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mServ= ((LockService.MyBinder)iBinder).getService();
            //mIsBound= true;
            if(mServ!= null){
                Log.d("LogTestService", "Service connected");
            }
            //위와 동일한 코드
//            LockService.MyBinder binder= (LockService.MyBinder) iBinder;
//            mServ= binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //mServ= null;
            mIsBound=false;
        }
    };

    public void toBindService(){
        serviceIntent= new Intent(this, LockService.class);
        bindService(serviceIntent, serviceCon, Context.BIND_AUTO_CREATE);
        mIsBound= true;
        //서비스 바인딩
    }

    public void toUnbindService(){
        unbindService(serviceCon);
        mIsBound= false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toBindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toUnbindService();
    }

    public LockService getmServ(){
        return this.mServ;
    }

}
