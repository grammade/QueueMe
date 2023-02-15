package id.ac.polman.astra.prg6.queue.architecture.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import id.ac.polman.astra.prg6.queue.architecture.model.model.EmailToUid;
import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;

public class UserRepository {
    private static UserRepository instance;
    private static final String TAG = "RepoUser";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    MutableLiveData<FirebaseUser> mFirebaseUserMutableLiveData;
    MutableLiveData<Exception> mExceptionMutableLiveData;
    FirebaseUser mFirebaseUser;

    public static UserRepository getInstance(){
        if(instance==null){
            instance=new UserRepository();
        }
        return instance;
    }

    private UserRepository(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser()!=null)
            Log.d(TAG, "UserRepository: USER IS LOGGED IN: "+mAuth.getCurrentUser().getEmail());
    }

    public void authLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mFirebaseUserMutableLiveData.postValue(task.getResult().getUser());
                }else{
                    Log.d(TAG, "onComplete: FAIL :"+task.getException().getMessage());
                    mExceptionMutableLiveData.postValue(task.getException());
                }
            }
        });
    }

    public void FirebaseRegister(final String name, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            mExceptionMutableLiveData.postValue(task.getException());
                        }
                    }
                });

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(final AuthResult authResult) {
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                authResult.getUser().updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mFirebaseUserMutableLiveData.postValue(authResult.getUser());
                        //todo masukin Uid dan email ke table emailToUid
                        emailToUid(authResult.getUser().getEmail(), authResult.getUser().getUid());
                    }
                });
            }
        });
    }

    //todo method emailToUid
    public void emailToUid(String email, String userId){
        mRef = mFirebaseDatabase.getReference("emailToUid");
        EmailToUid admin = new EmailToUid();
        admin.setEmail(email);
        admin.setUserUid(userId);
        mRef.push().setValue(admin);
    }

    public void logout(){
        mAuth.signOut();
    }

    public void updateUser(String key){
        mRef = mFirebaseDatabase.getReference("hostUser");
        QueueHost user = new QueueHost();
        user.setKey(key);
        user.setUserUid(getCurrentUser().getUid());
        mRef.push().setValue(user);
    }

    public void addHostUser(String key, String Uid){
        mRef = mFirebaseDatabase.getReference("hostUser");
        QueueHost user = new QueueHost();
        user.setKey(key);
        user.setUserUid(Uid);
        mRef.push().setValue(user);
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        mFirebaseUserMutableLiveData = new MutableLiveData<>();
        return mFirebaseUserMutableLiveData;
    }

    public MutableLiveData<Exception> getExceptionMutableLiveData() {
        mExceptionMutableLiveData = new MutableLiveData<>();
        return mExceptionMutableLiveData;
    }

    //    registration with mysql + php api
    public void register(String name, String username, String password){
//        mUserAPI.register(name, username, password).enqueue(new Callback<UserModelJSON>() {
//            @Override
//            public void onResponse(Call<UserModelJSON> call, Response<UserModelJSON> response) {
//                if(response.isSuccessful()){
//                    mUserModelLiveData.postValue(response.body());
//                }else{
//                    Log.d(TAG, "onResponse: FAILED. "+response.errorBody());
//                }
//            }
//            @Override
//            public void onFailure(Call<UserModelJSON> call, Throwable t) {
//                Log.d(TAG, "onFailure: "+t.getMessage());
//            }
//        });
    }
}
