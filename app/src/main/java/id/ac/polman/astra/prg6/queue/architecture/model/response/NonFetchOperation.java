package id.ac.polman.astra.prg6.queue.architecture.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonFetchOperation {
    @SerializedName("response")
    @Expose()
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
