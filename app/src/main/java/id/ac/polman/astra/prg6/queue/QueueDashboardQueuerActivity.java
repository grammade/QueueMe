package id.ac.polman.astra.prg6.queue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import id.ac.polman.astra.prg6.queue.architecture.model.model.Queuer;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;
import id.ac.polman.astra.prg6.queue.fragments.QueueOverviewFragment;
import id.ac.polman.astra.prg6.queue.utils.DialogYourTurn;

public class QueueDashboardQueuerActivity extends AppCompatActivity
    implements QueueOverviewFragment.QueueOverviewCallbacks,
        DialogYourTurn.Callbacks {
    public static final String TAG = "ActivityQueuerDashboard";

    private QueueViewModel mViewModel;
    private FragmentManager fm;
    private Fragment mFragment;

    private Button mBtnExit;
    private TextView mQueuerNumber;

    private LiveData<Queuer> mQueuerLiveData;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_dashboard_queuer);

        //ACTIVITY A
        mViewModel = new ViewModelProvider(this).get(QueueViewModel.class);

        fm = getSupportFragmentManager();
        sp = getSharedPreferences("queue",MODE_PRIVATE);

        mBtnExit = findViewById(R.id.btnExit);
        mQueuerNumber = findViewById(R.id.txtQueuerNumber);
        mQueuerNumber.setText("-1");

        initFragment();
        initQueuerNumber();
        initListener();
    }

    private void initQueuerNumber() {
        String key = sp.getString("key", null);
        final String queuerKey = sp.getString("queuerKey", null);
        mViewModel.getQueuerNumber(key, queuerKey)
                .observe(this, new Observer<Queuer>() {
                    @Override
                    public void onChanged(Queuer queuer) {
                        Log.d(TAG, "onChanged: QueuerChanged: "+queuer);
                        if(queuer!=null){
                            mQueuerNumber.setText(String.valueOf(queuer.getUserNumber()));
                            mViewModel.setQueuerNumber(queuer.getUserNumber());
                            sp.edit().putInt("queuerNumber", queuer.getUserNumber()).apply();
                        }
                    }
                });
    }

    private void initFragment() {
        if(mFragment==null){
            mFragment = QueueOverviewFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container,mFragment)
                    .commit();
        }
    }

    private void initListener() {
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = sp.getString("key", null);
                String queuerKey = sp.getString("queuerKey", null);
                sp.edit().clear().apply();
                mViewModel.exitQueue(key, queuerKey);
                goMainMenu();
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

    @Override
    public void dismiss() {
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().clear().apply();
                goMainMenu();
            }
        });
    }
}