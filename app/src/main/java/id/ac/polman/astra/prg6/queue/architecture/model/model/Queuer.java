package id.ac.polman.astra.prg6.queue.architecture.model.model;

import androidx.annotation.NonNull;

public class Queuer {
    private String key;
    private String userUid;
    private String userName;
    private String userToken;
    private int userNumber;
    private int nextNumber;

    public Queuer() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nUserName: "+ getUserName()+
                "\nUserNumber: "+getUserNumber();
    }
}
