package face;

import com.bignerdrach.android.psxl.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IRegister {
    @FormUrlEncoded
    @POST("/newUser")
    Call<User> setUserInfo(@Field("account") String account, @Field("pwd") String pwd);
}
