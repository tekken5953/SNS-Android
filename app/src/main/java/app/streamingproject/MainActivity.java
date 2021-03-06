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

        tedPermission(); // ?????? ?????? ??????

        bottomNavigationMenu = findViewById(R.id.main_bottomNavigationView);

        appTitleBar = findViewById(R.id.main_apptitle_tv);

        SharedPreferenceManager.setString(MainActivity.this, "name", getIntent().getExtras().getString("name")); // ?????? ????????? ??????
        SharedPreferenceManager.setString(MainActivity.this, "login_time", getIntent().getExtras().getString("login_time")); // ????????? ??????

        // ??????????????? ??????????????? ???????????? ??? ?????? ?????????
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame, userFragment,user_fragment_tag).addToBackStack("user")
                .add(R.id.main_frame, chatListFragment).addToBackStack("chat")
                .add(R.id.main_frame, postNewFragment).addToBackStack("new")
                .add(R.id.main_frame, homePostFragment).addToBackStack("home") .commit();

        profile = getIntent().getExtras().getString("name");

        //???????????? ?????? ??? ??????????????? ??????
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
            toastMsg.toastMsg_short("??????????????? ?????? ??? ????????? ???????????????.", MainActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExitFlag = false;
                }
            }, 2000);
        }
    }

    //??????????????? ??? ??????
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();     // Fragment??? ????????? Activity?????? layout????????? ??????.
    }

    public void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // ?????? ?????? ??????
                Toast.makeText(MainActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // ?????? ?????? ??????
                Toast.makeText(MainActivity.this, "?????? ??????" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("??? ????????? ?????? ??? ????????? ???????????? ????????? ?????? ????????? ???????????????.")
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .check();
    }
}