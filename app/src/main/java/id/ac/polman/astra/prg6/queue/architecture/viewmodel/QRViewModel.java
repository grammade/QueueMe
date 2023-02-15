package id.ac.polman.astra.prg6.queue.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.ac.polman.astra.prg6.queue.architecture.model.model.Queue;
import id.ac.polman.astra.prg6.queue.architecture.repositories.QueueRepository;

public class QRViewModel extends ViewModel {
    private QueueRepository mQueueRepository;
    private MutableLiveData<Queue> mQueueMutableLiveData;
    public QRViewModel() {
        mQueueRepository = QueueRepository.getInstance();
    }

    public LiveData<Queue> getQueueQR(String key){
        mQueueMutableLiveData = mQueueRepository.getQueueMutableLiveData2(key);
        QueueRepository.getInstance().initQRListener(key);
        return mQueueMutableLiveData;
    }

}
