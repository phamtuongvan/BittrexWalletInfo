package pamobile.co.bittrexwalletinfo.Service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.DependencyInjection.Application;
import pamobile.co.bittrexwalletinfo.HomeActivity;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.SharedPreference;

public class BackgroundService extends Service {
    @Inject
    ILocalDataService localDataService;
    public BackgroundService() {
    }
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    SharedPreference sharedPreference;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        ((Application) getApplication()).getGeneralComponent().Inject(this);
        sharedPreference = new SharedPreference(getApplicationContext());
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        Log.e("EEE",sharedPreference.getTimeNotify()*1000 +" mili giay");
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, sharedPreference.getTimeNotify()*1000);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void run() {
                    final long currentTime = new Date().getTime();
                    // display toast
                    new AsyncTask<Object,Object,Bittrex>() {
                        @Override
                        protected Bittrex doInBackground(Object[] objects) {

                            Gson gson = new Gson();
                            //String balanceJson = bittrex.getBalances();
                            SharedPreference sharedPreference = new SharedPreference(getBaseContext());
                            List<String> follows = sharedPreference.getFollows();
                            long deltaTime = sharedPreference.getLastTickerTime() - currentTime;
                            int roundTime = Math.abs(Math.round(deltaTime/1000.0f));
                            for (String follow:follows) {
                                try {
                                    String tickerContainer = localDataService.GetBittrexAPI(getApplicationContext()).getTicker(follow);
                                    Ticker ticker = gson.fromJson(tickerContainer, GetTickerContainer.class).getTicker();
                                    if(sharedPreference.getTickers(follow).size() >= 1 && roundTime == sharedPreference.getTimeNotify()){
                                        Ticker lastTicker = sharedPreference.getTickers(follow).get(sharedPreference.getTickers(follow).size()-1);
                                        double delta = ticker.getLast() - lastTicker.getLast();
                                        double percent = (delta/lastTicker.getLast() * 100);
                                        String currentP = String.format("%1$,.8f",ticker.getLast());
                                        String lastP = String.format("%1$,.8f",lastTicker.getLast());
                                        if(percent > sharedPreference.getDeltaPercent()){
                                            String a = String.format("%1$,.8f",delta);
                                            String b = String.format("%1$,.2f",percent);
                                            Log.e("EEEA",a);
                                            Log.e("EEEB",b);
                                            String content =follow+": "+ a +" increased "+ b + " %";
                                            Log.e("EEEcontent",content);
                                            Log.e("mNotificationId",follow +": "+follow.hashCode() );
                                            createNotification(follow.hashCode(),"Current price:"+currentP+", "+sharedPreference.getTimeNotify()+"s before price:"+lastP,content);
                                        }else if(percent < -sharedPreference.getDeltaPercent()){
                                            String a = String.format("%1$,.8f",delta);
                                            String b = String.format("%1$,.2f",percent);
                                            Log.e("EEEA",a);
                                            Log.e("EEEB",b);
                                            String content =follow+": "+ a +" decreased "+ b + " %";
                                            Log.e("EEEcontent",content);
                                            Log.e("mNotificationId",follow +": "+follow.hashCode() );
                                            createNotification(follow.hashCode(),"Current price:"+currentP+", "+sharedPreference.getTimeNotify()+"s before price:"+lastP,content);
                                        }
                                        //createNotification("Current price compare with 15 seconds before",String.format("%1$,.8f ",delta));
                                    }

                                    sharedPreference.addTicker(ticker,follow);
                                }catch (Exception ignored){
                                    Log.e("EEEE",ignored.getMessage());
                                }
                            }
                            sharedPreference.saveLastTickerTime(currentTime);
                            return localDataService.GetBittrexAPI(getApplicationContext());
                        }

                        @Override
                        protected void onPostExecute(Bittrex bittrex) {
                            super.onPostExecute(bittrex);
                        }
                    }.execute();
                }
            });
        }
    }

    public void createNotification(int mNotificationId,String content,String title) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        // Build notification
        // Actions are just fake
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);
        mBuilder.setContentIntent(pIntent);

        // Sets an ID for the notification

// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
