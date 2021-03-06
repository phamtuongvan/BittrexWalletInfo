package pamobile.co.bittrexwalletinfo.CryptoEvents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import PACore.Process.ProgressAsyncTask;
import PACore.Utilities.ArrayConvert;
import PACore.Utilities.JsonConvert;
import PACore.View.FragmentPattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import pamobile.co.bittrexwalletinfo.DependencyInjection.Application;
import pamobile.co.bittrexwalletinfo.Models.CryptoEvent;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.Service.Interface.IVolleyService;
import pamobile.co.bittrexwalletinfo.Wallet.BalanceAdapter;

public class CryptoEventFragment extends FragmentPattern {
    @Inject
    IVolleyService volleyService;
    @BindView(R.id.rcvEvents)
    RecyclerView rcvEvents;
    CryptoEventAdapter cryptoEventAdapter;
    ArrayList<CryptoEvent> cryptoEvents;
    public CryptoEventFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cryptoevent, container, false);
        ButterKnife.bind(this, rootView);
        ((Application) getActivity().getApplication()).getGeneralComponent().Inject(this);
        InitData();
        return rootView;
    }
    public void InitData(){
        cryptoEventAdapter = new CryptoEventAdapter(getContext(),new ArrayList<>());
        rcvEvents.setHasFixedSize(false);

        LinearLayoutManager mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvEvents.setLayoutManager(mManager);
        rcvEvents.setAdapter(cryptoEventAdapter);

        if(cryptoEvents != null){
            for (CryptoEvent event:cryptoEvents) {
                String string = event.getStartDate();
                if(string.split("-").length == 3 && !string.contains(":")){

                    DateFormat format = new SimpleDateFormat("yyyy-m-dd", Locale.ENGLISH);
                    try {
                        Date date = format.parse(string);
                        if(new Date().compareTo(date) == -1){
                            Log.e("after true",string);
                            cryptoEventAdapter.addDataSource(event);
                        }
                    } catch (ParseException e) {
                        Log.e("ParseException",string);
                        e.printStackTrace();
                    }
                }else{
                    cryptoEventAdapter.addDataSource(event);
                }
            }
        }else {
            loadNewsList();
        }
    }

    public void loadNewsList() {
        @SuppressLint("StaticFieldLeak") ProgressAsyncTask progressAsyncTask = new ProgressAsyncTask(getContext()) {
            @Override
            public void onDoing() {
                RequestQueue queue = volleyService.getRequestQueue(getContext());
                String parameterGet = "";
                Calendar calendar = Calendar.getInstance();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://coindar.org/api/v1/events?Year="+calendar.get(Calendar.YEAR)+"&Month="+(calendar.get(Calendar.MONTH)+1),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                cryptoEventAdapter.clearDataSource();
                                cryptoEvents = ArrayConvert.toArrayList(JsonConvert.getArray(response,CryptoEvent[].class));
                                Log.e("TODAY",new Date().toString());
                                for (CryptoEvent event:cryptoEvents) {
                                    String string = event.getStartDate();
                                    if(string.split("-").length == 3 && !string.contains(":")){

                                        DateFormat format = new SimpleDateFormat("yyyy-m-dd", Locale.ENGLISH);
                                        try {
                                            Date date = format.parse(string);
                                            if(new Date().compareTo(date) == -1){
                                                Log.e("after true",string);
                                                cryptoEventAdapter.addDataSource(event);
                                            }
                                        } catch (ParseException e) {
                                            Log.e("ParseException",string);
                                            e.printStackTrace();
                                        }
                                    }else{
                                        cryptoEventAdapter.addDataSource(event);
                                    }
                                }
                                Log.e("EEE",cryptoEvents.size() +"");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }
                };
                queue.add(stringRequest);
            }

            @Override
            public void onTaskComplete(Void aVoid) {

            }
        };
        progressAsyncTask.execute();
    }
}
