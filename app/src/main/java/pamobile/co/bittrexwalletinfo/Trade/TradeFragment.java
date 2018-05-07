package pamobile.co.bittrexwalletinfo.Trade;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.Balance;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.GetBalancesContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetMarketSummariesContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.MarketSummary;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.DependencyInjection.Application;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.SharedPreference;
import pamobile.co.bittrexwalletinfo.Wallet.BalanceAdapter;

public class TradeFragment extends FragmentPattern {
    @Inject
    ILocalDataService localDataService;
    @BindView(R.id.btnSell)
    Button btnSell;
    @BindView(R.id.btnBuy)
    Button btnBuy;
    @BindView(R.id.btnBot)
    Button btnBot;
    public TradeFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trade, container, false);
        ButterKnife.bind(this, rootView);
        ((Application) getActivity().getApplication()).getGeneralComponent().Inject(this);
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("SELL ALL TO USDT");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("ALL IN BTC");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        btnBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("START TEST BOT");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        return rootView;
    }

}
