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

import java.io.FileNotFoundException;
import java.util.List;
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
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, sharedPreference.getTimeNotify());   //Schedule task
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
                    // display toast
                    new AsyncTask<Object,Object,Bittrex>() {
                        @Override
                        protected Bittrex doInBackground(Object[] objects) {

                            Gson gson = new Gson();
                            //String balanceJson = bittrex.getBalances();
                            SharedPreference sharedPreference = new SharedPreference(getBaseContext());
                            List<String> follows = sharedPreference.getFollows();
                            for (String follow:follows) {
                                try {
                                    String tickerAda = localDataService.GetBittrexAPI(getApplicationContext()).getTicker(follow);
                                    Ticker ticker = gson.fromJson(tickerAda, GetTickerContainer.class).getTicker();
                                    if(sharedPreference.getTickers(follow).size() >= 1){
                                        Ticker lastTicker = sharedPreference.getTickers(follow).get(sharedPreference.getTickers(follow).size()-1);
                                        double delta = ticker.getLast() - lastTicker.getLast();
                                        double percent = (float) (delta/lastTicker.getLast() * 100);
                                        Log.e("EEE",""+percent);
                                        if(percent > sharedPreference.getDeltaPercent()){
                                            createNotification(follow+" Current price compare with "+sharedPreference.getTimeNotify()/1000+" s before",String.format("%1$,.8f increased ",delta)+String.format("%1$,.2f % ",percent));
                                        }else if(percent < -sharedPreference.getDeltaPercent()){
                                            createNotification(follow+" Current price compare with "+sharedPreference.getTimeNotify()/1000+" s before",String.format("%1$,.8f decreased ",delta)+String.format("%1$,.2f % ",percent));
                                        }
                                        //createNotification("Current price compare with 15 seconds before",String.format("%1$,.8f ",delta));
                                    }
                                    sharedPreference.addTicker(ticker,follow);
                                }catch (NullPointerException ignored){
                                    Log.e("EEEE",ignored.getMessage());
                                }
                            }
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

    public void createNotification(String title,String content) {
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
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
