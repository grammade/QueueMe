package id.ac.polman.astra.prg6.queue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.ac.polman.astra.prg6.queue.architecture.repositories.QueueRepository;

public class QueueOptionActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ActivityQueueOption";

    private Button btnShowQR;
    private Button btnAddAdmin;
    private Button btnTerminateQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_option);

        btnShowQR = findViewById(R.id.btnShowQR);
        btnTerminateQueue = findViewById(R.id.btnTerminateQueue);
        btnAddAdmin = findViewById(R.id.btnAddAdmin);

        initListener();
    }

    private void initListener() {
        btnShowQR.setOnClickListener(this);
        btnTerminateQueue.setOnClickListener(this);
        btnAddAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.btnShowQR:
                i = new Intent(getApplicationContext(), QRActivity.class);
                startActivity(i);
                break;
            case R.id.btnAddAdmin:
                i = new Intent(getApplicationContext(), AddAdminActivity.class);
                startActivity(i);
                break;
            case R.id.btnTerminateQueue:
                SharedPreferences sp = getSharedPreferences("queue",MODE_PRIVATE);
                String key = sp.getString("key",null);

                if(key!=null){
                    sp.edit().clear().apply();
                    QueueRepository.getInstance().removeQueue(key);
                }
                i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);
                break;
        }
    }
}