package id.ac.polman.astra.prg6.queue.architecture.model.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import id.ac.polman.astra.prg6.queue.architecture.model.model.QueueHost;

public class UserModelJSON {

    @SerializedName("userModel")
    private QueueHost mUserModel;
    @SerializedName("response")
    private String response;

    public QueueHost getUserModel() {
        return mUserModel;
    }

    public String getResponse() {
        return response;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nResponse: "+response+
                mUserModel.toString();
    }
}
