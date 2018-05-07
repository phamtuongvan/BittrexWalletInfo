package pamobile.co.bittrexwalletinfo.Wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import PACore.Process.ProgressAsyncTask;
import PACore.Utilities.ArrayConvert;
import PACore.View.FragmentPattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.UserCredentials;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.Balance;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.GetBalancesContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetMarketSummariesContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.MarketSummary;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.DependencyInjection.Application;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.Service.BackgroundService;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.SharedPreference;
import pamobile.co.bittrexwalletinfo.Wallet.BalanceAdapter;

public class WalletFragment extends FragmentPattern {
    @Inject
    ILocalDataService localDataService;
    @BindView(R.id.rcvBalance)
    RecyclerView rcvBalance;
    @BindView(R.id.txtEstValue)
    TextView txtEstValue;
    BalanceAdapter balanceAdapter;
    SharedPreference sharedPreference;
    List<Balance> balanceAvailableList;
    Bittrex bittrex;
    Gson gson = new Gson();
    Ticker tickerUsdt;
    public WalletFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        ButterKnife.bind(this, rootView);
        ((Application) getActivity().getApplication()).getGeneralComponent().Inject(this);
        InitData();
        return rootView;
    }
    @SuppressLint("StaticFieldLeak")
    public void InitData(){
        sharedPreference = new SharedPreference(getContext());
        rcvBalance.setHasFixedSize(false);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvBalance.setLayoutManager(mManager);

        if(balanceAvailableList == null){
            balanceAdapter = new BalanceAdapter(this,new ArrayList<>());
            loadNewData();
            rcvBalance.setAdapter(balanceAdapter);
        }else {
            new ProgressAsyncTask(getContext()) {

                @Override
                public void onDoing() {
                    String tickerUsdtJson = bittrex.getTicker("USDT-BTC");
                    tickerUsdt = gson.fromJson(tickerUsdtJson, GetTickerContainer.class).getTicker();
                    System.out.println(tickerUsdt.getLast());
                    balanceAdapter.setBtcPrice(tickerUsdt.getLast());
                    balanceAdapter.setBittrex(bittrex);
                }

                @Override
                public void onTaskComplete(Void aVoid) {
                    balanceAdapter.setDataSource(ArrayConvert.toObjectArray(balanceAvailableList));
                    rcvBalance.setAdapter(balanceAdapter);
                }
            }.execute();
        }


    }

    public void updateEstValue(String sumBtc,String sumUsd){
        txtEstValue.setText("Estimated Value: "+sumBtc+" / "+sumUsd);
    }


    @SuppressLint("StaticFieldLeak")
    public void loadNewData(){
        new ProgressAsyncTask(getContext()) {
            List<Balance> balanceList;
            @Override
            public void onDoing() {
                bittrex =  localDataService.GetBittrexAPI(getActivity());

                String balanceJson = bittrex.getBalances();
                String tickerUsdtJson = bittrex.getTicker("USDT-BTC");
                tickerUsdt = gson.fromJson(tickerUsdtJson, GetTickerContainer.class).getTicker();
                System.out.println(tickerUsdt.getLast());
                balanceList = gson.fromJson(balanceJson, GetBalancesContainer.class).getBalances();
                balanceAvailableList = new ArrayList<Balance>();
                balanceAdapter.setBtcPrice(tickerUsdt.getLast());
                balanceAdapter.setBittrex(bittrex);

                balanceAdapter.clearDataSource();
                for (Balance balance:balanceList) {

                    if(balance.getBalance() != 0) {
                        balanceAdapter.getDataSource().add(balance);
                        balanceAvailableList.add(balance);
                    }
                }
            }

            @Override
            public void onTaskComplete(Void aVoid) {
                balanceAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void findBiggest24HGainer(Bittrex bittrex) {
        Gson gson = new Gson();
        List<MarketSummary> marketSummaries = gson.fromJson(bittrex.getMarketSummaries(), GetMarketSummariesContainer.class).getMarketSummaries();
        String marketName = "";
        Double percentChange = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < marketSummaries.size(); i++) {
            MarketSummary marketSummary = marketSummaries.get(i);
            Double lastVal = marketSummary.getLast();
            Double prevDayVal = marketSummary.getPrevDay();
            Double marketPercentChange = (lastVal-prevDayVal)/prevDayVal;
            if (marketPercentChange > percentChange) {
                marketName = marketSummary.getMarketName();
                percentChange = marketPercentChange;
            }
        }
        System.out.println("Biggest Gainer in the last 24H is " + bittrex.getMarket(marketName).getMarketCurrencyLong()
                + " with " + Double.toString(percentChange*100).substring(0, 6) + "% change.");
    }
}
