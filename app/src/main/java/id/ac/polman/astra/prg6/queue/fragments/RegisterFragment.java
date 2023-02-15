package id.ac.polman.astra.prg6.queue.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.model.response.UserModelJSON;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.UserDataViewModel;
import id.ac.polman.astra.prg6.queue.utils.Utils;

public class RegisterFragment extends Fragment {
    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private static final String TAG = "FragRegister";

    private UserDataViewModel mViewModel = null;;

    private LiveData<FirebaseUser> mFirebaseUserLiveData;
    private LiveData<Exception> mExceptionLiveData;

    private Button mBtnRegister;
    private EditText mTxtEmail, mTxtPass, mTxtPass2, mTxtName;
    private TextView mTxtLogin;
    private ProgressBar mProgressBar;
    private Callbacks mCallbacks = null;

    private String mEmail, mPassword, mPasswordRe, mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        mFirebaseUserLiveData = mViewModel.getFirebaseUserLiveData();
        mExceptionLiveData = mViewModel.getExceptionLiveData();
        initObserver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);

        mBtnRegister = view.findViewById(R.id.btnRegister_register);
        mTxtEmail = view.findViewById(R.id.txtEmail);
        mTxtName = view.findViewById(R.id.txtName);
        mTxtPass = view.findViewById(R.id.txtPass);
        mTxtPass2 = view.findViewById(R.id.txtPass2);
        mTxtLogin = view.findViewById(R.id.txtLogin_register);
        mProgressBar = view.findViewById(R.id.progressBar);
        initListener();
        initText();

        return view;
    }

    private void initText() {
        mTxtName.setText(mViewModel.getRegisterName());
        mTxtEmail.setText(mViewModel.getRegisterEmail());
        mTxtPass.setText(mViewModel.getRegisterPassword());
        mTxtPass2.setText(mViewModel.getRegisterPassword2());
    }

    private void doRegister(){
        mEmail = mTxtEmail.getText().toString().trim();
        mPassword = mTxtPass.getText().toString().trim();
        mPasswordRe = mTxtPass2.getText().toString().trim();
        mName = mTxtName.getText().toString().trim();
        if(isValid()){
            mProgressBar.setVisibility(View.VISIBLE);
            mViewModel.register(mName, mEmail,mPassword);
        }
    }

    private boolean isValid(){
        boolean result = true;
        if(mEmail == null || mEmail.isEmpty()){
            mTxtEmail.setError("Email is Required");
            result = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            mTxtEmail.setError("Invalid Email");
            result = false;
        }
        if(mPassword == null || mEmail.isEmpty()){
            mTxtPass.setError("Password is Required");
            result = false;
        }
        if(mPasswordRe == null || mEmail.isEmpty()){
            mTxtPass2.setError("Please Re-type your password");
            result = false;
        }
        if(mName == null || mEmail.isEmpty()){
            mTxtName.setError("Name is Required");
            result = false;
        }
        if(!mPassword.equals(mPasswordRe)){
            mTxtPass2.setError("Miss match Password");
            result = false;
        }


        return result;
    }

    private void initListener(){
        mTxtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.register_goLogin();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(getActivity());
                doRegister();
            }
        });
        mTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setRegisterEmail(s.toString());
            }
        });
        mTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setRegisterName(s.toString());
            }
        });
        mTxtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setRegisterPassword(s.toString());
            }
        });
        mTxtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setRegisterPassword2(s.toString());
            }
        });
    }

    private void initObserver(){

        mFirebaseUserLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"User has been successfully created",Toast.LENGTH_LONG).show();
                mCallbacks.register_goLogin();
            }
        });

        mExceptionLiveData.observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public interface Callbacks{
        void register_goLogin();
    }
}
