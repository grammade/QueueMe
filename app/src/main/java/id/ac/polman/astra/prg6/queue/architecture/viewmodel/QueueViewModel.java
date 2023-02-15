package id.ac.polman.astra.prg6.queue.architecture.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;
import id.ac.polman.astra.prg6.queue.architecture.model.model.Queuer;
import id.ac.polman.astra.prg6.queue.architecture.repositories.QueueRepository;
import id.ac.polman.astra.prg6.queue.architecture.repositories.UserRepository;

public class QueueViewModel extends ViewModel {
    public static final String TAG = "ViewModelQueue";

    private QueueRepository mQueueRepository;
    private MutableLiveData<Queue> mQueueMutableLiveData;
    private MutableLiveData<Exception> mExceptionMutableLiveData;
    private MutableLiveData<Queuer> mQueuerMutableLiveData;
    private Integer mQueuerNumber;

    public QueueViewModel(){
        mQueueRepository = QueueRepository.getInstance();

        mExceptionMutableLiveData = mQueueRepository.getExceptionMutableLiveData();
        mQueueMutableLiveData = mQueueRepository.getQueueMutableLiveData();
        mQueuerMutableLiveData = mQueueRepository.getQueuerMutableLiveData();
    }

    public void getQueue(String key){
        mQueueRepository.getQueue(key);
    }


    public void initQueueListener(String key){
        mQueueRepository.initQueueListener(key);
    }

    public void enterQueue(String key, String userToken){
        mQueueRepository.enterQueue(key, userToken);
    }

    public void addAdmin(String email, String key){
        mQueueRepository.addAdmin(email, key);
    }

    public LiveData<Queuer> getQueuerNumber(String key, String queuerKey){
        return mQueueRepository.getQueuerNumber(key, queuerKey);
    }

    public void exitQueue(String key, String queuerKey){
        mQueueRepository.exitQueue(key, queuerKey);
    }

    public void nextQueue(String key){mQueueRepository.nextQueue(key);}

    public MutableLiveData<Queue> getQueueMutableLiveData() {
        return mQueueMutableLiveData;
    }

    public MutableLiveData<Exception> getExceptionMutableLiveData() {
        return mExceptionMutableLiveData;
    }

    public LiveData<Queuer> getQueuerMutableLiveData() {
        return mQueueRepository.getQueuerMutableLiveData();
    }

    public Integer getQueuerNumber() {
        return mQueuerNumber;
    }

    public void setQueuerNumber(Integer queuerNumber) {
        mQueuerNumber = queuerNumber;
    }
}
