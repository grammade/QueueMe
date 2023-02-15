package id.ac.polman.astra.prg6.queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

import id.ac.polman.astra.prg6.queue.architecture.viewmodel.UserDataViewModel;
import id.ac.polman.astra.prg6.queue.fragments.LoginFragment;
import id.ac.polman.astra.prg6.queue.fragments.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity implements LoginFragment.Callbacks, RegisterFragment.Callbacks {
    private static final String TAG = "LoginRegisterActivity";
    public static final int RC_SIGN_IN = 1;
    private FragmentManager fm;
    private Fragment mFragment;

    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private UserDataViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        mViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        init();
        if(savedInstanceState==null)
            initFragment();
    }

    private void init() {
        mSharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        fm = getSupportFragmentManager();
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
    }

    private void initFragment() {
        mFragment = LoginFragment.newInstance();
        fm.beginTransaction().replace(R.id.fragment_container, mFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLogout:
                mEditor.putBoolean("rememberMe", false);
                mEditor.apply();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void login_goMainMenu(String name) {
        doLogin();
    }

    @Override
    public void login_goRegister() {
        Fragment fragment = RegisterFragment.newInstance();
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void register_goLogin() {
//        fm.popBackStack();
        Fragment fragment = LoginFragment.newInstance();
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void doLogin() {
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
        finish();
    }
}
