package id.ac.polman.astra.prg6.queue.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.model.response.NonFetchOperation;
import id.ac.polman.astra.prg6.queue.architecture.model.response.QueueDetailJSON;
import id.ac.polman.astra.prg6.queue.architecture.repositories.QueueRepository;
import id.ac.polman.astra.prg6.queue.architecture.repositories.UserRepository;

public class CreateQueueViewModel extends ViewModel {
    public static final String TAG = "ViewModelCreateQueue";

    private QueueRepository mQueueRepository;
    private UserRepository mUserRepository;
    private MutableLiveData<Queue> mQueueMutableLiveData;
    private MutableLiveData<Exception> mExceptionMutableLiveData;
    private String mName, mEst, mDesc;

    public CreateQueueViewModel() {
        mQueueRepository = QueueRepository.getInstance();
        mUserRepository = UserRepository.getInstance();

        mQueueMutableLiveData = mQueueRepository.getQueueMutableLiveData();
        mExceptionMutableLiveData = mQueueRepository.getExceptionMutableLiveData();
    }

    public void createQueue(Queue queue){
        queue.setHostUid(mUserRepository.getCurrentUser().getUid());
        mQueueRepository.createQueue(queue);
    }

    public MutableLiveData<Queue> getQueueMutableLiveData() {
        return mQueueMutableLiveData;
    }

    public MutableLiveData<Exception> getExceptionMutableLiveData() {
        return mExceptionMutableLiveData;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEst() {
        return mEst;
    }

    public void setEst(String est) {
        mEst = est;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }
}
