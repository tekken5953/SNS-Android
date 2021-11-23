package app.streamingproject.OuterClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.streamingproject.R;

public class ToastMsg extends Activity {

    final static int color = Color.WHITE;
    final static int size = 17;

    public void toastMsg_short(String s, Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.toast_layout, null);
        final TextView text = layout.findViewById(R.id.toast_text);
        Typeface typeface = context.getResources().getFont(R.font.gmarket_sans_medium);
        Toast toast = new Toast(context.getApplicationContext());
        text.setTextSize(size);
        text.setTextColor(color);
        text.setTypeface(typeface);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        text.setText(s);
        toast.show();
    }

    public void toastMsg_long(String s, Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.toast_layout, null);
        final TextView text = layout.findViewById(R.id.toast_text);
        Typeface typeface = context.getResources().getFont(R.font.gmarket_sans_medium);
        Toast toast = new Toast(context.getApplicationContext());
        text.setTextSize(size);
        text.setTextColor(color);
        toast.setGravity(Gravity.CENTER, 0, 0);
        text.setTypeface(typeface);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        text.setText(s);
        toast.show();
    }
}
