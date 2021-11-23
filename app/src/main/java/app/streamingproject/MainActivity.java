package app.streamingproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import app.streamingproject.Home.HomePostFragment;
import app.streamingproject.Message.ChatListFragment;
import app.streamingproject.OuterClass.SharedPreferenceManager;
import app.streamingproject.OuterClass.ToastMsg;
import app.streamingproject.Tutorial.Tutorial_Activity;
import app.streamingproject.UserPage.UserFragment;

public class MainActivity extends AppCompatActivity {

    NavigationBarView bottomNavigationMenu;
    TextView appTitleBar;
    Boolean isExitFlag = false;
    ToastMsg toastMsg = new ToastMsg();
    String profile;
    final String user_fragment_tag = "UserFragmentTag";
    final String lifecycle = "lifecycle";

    HomePostFragment homePostFragment = new HomePostFragment();
    UserFragment userFragment = new UserFragment();
    PostNewFragment postNewFragment = new PostNewFragment();
    ChatListFragment chatListFragment = new ChatListFragment();

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(lifecycle,"MainActivity is onResume");

        UserFragment userFragmentFun = (UserFragment)
                getSupportFragmentManager().findFragmentByTag(user_fragment_tag);
        if (userFragmentFun != null)
        userFragmentFun.settingProfile(profile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(lifecycle,"MainActivity is onStop");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d(lifecycle,"MainActivity is onCreate");

        tedPermission(); // 접근 권한 요청

        bottomNavigationMenu = findViewById(R.id.main_bottomNavigationView);

        appTitleBar = findViewById(R.id.main_apptitle_tv);

        SharedPreferenceManager.setString(MainActivity.this, "name", getIntent().getExtras().getString("name")); // 현재 사용자 이름
        SharedPreferenceManager.setString(MainActivity.this, "login_time", getIntent().getExtras().getString("login_time")); // 로그인 시간

        // 액티비티에 프래그먼트 추가하고 첫 화면 띄우기
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame, userFragment,user_fragment_tag).addToBackStack("user")
                .add(R.id.main_frame, chatListFragment).addToBackStack("chat")
                .add(R.id.main_frame, postNewFragment).addToBackStack("new")
                .add(R.id.main_frame, homePostFragment).addToBackStack("home") .commit();

        profile = getIntent().getExtras().getString("name");

        //바텀메뉴 클릭 시 프래그먼트 이동
        bottomNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    appTitleBar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homePostFragment).commit();
                    break;
                case R.id.bottom_nav_post:
                    appTitleBar.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, postNewFragment).commit();
                    break;
                case R.id.bottom_nav_chat:
                    appTitleBar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, chatListFragment).commit();
                    break;
                case R.id.bottom_nav_user:
                    appTitleBar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, userFragment,user_fragment_tag).commit();
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (isExitFlag) {
            finish();
        } else {
            isExitFlag = true;
            toastMsg.toastMsg_short("뒤로가기를 한번 더 누르면 종료됩니다.", MainActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExitFlag = false;
                }
            }, 2000);
        }
    }

    //프레그먼트 간 이동
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();     // Fragment로 사용할 Activity내의 layout공간을 선택.
    }

    public void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // 권한 요청 실패
                Toast.makeText(MainActivity.this, "권한 거부" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("앱 이용에 사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .check();
    }
}