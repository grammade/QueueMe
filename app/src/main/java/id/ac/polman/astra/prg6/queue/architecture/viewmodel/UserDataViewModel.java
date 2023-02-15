package id.ac.polman.astra.prg6.queue.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import id.ac.polman.astra.prg6.queue.architecture.model.model.EmailToUid;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;
import id.ac.polman.astra.prg6.queue.architecture.repositories.QueueRepository;
import id.ac.polman.astra.prg6.queue.architecture.repositories.UserRepository;

public class UserDataViewModel extends ViewModel {
    public static final String TAG = "ViewModelUserData";

    private UserRepository mUserRepository;
    private QueueRepository mQueueRepository;

    private MutableLiveData<FirebaseUser> mFirebaseUserLiveData;
    private MutableLiveData<Queue> mQueueLiveData;
    private MutableLiveData<QueueHost> mQueueHostMutableLiveData;
    private MutableLiveData<Exception> mExceptionLiveData;

    //Login Frag
    private String mLoginEmail, mLoginPassword;
    //Register Frag
    private String mRegisterName, mRegisterEmail, mRegisterPassword, mRegisterPassword2;
    //LoginRegister Act

    public UserDataViewModel() {
        mUserRepository = UserRepository.getInstance();
        mQueueRepository = QueueRepository.getInstance();
    }

    public void register(String name, String email, String password) {
        mUserRepository.FirebaseRegister(name, email, password);
    }

    public void authLogin(String email, String password) {
        mUserRepository.authLogin(email, password);
    }

    public void logout(){
        mUserRepository.logout();
    }

    public FirebaseUser getCurrentUser() {
        return mUserRepository.getCurrentUser();
    }

    public void ifHostGetQueue(){
        mQueueRepository.ifHostGetQueue();
    }

    public LiveData<FirebaseUser> getFirebaseUserLiveData() {
        mFirebaseUserLiveData = mUserRepository.getFirebaseUserMutableLiveData();
        return mFirebaseUserLiveData;
    }

    public LiveData<Queue> getQueueLiveData() {
        mQueueLiveData = mQueueRepository.getQueueMutableLiveData();
        return mQueueLiveData;
    }

    public MutableLiveData<QueueHost> getQueueHostMutableLiveData() {
        mQueueHostMutableLiveData = mQueueRepository.getQueueHostMutableLiveData();
        return mQueueHostMutableLiveData;
    }

    public LiveData<Exception> getExceptionLiveData() {
        mExceptionLiveData = mUserRepository.getExceptionMutableLiveData();
        return mExceptionLiveData;
    }

    public String getLoginEmail() {
        return mLoginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        mLoginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return mLoginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        mLoginPassword = loginPassword;
    }

    public String getRegisterEmail() {
        return mRegisterEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        mRegisterEmail = registerEmail;
    }

    public String getRegisterPassword() {
        return mRegisterPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        mRegisterPassword = registerPassword;
    }

    public String getRegisterName() {
        return mRegisterName;
    }

    public void setRegisterName(String registerName) {
        mRegisterName = registerName;
    }

    public String getRegisterPassword2() {
        return mRegisterPassword2;
    }

    public void setRegisterPassword2(String registerPassword2) {
        mRegisterPassword2 = registerPassword2;
    }
}
