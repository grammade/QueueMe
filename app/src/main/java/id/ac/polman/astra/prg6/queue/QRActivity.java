package id.ac.polman.astra.prg6.queue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QRViewModel;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;

public class QRActivity extends AppCompatActivity {

    private ImageView mImageViewQR;
    private ProgressBar mProgressBar;
    private TextView mTxtCurrent;
    private TextView mTxtLast;

    private SharedPreferences mSharedPreferencesQueue;

    private QRViewModel mViewModel;
    private LiveData<Queue> mQueueLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

        mViewModel = new ViewModelProvider(this).get(QRViewModel.class);

        mSharedPreferencesQueue = getSharedPreferences("queue", MODE_PRIVATE);

        mImageViewQR = findViewById(R.id.imageViewQR);
        mProgressBar = findViewById(R.id.progressBar);
        mTxtCurrent = findViewById(R.id.txtCurrentNumber);
        mTxtLast = findViewById(R.id.txtLastNumber);

        initImage();
        initObserver();
    }

    private void initObserver() {
        mViewModel.getQueueQR(mSharedPreferencesQueue.getString("key", null))
                .observe(this, new Observer<Queue>() {
                    @Override
                    public void onChanged(Queue queue) {
                        Log.d("QRCODE", "onChanged: QR: " + queue);
                        if (queue != null) {
                            mTxtCurrent.setText(String.valueOf(queue.getCurrentNumber()));
                            mTxtLast.setText(String.valueOf(queue.getLastNumber()));
                        }
                    }
                });
    }

    private void initImage() {
        String key = getApplicationContext()
                .getSharedPreferences("queue", MODE_PRIVATE)
                .getString("key", null);
        if (key != null) {
            String goqr = "https://api.qrserver.com/v1/create-qr-code/?data=";
            String url = goqr + key;
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mImageViewQR);
        }
    }
}