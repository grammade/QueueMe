package id.ac.polman.astra.prg6.queue.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {
    public static void closeKeyboard(Activity activity){
        View v = activity.getCurrentFocus();
        if(v!=null){
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void toast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
