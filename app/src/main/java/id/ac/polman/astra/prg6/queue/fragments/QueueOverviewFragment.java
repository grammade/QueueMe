package id.ac.polman.astra.prg6.queue.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eyalbira.loadingdots.LoadingDots;

import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.QueueViewModel;
import id.ac.polman.astra.prg6.queue.utils.DialogYourTurn;


public class QueueOverviewFragment extends Fragment{
    public static final String TAG = "FragmentOverviewQueue";
    private QueueViewModel mViewModel;
    private QueueOverviewCallbacks mCallbacks;

    private SharedPreferences mSharedPreferencesQueue;
    private SharedPreferences.Editor mPreferencesEditorQueue;

    private TextView mTxtQueueName, mTxtQueueEstablishment;
    private TextView mTxtCurrent, mTxtNext;
    private TextView mTxtCurrentName;
    private LoadingDots mLoadingName, mLoadingEst;
    private ProgressBar mProgressBarCurrent, mProgressBarNext;

    private LiveData<Queue> mQueueLiveData;
    private Boolean hasInitialized = false;
    private Integer queuerNumber = null;

    public QueueOverviewFragment() {
    }

    public static QueueOverviewFragment newInstance() {
        QueueOverviewFragment fragment = new QueueOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FRAGMENT A
        mViewModel = new ViewModelProvider(requireActivity()).get(QueueViewModel.class);
        mQueueLiveData = mViewModel.getQueueMutableLiveData();

        mSharedPreferencesQueue = getContext()
                .getSharedPreferences("queue", Context.MODE_PRIVATE);
        if (mSharedPreferencesQueue.getInt("queuerNumber", -1) != -1)
            queuerNumber = mSharedPreferencesQueue.getInt("queuerNumber", -1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_queue_overview, container, false);

        mTxtQueueName = v.findViewById(R.id.txtQueueName);
        mTxtQueueEstablishment = v.findViewById(R.id.txtQueueEst);
        mTxtCurrent = v.findViewById(R.id.txtCurrentNumber);
        mTxtCurrentName = v.findViewById(R.id.txtCurrentName);
        mTxtNext = v.findViewById(R.id.txtNext);

        mLoadingEst = v.findViewById(R.id.loadingQueueEst);
        mLoadingName = v.findViewById(R.id.loadingQueueName);
        mProgressBarCurrent = v.findViewById(R.id.progressbarCurrent);
        mProgressBarNext = v.findViewById(R.id.progressbarNext);

        mSharedPreferencesQueue = getContext()
                .getSharedPreferences("queue", Context.MODE_PRIVATE);

        initObserver();
        initQueue();

        return v;
    }

    private void initObserver() {
        mQueueLiveData.observe(getActivity(), new Observer<Queue>() {
            @Override
            public void onChanged(Queue queue) {
                try{
                    if (queue == null) {
                        mSharedPreferencesQueue.edit().clear().apply();
                        mCallbacks.goMainMenu();
                    }
                    mLoadingEst.setVisibility(View.GONE);
                    mLoadingName.setVisibility(View.GONE);
                    mProgressBarCurrent.setVisibility(View.GONE);
                    mProgressBarNext.setVisibility(View.GONE);

                    mTxtQueueName.setText(queue.getName());
                    mTxtQueueEstablishment.setText(queue.getEstablishmentName());
                    mTxtCurrent.setText(String.valueOf(queue.getCurrentNumber()));
                    mTxtCurrentName.setText(queue.getCurrentName());
                    mTxtNext.setText(String.valueOf(queue.getNextNumber()));

                    queuerNumber = mViewModel.getQueuerNumber();
                    if (queuerNumber != null && queuerNumber == queue.getCurrentNumber()) {
                        mQueueLiveData.removeObserver(this);
                        DialogYourTurn dialog = new DialogYourTurn();
                        dialog.show(getActivity().getSupportFragmentManager(), "yourTurn");
                    }

                    if (!hasInitialized)
                        initQueueListener();
                }catch (Exception e){
                    mSharedPreferencesQueue.edit().clear().apply();
                    mCallbacks.goMainMenu();
                }
            }
        });
    }

    private void initQueue() {
        mViewModel.getQueue(mSharedPreferencesQueue.getString("key", null));
    }

    private void initQueueListener() {
        hasInitialized = true;
        mViewModel.initQueueListener(mSharedPreferencesQueue.getString("key", null));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (QueueOverviewCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface QueueOverviewCallbacks {
        void goMainMenu();
    }
}
