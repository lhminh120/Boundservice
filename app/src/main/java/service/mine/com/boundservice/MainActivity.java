package service.mine.com.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//Đối với service thì ở on create thì nên dùng Thread hoặc Handler hoặc Asynctask
//Hệ thống có những service được viết sẵn chẳng hạn như Alarmmanger(hẹn giờ), check mạng, đánh thức ứng dụng (tin nhắn) (luôn chạy)

public class MainActivity extends AppCompatActivity {
    private Button btnStart, btnStop, btnDetail;
    private TextView tvStart;
    private CounterBoundService sv;
    private Handler hUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        tvStart = (TextView) findViewById(R.id.tvStart);
        btnDetail = (Button) findViewById(R.id.btnDetail);
        btnStart.setOnClickListener(onAction_click);
        btnStop.setOnClickListener(onAction_click);
        btnDetail.setOnClickListener(onAction_click);
        hUpdate = new Handler();


    }

    private View.OnClickListener onAction_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart:
                    startSV();
                    break;
                case R.id.btnStop:
                    stopSV();
                    break;
                case R.id.btnDetail:
                    Intent detail = new Intent(v.getContext(),DetailActivity.class);
                    startActivity(detail);
                    break;
            }

        }
    };

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

    private void stopSV() {
        Intent intentSV = new Intent(this,CounterBoundService.class);
        unbindService(myConnectionService);
    }
    private void Update(){
        hUpdate.postDelayed(display,1000);
    }
    private Runnable display = new Runnable() {
        @Override
        public void run() {
            //UI Thread
            tvStart.setText(sv.getCounter()+"");
            if(sv.getCounter()<=60){
                Update();
            }

        }
    };
}
