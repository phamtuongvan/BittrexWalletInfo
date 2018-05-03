package pamobile.co.bittrexwalletinfo.Wallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import PACore.View.RecycleViewAdapterPattern;
import PACore.View.ViewHolderPattern;
import butterknife.BindView;
import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.Balance;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.R;

/**
 * Created by admin on 1/8/18.
 */

public class BalanceAdapter extends RecycleViewAdapterPattern {
    /**
     * Initialize
     *
     * @param mContext   The View Context
     * @param dataSource
     */

    double btc_usdt_price = 0.0;
    Bittrex bittrex;

    public double getBtcPrice() {
        return btc_usdt_price;
    }

    public void setBtcPrice(double btc_usdt_price) {
        this.btc_usdt_price = btc_usdt_price;
    }

    public Bittrex getBittrex() {
        return bittrex;
    }

    public void setBittrex(Bittrex bittrex) {
        this.bittrex = bittrex;
    }

    public BalanceAdapter(Context mContext, ArrayList<Object> dataSource) {
        super(mContext, dataSource);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(getContext()).inflate(R.layout.item_balance, parent,false);
        BalanceViewHolder holder = new BalanceViewHolder(vi);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BalanceViewHolder balanceViewHolder = (BalanceViewHolder) holder;
        Balance balance = (Balance) getDataSource().get(position);
        Gson gson = new Gson();
        balanceViewHolder.txtSymbol.setText(balance.getCurrency());
        balanceViewHolder.txtSumTitle.setText("Sum");
        balanceViewHolder.txtAvailableTitle.setText("Available");
        balanceViewHolder.txtSum.setText(String.format("%1$,.8f",balance.getBalance()));
        balanceViewHolder.txtAvailable.setText(String.format("%1$,.8f",balance.getAvailable()));

        String tickerAda = bittrex.getTicker("BTC-"+balance.getCurrency());
        if(!balance.getCurrency().equals("BTC") && tickerAda != null){
            Ticker ticker = gson.fromJson(tickerAda, GetTickerContainer.class).getTicker();

            double estBtc = ticker.getLast() *  balance.getBalance();
            double estUsdt = estBtc*getBtcPrice();
            balanceViewHolder.txtEstUsdt.setText(String.format("%1$,.2f USD",estUsdt));
            if(estUsdt<0.01){
                balanceViewHolder.txtEstUsdt.setText("< 0.01 USD");
            }

        }else if(balance.getCurrency().equals("USDT")){
            double estUsdt = balance.getBalance();
            balanceViewHolder.txtEstUsdt.setText(String.format("%1$,.2f USD",estUsdt));
            if(estUsdt<0.01){
                balanceViewHolder.txtEstUsdt.setText("< 0.01 USD");
            }
        }else if(balance.getCurrency().equals("BTCP")){

        }else if(balance.getCurrency().equals("BTC")){
            double estUsdt = balance.getBalance()*getBtcPrice();
            balanceViewHolder.txtEstUsdt.setText(String.format("%1$,.2f USD",estUsdt));
            if(estUsdt<0.01){
                balanceViewHolder.txtEstUsdt.setText("< 0.01 USD");
            }

        }

    }

    class BalanceViewHolder extends ViewHolderPattern {
        @BindView(R.id.txtSymbol)
        TextView txtSymbol;
        @BindView(R.id.txtSumTitle)
        TextView txtSumTitle;
        @BindView(R.id.txtSum)
        TextView txtSum;
        @BindView(R.id.txtAvailableTitle)
        TextView txtAvailableTitle;
        @BindView(R.id.txtAvailable)
        TextView txtAvailable;
        @BindView(R.id.txtEstUsdt)
        TextView txtEstUsdt;
        BalanceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
