package service.mine.com.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView tvRes;
    private CounterBoundService sv;
    private Handler hUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvRes = (TextView) findViewById(R.id.tvRes);
        hUpdate = new Handler();
        //Bindservice ở dây, vẫn phải có service connection ở đây để lấy dữ liệu từ service truyền ra
        //Vẫn phải dùng handler và đệ quy để update dữ liệu lên view thường xuyên
        startSV();
    }
    private void startSV() {
        Intent intentSV = new Intent(this,CounterBoundService.class);
        bindService(intentSV, myConnectionService, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection myConnectionService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Nơi nhận dữ liệu từ service xuất ra từ UI
            CounterBoundService.doBinder binder = (CounterBoundService.doBinder) service;
            //Lấy được Instant của bound service
            sv = binder.getInstant_CounterBoundService();
            //Lấy dữ liệu của nó đẩy lên màn hình
            Update();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void Update(){
        hUpdate.postDelayed(display,1000);
    }
    private Runnable display = new Runnable() {
        @Override
        public void run() {
            //UI Thread
            tvRes.setText(sv.getCounter()+"");
            if(sv.getCounter()<=60){
                Update();
            }

        }
    };
}
