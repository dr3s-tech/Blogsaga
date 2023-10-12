package com.example.blogsaga.utils.services;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.blogsaga.utils.FirebaseUtilities.DownloadUploadUtils;

public class UpdateFollowersService extends Service {

    private Looper serviceLooper;
    private UpdateFollowersService.ServiceHandler serviceHandler;
    private static final String SERVICE_CLASS_TAG = UpdateFollowersService.class.getName().toString();

    private static String mail;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i(SERVICE_CLASS_TAG, "Firebase user follower thread");
            DownloadUploadUtils.updationFollowerFollowing(mail);
            System.out.println(mail);
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate(){
        HandlerThread thread=new HandlerThread("ServicesFollowerStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();;
        serviceLooper=thread.getLooper();
        serviceHandler=new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg=serviceHandler.obtainMessage();
        msg.arg1=startId;
        Log.e("Followers update service","started");
        mail= intent.getStringExtra("EMAIL");
        System.out.println(mail);
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
