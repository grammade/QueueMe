package id.ac.polman.astra.prg6.queue.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.CreateQueueViewModel;
import id.ac.polman.astra.prg6.queue.utils.Utils;

public class CreateQueueFragment extends Fragment {
    private static final String TAG = "ActivityCreateQueue";
    public static CreateQueueFragment newInstance(){
        return new CreateQueueFragment();
    }

    private CreateQueueViewModel mViewModel;
    private FirebaseAuth mAuth;

    private EditText mQueueTitle, mEstName, mDescription;
    private Button mBtnCreateQueue, mBtnCancel;
    private ProgressBar mProgressBar;

    private LiveData<Queue> mQueueLiveData;
    private LiveData<Exception> mExceptionLiveData;

    private SharedPreferences mSharedPreferencesQueue;
    private SharedPreferences.Editor mSharedPreferencesEditorQueue;

    private CreateQueueFragmentCallbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CreateQueueViewModel.class);
        mQueueLiveData = mViewModel.getQueueMutableLiveData();
        mExceptionLiveData = mViewModel.getExceptionMutableLiveData();

        mSharedPreferencesQueue = getContext()
                .getSharedPreferences("queue", Context.MODE_PRIVATE);
        mSharedPreferencesEditorQueue = mSharedPreferencesQueue.edit();

        initObserver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_queue,container,false);
        mQueueTitle = view.findViewById(R.id.txtQueuetitle);
        mEstName = view.findViewById(R.id.txtEstName);
        mDescription = view.findViewById(R.id.txtDescription);
        mBtnCreateQueue = view.findViewById(R.id.btnCreate_CreateQueue);
        mBtnCancel = view.findViewById(R.id.btnCancel_CreateQueue);
        mProgressBar = view.findViewById(R.id.progressBar);

        initListener();
        initText();
        return view;
    }

    private void initText() {
        mQueueTitle.setText(mViewModel.getName());
        mEstName.setText(mViewModel.getEst());
        mDescription.setText(mViewModel.getDesc());
    }

    private void initListener(){
        mBtnCreateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(getActivity());

                Queue queue = new Queue();
                queue.setName(mQueueTitle.getText().toString().trim());
                queue.setEstablishmentName(mEstName.getText().toString().trim());
                queue.setDescription(mDescription.getText().toString().trim());
                queue.setNextNumber(1);
                queue.setCurrentNumber(0);
                queue.setCurrentName("-");
                queue.setLastNumber(0);
                mViewModel.createQueue(queue);
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mQueueTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setName(s.toString());
            }
        });
        mEstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setEst(s.toString());
            }
        });
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setDesc(s.toString());
            }
        });
    }

    private void initObserver(){
        mQueueLiveData.observe(this, new Observer<Queue>() {
            @Override
            public void onChanged(Queue queue) {
                if(queue!=null){
                    Utils.toast(getContext(), queue.getName()+" has been created!");

                    mProgressBar.setVisibility(View.GONE);
                    mSharedPreferencesEditorQueue.putString("key", queue.getKey());
                    mSharedPreferencesEditorQueue.apply();

                    mCallbacks.goQueueDashboard();
                }
            }
        });

        mExceptionLiveData.observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                Utils.toast(getContext(), e.getMessage());
                Log.d(TAG, "onChanged: EXCEPTION: "+e.getMessage());
            }
        });
    }

    public interface CreateQueueFragmentCallbacks{
        void goQueueDashboard();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (CreateQueueFragmentCallbacks) context;
    }
}
