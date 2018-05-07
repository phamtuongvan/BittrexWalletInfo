package pamobile.co.bittrexwalletinfo.CryptoEvents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import PACore.View.RecycleViewAdapterPattern;
import PACore.View.ViewHolderPattern;
import butterknife.BindView;
import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.accountApiObjects.Balance;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.GetTickerContainer;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Ticker;
import pamobile.co.bittrexwalletinfo.Models.CryptoEvent;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.Wallet.BalanceAdapter;

/**
 * Created by admin on 1/26/18.
 */

public class CryptoEventAdapter extends RecycleViewAdapterPattern {


    public CryptoEventAdapter(Context mContext, ArrayList<Object> dataSource) {
        super(mContext, dataSource);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent,false);
        EventsViewHolder holder = new EventsViewHolder(vi);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventsViewHolder eventsViewHolder = (EventsViewHolder) holder;
        CryptoEvent cryptoEvent = (CryptoEvent) getDataSource().get(position);
        eventsViewHolder.txtCaption.setText(cryptoEvent.getCaption());
        eventsViewHolder.txtCoinName.setText(cryptoEvent.getCoinName());
        eventsViewHolder.txtCoinSymbol.setText(cryptoEvent.getCoinSymbol());


        eventsViewHolder.txtPublicDate.setText(cryptoEvent.getStartDate());
    }

    class EventsViewHolder extends ViewHolderPattern {
        @BindView(R.id.txtCoinName)
        TextView txtCoinName;
        @BindView(R.id.txtCoinSymbol)
        TextView txtCoinSymbol;
        @BindView(R.id.txtCaption)
        TextView txtCaption;
        @BindView(R.id.txtPublicDate)
        TextView txtPublicDate;
        EventsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
