package app.streamingproject.Retrfofit;

//회원가입시 Request 되는 UserInfo
public class UserItem {

    private String id;
    private String name;  // 이름
    private String phone; // 전화번호
    private String pwd; // 비밀번호
    private String repeat_pwd; // 비밀번호 확인
    private String profile; // 프로필 사진 URI
//    private String token;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRepeat_pwd() {
        return repeat_pwd;
    }

    public void setRepeat_pwd(String repeat_pwd) {
        this.repeat_pwd = repeat_pwd;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
