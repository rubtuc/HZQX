package face;

import com.bignerdrach.android.psxl.Person;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPersonRequest {
    @FormUrlEncoded
    @POST("/findPerson")
    Call<Person> getPersonInfo(@Field("person_name") String person_name);
}
