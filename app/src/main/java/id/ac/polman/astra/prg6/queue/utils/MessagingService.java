package id.ac.polman.astra.prg6.queue.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = "FCM";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: "+s);
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("token", s).apply();
    }

    public static String getToken(Context context){
        return context.getSharedPreferences("user", MODE_PRIVATE).getString("token", null);
    }
}
