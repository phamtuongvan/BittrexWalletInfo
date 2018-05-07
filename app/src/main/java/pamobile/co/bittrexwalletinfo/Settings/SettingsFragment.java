package pamobile.co.bittrexwalletinfo.Settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import PACore.Process.ProgressAsyncTask;
import PACore.Utilities.ArrayConvert;
import PACore.Utilities.JsonConvert;
import PACore.View.FragmentPattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.Balance;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.GetBalancesContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetMarketsContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Market;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.CryptoEvents.CryptoEventAdapter;
import pamobile.co.bittrexwalletinfo.DependencyInjection.Application;
import pamobile.co.bittrexwalletinfo.Models.CryptoEvent;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.Service.Interface.IVolleyService;
import pamobile.co.bittrexwalletinfo.SharedPreference;


public class SettingsFragment extends FragmentPattern {
    @Inject
    ILocalDataService localDataService;
    @Inject
    IVolleyService volleyService;
    @BindView(R.id.rcvSettings)
    RecyclerView rcvSettings;
    @BindView(R.id.btnNofTime)
    Button btnNofTime;
    @BindView(R.id.btnDeltaPercent)
    Button btnDeltaPercent;
    @BindView(R.id.btnAPI)
    Button btnAPI;
    SettingsAdapter settingsAdapter;
    List<Market> marketList;
    SharedPreference sharedPreference;
    public SettingsFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);
        ((Application) getActivity().getApplication()).getGeneralComponent().Inject(this);
        sharedPreference = new SharedPreference(getActivity());
        InitData();
        return rootView;
    }
    public void InitData(){
        btnNofTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notify Time");

// Set up the input
                final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Input number (seconds)");
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreference.saveTimeNotify(Integer.parseInt(input.getText().toString()));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        btnDeltaPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Percent Change");

// Set up the input
                final EditText input = new EditText(getActivity());
                input.setHint("Input minimum % change to notify");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreference.saveDeltaPercent(Math.abs(Float.parseFloat(input.getText().toString())));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        btnAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("API Key and Secret");

// Set up the input
                final EditText apikey = new EditText(getActivity());
                apikey.setHint("API Key");
                final EditText scaret = new EditText(getActivity());
                scaret.setHint("Secret Key");
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(apikey);
                linearLayout.addView(scaret);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(linearLayout);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreference.saveAPI(apikey.getText().toString(),scaret.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        settingsAdapter = new SettingsAdapter(getContext(),new ArrayList<>());
        rcvSettings.setHasFixedSize(false);

        LinearLayoutManager mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSettings.setLayoutManager(mManager);
        rcvSettings.setAdapter(settingsAdapter);
        if(marketList == null){
            loadNewsList();
        }else {
            settingsAdapter.setDataSource(ArrayConvert.toObjectArray(marketList));
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void loadNewsList() {
        final Gson gson = new Gson();

        new ProgressAsyncTask(getContext()) {
            Bittrex bittrex;
            @Override
            public void onDoing() {
                bittrex =  localDataService.GetBittrexAPI(getActivity());
                String jsonMarket = bittrex.getMarketSummaries();
                GetMarketsContainer marketsContainer = gson.fromJson(jsonMarket, GetMarketsContainer.class);
                marketList = marketsContainer.getMarkets();
                System.out.print(jsonMarket);
            }

            @Override
            public void onTaskComplete(Void aVoid) {
                settingsAdapter.setDataSource(ArrayConvert.toObjectArray(marketList));
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewsList();
    }
}
