package id.ac.polman.astra.prg6.queue.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import id.ac.polman.astra.prg6.queue.LoginRegisterActivity;
import id.ac.polman.astra.prg6.queue.R;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;
import id.ac.polman.astra.prg6.queue.architecture.viewmodel.UserDataViewModel;

public class MainMenuFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = "FragmentMainMenu";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private UserDataViewModel mViewModel;

    private Button mBtnEnterQ, mBtnCreateQ;
    private TextView mTxtName;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainMenuFragmentCallbacks mMainMenuFragmentCallbacks;

    public MainMenuFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        checkIfUserIsHost();
    }

    private void checkIfUserIsHost() {
        LiveData<QueueHost> queueHost = mViewModel.getQueueHostMutableLiveData();
        mViewModel.ifHostGetQueue();
        queueHost.observe(this, new Observer<QueueHost>() {
            @Override
            public void onChanged(QueueHost queueHost) {
                getActivity()
                        .getSharedPreferences("queue", Context.MODE_PRIVATE)
                        .edit()
                        .putString("key",queueHost.getKey())
                        .apply();
                Log.d(TAG, "onChanged: "+queueHost.toString());
                mMainMenuFragmentCallbacks.goQueueDashboard();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu,container,false);

        mBtnEnterQ = view.findViewById(R.id.btnEnterQueue_MainMenu);
        mBtnCreateQ = view.findViewById(R.id.btnCreateQueue_MainMenu);
        mTxtName = view.findViewById(R.id.txtEmail);
        mToolbar = view.findViewById(R.id.toolbar);
        mSharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        initListener();
        initUser();

        return view;
    }

    private void initListener() {
        mBtnCreateQ.setOnClickListener(this);
        mBtnEnterQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnEnterQueue_MainMenu:
                mMainMenuFragmentCallbacks.goEnterQueue();
                break;
            case R.id.btnCreateQueue_MainMenu:
                mMainMenuFragmentCallbacks.goCreateQueue();
                break;
        }
    }

    private void initUser(){
        String welcomeMessage = "Welcome, "+ mViewModel.getCurrentUser().getDisplayName();
        mTxtName.setText(welcomeMessage);
    }

    private void doLogout() {
        mViewModel.logout();
        mEditor.clear();
        mEditor.apply();
        startActivity(new Intent(getContext(), LoginRegisterActivity.class));
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLogout:
                doLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainMenuFragmentCallbacks = (MainMenuFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainMenuFragmentCallbacks = null;
    }

    public interface MainMenuFragmentCallbacks {
        void goCreateQueue();
        void goQueueDashboard();
        void goEnterQueue();
        void doLogout();
    }
}
