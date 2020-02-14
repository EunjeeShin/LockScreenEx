package com.devej.lockscreenex;

import android.os.AsyncTask;

public class TimerTask extends AsyncTask<Integer, Integer, Integer> {

    int runningTime= 0;     //진행된 시간 표시
    int setTime= 0;


    public TimerTask(int setTime) {
        //Timer기능.
        this.setTime = setTime;
    }

    public TimerTask() {
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
