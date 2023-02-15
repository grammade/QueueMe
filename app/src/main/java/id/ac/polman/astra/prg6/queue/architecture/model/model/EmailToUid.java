package id.ac.polman.astra.prg6.queue.architecture.model.model;

import androidx.annotation.NonNull;

public class EmailToUid {
    private String Email;
    private String userUid;


    public EmailToUid() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nEmail: "+getEmail()+
                "\nUserID: "+getUserUid();
    }
}
