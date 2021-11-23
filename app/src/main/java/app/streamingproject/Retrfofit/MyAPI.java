package app.streamingproject.Retrfofit;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyAPI {

    // 유저 - 회원가입 요청
    @POST("/user/")
    Call<UserItem> post_user(@Body UserItem post);

    // 유저 - 로그인 요청
    @GET("/user/getUserByName/{name}/")
    Call<List<UserItem>> get_login(@Path("name") String name);

    // 유저 - 회원 전체 정보 출력
    @GET("/user/")
    Call<List<UserItem>> get_user();

    // 유저 - 회원 이름 별 정보 출력
    @GET("/user/getUserByName/{name}/")
    Call<List<UserItem>> get_user_by_user_name(@Path("name") String name);

    // 게시글 - 게시물 정보 요청
    @POST("/post/")
    Call<PostItem> post_post(@Body PostItem post);

    // 게시글 - 현재 게시물 정보 출력
    @GET("/post/")
    Call<List<PostItem>> get_post();

    // 게시글 - 아이디 별 게시글 출력
    @GET("/post/getPostById/{id}")
    Call<PostItem> get_post_by_id(@Path("id") String id);

    // 게시글 - 아이디로 게시글 정보 수정(하트 숫자 만)
    @PUT("/post/updatePostById/{id}")
    Call<PostItem> update_post_by_id(@Path("id") String id, @Body PostItem update);

    // 게시글 - 날짜 별 게시글 출력
    @GET("/post/getPostByDate/{date}")
    Call<List<PostItem>> get_post_by_date(@Path("date") String date);

    // 게시글 - 회원 이름 별 게시글 출력
    @GET("/post/getPostByName/{name}")
    Call<List<PostItem>> get_post_by_name(@Path("name") String name);

    // 유저 - 모든 유저 삭제
    @DELETE("/post/")
    Call<PostItem> delete_all_post();

    // 게시글 - 아이디로 게시글 삭제
    @DELETE("/post/deletePostById/{id}")
    Call<PostItem> delete_post_by_id(@Path("id") String id);

    // 유저 - 이름으로 유저 프로필사진 변경
    @PUT("/user/updateProfileByName/{name}")
    Call<UserItem> update_profile_by_name(@Path("name") String name, @Body UserItem update);

    // 유저 - 이름으로 유저 비밀번호(확인포함) 변경
    @PUT("/user/updatePwdByName/{name}")
    Call<UserItem> update_pwd_by_name(@Path("name") String name, @Body UserItem update);

    // 유저 - 아이디로 유저 삭제
    @DELETE("/user/deleteUserById/{id}")
    Call<UserItem> delete_user_by_id(@Path("id") String id);

}
