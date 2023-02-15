package id.ac.polman.astra.prg6.queue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;
import id.ac.polman.astra.prg6.queue.fragments.QueueOverviewFragment;

public class QueueDashboardAdminActivity extends AppCompatActivity
    implements QueueOverviewFragment.QueueOverviewCallbacks {
    public static final String TAG = "ActivityQueueDashboard";

    private QueueViewModel mViewModel;
    private FragmentManager fm;
    private Fragment mFragment;

    private Button mBtnNext, mBtnOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_dashboard_admin);

        mViewModel = new ViewModelProvider(this).get(QueueViewModel.class);
        fm = getSupportFragmentManager();

        mBtnNext = findViewById(R.id.btnNext);
        mBtnOption = findViewById(R.id.btnQueueOption);

        initFragment();
        initListener();
    }

    private void initFragment(){
        if(mFragment==null){
            mFragment = QueueOverviewFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container,mFragment)
                    .commit();
        }
    }

    private void initListener(){
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.nextQueue(getSharedPreferences("queue",MODE_PRIVATE).getString("key",null));
            }
        });
        mBtnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QueueOptionActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void goMainMenu() {
        Intent i = new Intent(this, MainMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
