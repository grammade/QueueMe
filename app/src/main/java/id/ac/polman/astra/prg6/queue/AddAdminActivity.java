package id.ac.polman.astra.prg6.queue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.CreateQueueViewModel;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;

public class AddAdminActivity extends AppCompatActivity {
    private static final String TAG = "AddAdminActity";
    private QueueViewModel mViewModel;

    private EditText mEmail;
    private Button mBtnAddAdmin, mBtnCancel;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_admin);
        mViewModel = new ViewModelProvider(this).get(QueueViewModel.class);

        mEmail = findViewById(R.id.txtEmail);
        mBtnAddAdmin = findViewById(R.id.btnAdd_AddAdmin);
        mBtnCancel = findViewById(R.id.btnCancel_AddAdmin);
        mProgressBar = findViewById(R.id.progressBar);

        initListener();
    }

    private void initListener(){
        mBtnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
                mViewModel.addAdmin(mEmail.getText().toString().trim(), sp.getString("key", null));
                goDashboardAdmin();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QueueOptionActivity.class);
                startActivity(i);
            }
        });
    }
    public void goDashboardAdmin(){
        Intent i;
        i = new Intent(getApplicationContext(), QueueDashboardAdminActivity.class);
        finish();
        startActivity(i);
    }

}
