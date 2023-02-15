package id.ac.polman.astra.prg6.queue.connection.apis;

import id.ac.polman.astra.prg6.queue.architecture.model.response.QueueDetailJSON;
import id.ac.polman.astra.prg6.queue.connection.CONFIG;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface QueueAPIInterface {

    @FormUrlEncoded
    @POST(CONFIG.QUEUE_CREATE)
    Call<QueueDetailJSON>createQueue(
            @Field("name") String name,
            @Field("hostId") String hostId,
            @Field("establishmentName") String establishmentName,
            @Field("description") String description
    );
}
