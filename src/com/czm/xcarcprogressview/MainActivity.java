
package com.czm.xcarcprogressview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    XCArcProgressBar arcProgressBar ;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xc_arc_progressbar_demo);
        arcProgressBar = (XCArcProgressBar) findViewById(R.id.arcProgressBar);

        arcProgressBar.setMax(400);
        arcProgressBar.setTitle("内存");
        button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(new OnClickListener() {
            

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new MyAsyncTask().execute(0);
            }
        });
        
        new MyAsyncTask().execute(0);
        
    }
    
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //arcProgressBar.setProgress(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            Log.v("czm", ""+values[0]);
            arcProgressBar.setProgress((int)(values[0]));

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Integer timer = 0;
            boolean flag = false;
            while(timer <= 45 * 4 ){
                try {
                    publishProgress(timer);
                    timer ++;
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            while(timer > 25 * 4 ){
                try {
                    publishProgress(timer);
                    timer --;
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }
         
    }

}
