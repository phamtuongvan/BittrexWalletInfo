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

        balanceAdapter = new BalanceAdapter(getContext(),new ArrayList<>());
        rcvBalance.setHasFixedSize(false);

        LinearLayoutManager mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvBalance.setLayoutManager(mManager);
        rcvBalance.setAdapter(balanceAdapter);
        //createNotification("Your Balance",String.format("%1$,.8f "+balanceAvailableList.get(0).getCurrency(), balanceAvailableList.get(0).getBalance()));
        new ProgressAsyncTask(getContext()) {
            Bittrex bittrex;
            double sumBtc = 0;
            double sumUsd = 0;
            List<Balance> balanceAvailableList;
            List<Balance> balanceList;
            @Override
            public void onDoing() {
                bittrex =  localDataService.GetBittrexAPI(getActivity());
                Gson gson = new Gson();
                String balanceJson = bittrex.getBalances();
                String tickerUsdtJson = bittrex.getTicker("USDT-BTC");
                Ticker tickerUsdt = gson.fromJson(tickerUsdtJson, GetTickerContainer.class).getTicker();
                System.out.println(tickerUsdt.getLast());
                balanceList = gson.fromJson(balanceJson, GetBalancesContainer.class).getBalances();
                balanceAvailableList = new ArrayList<Balance>();
                balanceAdapter.setBtcPrice(tickerUsdt.getLast());
                balanceAdapter.setBittrex(bittrex);
                balanceAdapter.clearDataSource();

                for (Balance balance:balanceList) {
                    if(balance.getBalance() != 0) {
                        String tickerCurrencyJson = bittrex.getTicker("BTC-" + balance.getCurrency());
                        if (!balance.getCurrency().equals("BTC") && tickerCurrencyJson != null) {
                            Ticker tickerCurrency = gson.fromJson(tickerCurrencyJson, GetTickerContainer.class).getTicker();

                            double estBtc = tickerCurrency.getLast() * balance.getBalance();
                            double estUsdt = estBtc * tickerUsdt.getLast();
                            sumBtc+=estBtc;
                            sumUsd+=estUsdt;
                        }else  if(balance.getCurrency().equals("USDT")){
                            double estUsdt = balance.getBalance();
                            sumUsd+=estUsdt;
                            sumBtc+= (balance.getBalance()/tickerUsdt.getLast());
                        }else if(balance.getCurrency().equals("BTCP")){

                        }else if(balance.getCurrency().equals("BTC")){
                            Log.e("Currency: ",balance.getCurrency());
                            sumBtc+=balance.getBalance();
                            double estUsdt = balance.getBalance() * tickerUsdt.getLast();
                            sumUsd+=estUsdt;
                        }

                    }
                }
            }

            @Override
            public void onTaskComplete(Void aVoid) {
                for (Balance balance:balanceList) {
                    if(balance.getBalance() != 0) {
                        balanceAvailableList.add(balance);
                        balanceAdapter.addDataSource(balance);
                    }
                }
                String btc = String.format("%1$,.8f BTC", sumBtc);
                String usd =String.format("%1$,.2f USD", sumUsd);
                txtEstValue.setText("Estimated Value: "+btc+" / "+usd);
                System.out.println("Sum USD :"+sumUsd);
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
