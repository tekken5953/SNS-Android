package app.streamingproject.OuterClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.core.content.res.ResourcesCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingCircle;

import app.streamingproject.R;

public class ProgressDialogClass extends Dialog {
    protected Context mContext;

    public ProgressDialogClass(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progressdial);
        this.mContext = context;

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite rotatingCircle = new RotatingCircle();
        progressBar.setIndeterminateDrawable(rotatingCircle);

        Window window = getWindow();

        if (window != null) {
            // 백그라운드 투명
            window.setBackgroundDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    R.color.none, null));

            WindowManager.LayoutParams params = window.getAttributes();
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;

            // UI 중앙 정렬
            window.setGravity(Gravity.CENTER);
        }


    }
}