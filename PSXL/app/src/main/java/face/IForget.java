package face;

import com.bignerdrach.android.psxl.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IForget {
    @FormUrlEncoded
    @POST("/updateUser")
    Call<User> resetUserInfo(@Field("account") String account, @Field("pwd") String pwd);
}
