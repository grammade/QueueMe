package id.ac.polman.astra.prg6.queue.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queuer;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;
import id.ac.polman.astra.prg6.queue.utils.Utils;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EnterQueueFragment extends Fragment {
    public static final String TAG = "FragmentEnterQueue";
    private static final int RC_PERMISSION = 10;
    private QueueViewModel mViewModel;
    private Callbacks mCallbacks;

    private boolean mPermissionGranted;
    private CodeScanner mScanner;
    private CodeScannerView mScannerView;

    private LiveData<Queuer> mQueuerLiveData;
    private LiveData<Exception> mExceptionLiveData;;

    private SharedPreferences mSharedPreferencesQueue;
    private SharedPreferences mSharedPreferencesUser;
    private SharedPreferences.Editor mSharedPreferencesEditorQueue;

    private String mUserToken = null;
    private String mKey = null;

    public EnterQueueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QueueViewModel.class);
        mQueuerLiveData = mViewModel.getQueuerMutableLiveData();
        mExceptionLiveData = mViewModel.getExceptionMutableLiveData();

        mSharedPreferencesQueue = getContext()
                .getSharedPreferences("queue", Context.MODE_PRIVATE);
        mSharedPreferencesUser = getContext()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
        mSharedPreferencesEditorQueue = mSharedPreferencesQueue.edit();

        mUserToken = mSharedPreferencesUser.getString("token", null);

        initObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_queue, container, false);
        mScannerView = v.findViewById(R.id.scanner);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initScanner();
    }

    private void initScanner(){
        mScanner = new CodeScanner(getContext(), mScannerView);
        mScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.enterQueue(result.getText(), mUserToken);
                        mKey = result.getText();
                    }
                });
            }
        });
    }

    private void initObserver() {
        mExceptionLiveData.observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                Log.d(TAG, "onChanged: EXCEPTION: "+e.getMessage());
            }
        });
        mQueuerLiveData.observe(this, new Observer<Queuer>() {
            @Override
            public void onChanged(Queuer queuer) {
                mSharedPreferencesEditorQueue.putString("queuerKey", queuer.getKey());
                mSharedPreferencesEditorQueue.putString("key", mKey);
                mSharedPreferencesEditorQueue.apply();
                mQueuerLiveData.removeObserver(this);
                mCallbacks.goQueueDashboardQueuer();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionGranted)
            mScanner.startPreview();
    }

    @Override
    public void onPause() {
        mScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks{
        void goQueueDashboardQueuer();
    }
}