package id.ac.polman.astra.prg6.queue.architecture.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.ac.polman.astra.prg6.queue.architecture.model.model._OldQueueModel;


public class QueueDetailJSON {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("QueueModel")
    @Expose
    private _OldQueueModel queue;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public _OldQueueModel getQueue() {
        return queue;
    }

    public void setQueue(_OldQueueModel queue) {
        this.queue = queue;
    }
}