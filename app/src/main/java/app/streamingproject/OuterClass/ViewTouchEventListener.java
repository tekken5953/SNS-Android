package app.streamingproject.OuterClass;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class ViewTouchEventListener {

    public void onPressView(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,  MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                    case MotionEvent.ACTION_MOVE : {
                        v.setScaleY(1.1f);
                        v.setScaleX(1.1f);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.setScaleY(1f);
                        v.setScaleX(1f);
                        break;
                    }
                }
                return false;
            }
        });
    }
}
