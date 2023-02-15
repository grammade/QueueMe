package id.ac.polman.astra.prg6.queue.architecture.model.model;


import androidx.annotation.NonNull;

public class QueueHost {
    private String userUid;
    private String key;

    public QueueHost() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nUserID: "+getUserUid()+
                "\nKey: "+getKey();
    }
}
