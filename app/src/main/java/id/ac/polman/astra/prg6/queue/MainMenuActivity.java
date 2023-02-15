package id.ac.polman.astra.prg6.queue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import id.ac.polman.astra.prg6.queue.fragments.CreateQueueFragment;
import id.ac.polman.astra.prg6.queue.fragments.EnterQueueFragment;
import id.ac.polman.astra.prg6.queue.fragments.MainMenuFragment;

public class MainMenuActivity extends AppCompatActivity implements
        MainMenuFragment.MainMenuFragmentCallbacks,
        CreateQueueFragment.CreateQueueFragmentCallbacks,
        EnterQueueFragment.Callbacks {
    private static final String ARG_NAME = "arg_name";
    private static final String TAG = "MainMenuActivity";

    FragmentManager fm;
    Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);

        fm = getSupportFragmentManager();
        Log.d(TAG, "onCreate: ADMIN KEY: " + sp.getString("key", "NON"));
        Log.d(TAG, "onCreate: QUEUER KEY: " + sp.getString("queuerKey", "NON"));

        if (fm.getFragments().size() <= 0)
            if (sp.getString("queuerKey", null) != null) { //check if user is a queuer
                Log.d(TAG, "onCreate: USER IS QUEUER");
                goQueueDashboardQueuer();
            } else if (sp.getString("key", null) != null) { //check if user is an admin of a queue
                Log.d(TAG, "onCreate: USER IS ADMIN");
                goQueueDashboard();
            } else {
                Log.d(TAG, "onCreate: USER NO KEY");
                initFragment();
            }
    }

    private void initFragment() {
        if (mFragment == null) {
            mFragment = MainMenuFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container_main_menu, mFragment)
                    .commit();
        }
    }

    @Override
    public void goQueueDashboard() {
        Intent i = new Intent(this, QueueDashboardAdminActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void goCreateQueue() {
        mFragment = CreateQueueFragment.newInstance();
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_top, R.anim.slide_from_top, R.anim.slide_to_bottom)
                .replace(R.id.fragment_container_main_menu, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goEnterQueue() {
        mFragment = new EnterQueueFragment();
        fm.beginTransaction()
                .replace(R.id.fragment_container_main_menu, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goQueueDashboardQueuer() {
        Intent i = new Intent(this, QueueDashboardQueuerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void doLogout() {

    }

    @Override
    public void onBackPressed() {
        int count = fm.getBackStackEntryCount();
        if (count == 0)
            super.onBackPressed();
        else {
            fm.popBackStack();
        }
    }
}
