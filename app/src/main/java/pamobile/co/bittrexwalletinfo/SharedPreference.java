package pamobile.co.bittrexwalletinfo;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pamobile.co.bittrexwalletinfo.BittrexAPI.UserCredentials;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;

/**
 * Created by Dev04 on 8/25/2016.
 */
public class SharedPreference {
    public static final String PREFS_NAME = "BITTREX_APP";
    public static final String TICKER_NAME = "TICKER_NAME";
    public static final String FOLLOW_NAME = "FOLLOW_NAME";
    Context context;

    public SharedPreference() {
        super();
    }

    public SharedPreference(Context context) {
        super();
        this.context = context;
    }


    public void saveTickers(List<Ticker> tickers,String follow) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String project = gson.toJson(tickers);
        editor.putString(TICKER_NAME+follow, project);
        editor.commit();
    }

    public void addTicker(Ticker item,String follow) {
        List<Ticker> tiker = getTickers(follow);
        if (tiker == null)
            tiker = new ArrayList<Ticker>();
        tiker.add(item);
        saveTickers(tiker,follow);
    }

    public void removeTicker(Ticker item,String follow) {
        ArrayList<Ticker> tickers = getTickers(follow);
        if (tickers != null) {
            for (Ticker c : tickers) {
                if (c.getBid() == (item.getBid()) && c.getAsk() == (item.getAsk()) && c.getLast() == (item.getLast()) ) {
                    tickers.remove(c);
                    break;
                }
            }
            saveTickers(tickers,follow);
        }
    }

    public ArrayList<Ticker> getTickers(String follow) {
        SharedPreferences settings;
        List<Ticker> tickers;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains(TICKER_NAME+follow)) {
            String project = settings.getString(TICKER_NAME+follow, null);
            Type collectionType = new TypeToken<Collection<Ticker>>() {
            }.getType();
            Gson gson = new Gson();
            tickers = gson.fromJson(project, collectionType);
        } else
            return new ArrayList<>();

        return (ArrayList<Ticker>) tickers;
    }




    public void saveFollows(List<String> tickers) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String project = gson.toJson(tickers);
        editor.putString(FOLLOW_NAME, project);
        editor.apply();
    }

    public ArrayList<String> getFollows() {
        SharedPreferences settings;
        List<String> tickers;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains(FOLLOW_NAME)) {
            String project = settings.getString(FOLLOW_NAME, null);
            Type collectionType = new TypeToken<Collection<String>>() {
            }.getType();
            Gson gson = new Gson();
            tickers = gson.fromJson(project, collectionType);
        } else
            return new ArrayList<>();

        return (ArrayList<String>) tickers;
    }

    public void addFollow(String item) {
        List<String> tickers = getFollows();
        if (tickers == null)
            tickers = new ArrayList<String>();
        for (String c : tickers) {
            if (c.equals(item) ) {
                tickers.remove(c);
                break;
            }
        }
        tickers.add(item);
        saveFollows(tickers);
    }

    public boolean checkFollow(String item) {
        List<String> tickers = getFollows();
        if (tickers == null)
            tickers = new ArrayList<String>();
        for (String c : tickers) {
            if (c.equals(item) ) {
               return true;
            }
        }
        return false;
    }

    public void removeFollow(String item) {
        ArrayList<String> tickers = getFollows();
        if (tickers != null) {
            for (String c : tickers) {
                if (c.equals(item) ) {
                    tickers.remove(c);
                    break;
                }
            }
            saveFollows(tickers);
        }
    }

    public void saveTimeNotify(int time) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt("NOTIFY_TIME",time);
        editor.apply();
    }

    public int getTimeNotify() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains("NOTIFY_TIME")) {
            return settings.getInt("NOTIFY_TIME", 30);
        }
        return 30;
    }

    public void saveDeltaPercent(float time) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putFloat("DeltaPercent",time);
        editor.apply();
    }

    public float getDeltaPercent() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains("DeltaPercent")) {
            return settings.getFloat("DeltaPercent", 5);
        }
        return 5;
    }

    public void saveAPI(String userApiKey,String userSecret) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString("ApiKey",userApiKey);
        editor.putString("Secret",userSecret);
        editor.apply();
    }

    public String[] getApi() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains("ApiKey") && settings.contains("Secret")) {
            return new String[]{settings.getString("ApiKey",null),settings.getString("Secret",null)};
        }
        return new String[]{UserCredentials.userApiKey,UserCredentials.userSecret};
    }


    public void saveLastTickerTime(long time) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putLong("LastTickerTime",time);
        editor.apply();
    }

    public long getLastTickerTime() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains("LastTickerTime")) {
            return settings.getLong("LastTickerTime", 0);
        }
        return 0;
    }
}




