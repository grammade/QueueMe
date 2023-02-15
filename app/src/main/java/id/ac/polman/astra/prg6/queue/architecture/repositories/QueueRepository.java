package id.ac.polman.astra.prg6.queue.architecture.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import id.ac.polman.astra.prg6.queue.architecture.model.model.EmailToUid;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queuer;

public class QueueRepository {
    private static QueueRepository instance;
    private static String TAG = "RepoQueue";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private MutableLiveData<Queue> mQueueMutableLiveData;
    private MutableLiveData<Queue> mQueueMutableLiveData2;
    private MutableLiveData<QueueHost> mQueueHostMutableLiveData;
    private MutableLiveData<Exception> mExceptionMutableLiveData;
    private MutableLiveData<Queuer> mQueuerMutableLiveData;

    private QueueHost mQueueHost;

    private LifecycleOwner mOwnerObserver;

    public static QueueRepository getInstance() {
        if (instance == null)
            instance = new QueueRepository();
        return instance;
    }

    public QueueRepository() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void createQueue(final Queue queue) {
        mRef = mFirebaseDatabase.getReference("queue");
        final String key = mRef.push().getKey();
        mRef.child(key).setValue(queue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    mExceptionMutableLiveData.postValue(task.getException());
                } else {
                    UserRepository.getInstance().updateUser(key);
                    queue.setKey(key);
                    mQueueMutableLiveData.postValue(queue);
                }
            }
        });
    }

    public void getQueue(String key) {
        mRef = mFirebaseDatabase.getReference("queue");
        mRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mQueueMutableLiveData.postValue(snapshot.getValue(Queue.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void enterQueue(String key, String userToken) {
        mRef = mFirebaseDatabase.getReference("queue/" + key);
        mRef = mRef.child("queuer");
        final Queuer queuer = new Queuer();

        queuer.setUserUid(UserRepository.getInstance().getCurrentUser().getUid());
        queuer.setUserName(UserRepository.getInstance().getCurrentUser().getDisplayName());
        queuer.setUserToken(userToken);

        final String queuerKey = mRef.push().getKey();
        mRef.child(queuerKey).setValue(queuer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {   //post exception if failed
                    mExceptionMutableLiveData.postValue(task.getException());
                }else{
                    queuer.setKey(queuerKey);
                    mQueuerMutableLiveData.postValue(queuer);
                }
            }
        });
    }
    public LiveData<Queuer> getQueuerNumber(String key, String queuerKey){
        mRef = mFirebaseDatabase.getReference("queue/" + key);
        mRef = mRef.child("queuer");
        mRef.child(queuerKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Queuer q;
                q = snapshot.getValue(Queuer.class);
                mQueuerMutableLiveData.postValue(q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mQueuerMutableLiveData;
    }

    public void exitQueue(String key, String queuerKey){
        mRef = mFirebaseDatabase.getReference("queue/" + key);
        mRef = mRef.child("queuer").child(queuerKey);
        mRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }

    public void nextQueue(final String key){
        mRef = mFirebaseDatabase.getReference("queue");
        mRef.child(key+"/queuer/")
                .orderByChild("userNumber")
                .equalTo(mQueueMutableLiveData.getValue().getCurrentNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            snap.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private ValueEventListener mListenerCurrentNumber, mListenerCurrentName, mListenerNextNumber, mListenerLastNumber;
    private ValueEventListener mQListener;
    public void initQueueListener(String key) {
        mRef = mFirebaseDatabase.getReference("queue");
        mListenerCurrentNumber = mRef.child(key).child("currentNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    Queue q = mQueueMutableLiveData.getValue();
                    if(q!=null)
                        q.setCurrentNumber(((Long) snapshot.getValue()).intValue());
                    else
                        q = new Queue();
                    mQueueMutableLiveData.postValue(q);
                }else{
                    mQueueMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mListenerCurrentName = mRef.child(key).child("currentName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Queue q = mQueueMutableLiveData.getValue();
                if(q!=null)
                    q.setCurrentName((String) snapshot.getValue());
                else
                    q = new Queue();
                mQueueMutableLiveData.postValue(q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mListenerNextNumber = mRef.child(key).child("nextNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    Queue q = mQueueMutableLiveData.getValue();
                    if(q!=null)
                        q.setNextNumber(((Long) snapshot.getValue()).intValue());
                    else
                        q = new Queue();
                    mQueueMutableLiveData.postValue(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        mQListener = mRef.child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Queue q = mQueueMutableLiveData.getValue();
//                if(snapshot.getValue()!=null){
//                    Queue snap = snapshot.getValue(Queue.class);
//                    q.setNextNumber(snap.getNextNumber());
//                    q.setCurrentName(snap.getCurrentName());
//                    q.setCurrentNumber(snap.getCurrentNumber());
//                }
//                mQueueMutableLiveData.postValue(q);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void removeQueue(String key) {
        mRef.child(key).child("currentNumber").removeEventListener(mListenerCurrentNumber);
        mRef.child(key).child("currentName").removeEventListener(mListenerCurrentName);
        mRef.child(key).child("nextNumber").removeEventListener(mListenerNextNumber);
//        mRef.child(key).removeEventListener(mQListener);

        mRef = mFirebaseDatabase.getReference("queue");
        mRef.child(key).removeValue();
        mRef = mFirebaseDatabase.getReference("hostUser");
        mRef.orderByChild("key")
                .equalTo(key)
                .getRef().removeValue();
    }

    private ValueEventListener mQRListenerLast, mQRListenerCurrent;
    public void initQRListener(String key){
        mRef = mFirebaseDatabase.getReference("queue");
        mQRListenerCurrent = mRef.child(key).child("currentNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    Queue q = mQueueMutableLiveData2.getValue();
                    if(q!=null)
                        q.setCurrentNumber(((Long) snapshot.getValue()).intValue());
                    else
                        q = new Queue();
                    mQueueMutableLiveData2.postValue(q);
                }else{
                    mQueueMutableLiveData2.postValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mQRListenerLast = mRef.child(key).child("lastNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    Queue q = mQueueMutableLiveData2.getValue();
                    if(q!=null)
                        q.setLastNumber(((Long) snapshot.getValue()).intValue());
                    else
                        q = new Queue();
                    mQueueMutableLiveData2.postValue(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ifHostGetQueue() {
        mRef = mFirebaseDatabase.getReference("hostUser");
        mRef.orderByChild("userUid")
                .equalTo(UserRepository.getInstance().getCurrentUser().getUid())    //userUid is equal to current userUid
                .addListenerForSingleValueEvent(new ValueEventListener() {          //return single record
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            //get single record from snapshot
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: " + Objects.requireNonNull(ds.getValue(QueueHost.class)).toString());
                                mQueueHostMutableLiveData.postValue(ds.getValue(QueueHost.class));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public MutableLiveData<Queue> getQueueMutableLiveData() {
        mQueueMutableLiveData = new MutableLiveData<>();
        return mQueueMutableLiveData;
    }

    public MutableLiveData<Queue> getQueueMutableLiveData2(String key) {
        mRef = mFirebaseDatabase.getReference("queue");
        mRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mQueueMutableLiveData2.postValue(snapshot.getValue(Queue.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mQueueMutableLiveData2 = new MutableLiveData<>();
        return mQueueMutableLiveData2;
    }

    public MutableLiveData<QueueHost> getQueueHostMutableLiveData() {
        mQueueHostMutableLiveData = new MutableLiveData<>();
        return mQueueHostMutableLiveData;
    }

    public MutableLiveData<Queuer> getQueuerMutableLiveData() {
        mQueuerMutableLiveData = new MutableLiveData<>();
        return mQueuerMutableLiveData;
    }

    public MutableLiveData<Exception> getExceptionMutableLiveData() {
        mExceptionMutableLiveData = new MutableLiveData<>();
        return mExceptionMutableLiveData;
    }
    private EmailToUid e;
    public void addAdmin(String email, String key) {
        final String k = key;
        mRef = mFirebaseDatabase.getReference("emailToUid");
        mRef.orderByChild("email")
                .equalTo(email)    //email is equal to input email
                .addListenerForSingleValueEvent(new ValueEventListener() {          //return single record
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            //get single record from snapshot
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: " + Objects.requireNonNull(ds.getValue(QueueHost.class)).toString());
                                e = ds.getValue(EmailToUid.class);
                                UserRepository.getInstance().addHostUser(k, e.getUserUid());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
