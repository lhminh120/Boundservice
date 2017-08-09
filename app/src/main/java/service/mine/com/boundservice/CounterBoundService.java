package service.mine.com.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by MyPC on 27/07/2017.
 */

public class CounterBoundService extends Service {
    private Handler handler;
    private int counter = 0;
    //binder này là 1 cầu nối giữa bound service và activity
    private IBinder myBinder = new doBinder();
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Một hành động nào đấy
        //Có thể là down nhạc hay data từ server
        handler = new Handler();
        count();
    }
    private void count(){
        handler.postDelayed(toDo,1000);
    }

    //Runnable chính là UI Thread hay Main Thread nhưng mà phải viết ngoài activity
    private Runnable toDo = new Runnable() {
        @Override
        public void run() {
            // công việc ở Main UI Thread
            counter++;
            Log.d("Count: ", counter + "");
            if(counter <= 100)
                count();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Có thể là một hành động nào đó từ activity gửi qua vào cái cái start này để xử lý
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Dừng việc xử lý
        //Có thể là dừng down hay 1 việc gì đó
        handler.removeCallbacks(toDo);
    }
    class doBinder extends Binder{
        public CounterBoundService getInstant_CounterBoundService(){
            return CounterBoundService.this;
        }
    }
}
