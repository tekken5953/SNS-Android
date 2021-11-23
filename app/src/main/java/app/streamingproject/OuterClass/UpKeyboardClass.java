package app.streamingproject.OuterClass;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class UpKeyboardClass {

    //KeyBoard Up Inner Class
    public void keyboardUp(EditText editText, Context context) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //focus 후 키보드 올리기
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
