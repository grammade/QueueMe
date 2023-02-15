package id.ac.polman.astra.prg6.queue.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.UserDataViewModel;
import id.ac.polman.astra.prg6.queue.utils.Utils;

public class LoginFragment extends Fragment {
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private static final String TAG = "FragLogin";

    private UserDataViewModel mViewModel;

    private LiveData<FirebaseUser> mFirebaseUserLiveData;
    private LiveData<Exception> mExceptionLiveData;

    private Button mBtnLogin;
    private EditText mTxtEmail, mTxtPass;
    private TextView mTxtRegister;
    private CheckBox mCheckBoxRemember;
    private TextView mTxtInvalid;
    private Callbacks mCallbacks = null;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mBtnLogin = view.findViewById(R.id.btnLogin_login);
        mTxtEmail = view.findViewById(R.id.txtEmail);
        mTxtPass = view.findViewById(R.id.txtPass);
        mTxtRegister = view.findViewById(R.id.txtRegister_login);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTxtInvalid = view.findViewById(R.id.txtInvalid);

        mProgressBar.setVisibility(View.GONE);
        mTxtInvalid.setVisibility(View.INVISIBLE);

        initListener();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);
        mFirebaseUserLiveData = mViewModel.getFirebaseUserLiveData();
        mExceptionLiveData = mViewModel.getExceptionLiveData();

        initUser();
        initObserver();
        initText();
    }

    private void initUser() {
        FirebaseUser user = mViewModel.getCurrentUser();
        if (user != null) {
            mCallbacks.login_goMainMenu(user.getDisplayName());
        }
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(getActivity());
                doLogin();
            }
        });
        mTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.login_goRegister();
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
                mViewModel.setLoginEmail(s.toString());
            }
        }); //storing email to viewModel
        mTxtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setLoginPassword(s.toString());
            }
        }); //storing pass to viewModel
    }

    private void initText() {
        mTxtEmail.setText(mViewModel.getLoginEmail());
        mTxtPass.setText(mViewModel.getLoginPassword());
    }

    private void initObserver() {
        mFirebaseUserLiveData.observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mCallbacks.login_goMainMenu(firebaseUser.getDisplayName());
            }
        });
        mExceptionLiveData.observe(getActivity(), new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mTxtInvalid.setVisibility(View.VISIBLE);
                Utils.toast(getContext(), e.getMessage());
            }
        });
    }

    private void doLogin() {
        if (isValid()) {
            mProgressBar.setVisibility(View.VISIBLE);
            final String email = mTxtEmail.getText().toString().trim();
            final String password = mTxtPass.getText().toString().trim();
            mViewModel.authLogin(email, password);
        } else {
            mTxtInvalid.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValid() {
        if (mTxtEmail.getText().toString().isEmpty())
            return false;
        if (mTxtPass.getText().toString().isEmpty())
            return false;
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public interface Callbacks {
        void login_goMainMenu(String name);

        void login_goRegister();
    }
}
