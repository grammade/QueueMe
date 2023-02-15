package id.ac.polman.astra.prg6.queue.connection.apis;

import id.ac.polman.astra.prg6.queue.architecture.model.response.UserModelJSON;
import id.ac.polman.astra.prg6.queue.connection.CONFIG;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserAPIInterface {

    @FormUrlEncoded
    @POST(CONFIG.LOGIN)
    Call<UserModelJSON> getUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(CONFIG.REGISTER)
    Call<UserModelJSON> register(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(CONFIG.LOGOUT)
    Call<ResponseBody> logout(
            @Field("id") String id
    );

}
