package pamobile.co.bittrexwalletinfo.Settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import PACore.View.RecycleViewAdapterPattern;
import PACore.View.ViewHolderPattern;
import butterknife.BindView;
import pamobile.co.bittrexwalletinfo.BittrexAPI.publicApiObjects.Market;
import pamobile.co.bittrexwalletinfo.Models.CryptoEvent;
import pamobile.co.bittrexwalletinfo.R;
import pamobile.co.bittrexwalletinfo.SharedPreference;

/**
 * Created by admin on 1/26/18.
 */

public class SettingsAdapter extends RecycleViewAdapterPattern {

    SharedPreference sharedPreference;
    public SettingsAdapter(Context mContext, ArrayList<Object> dataSource) {
        super(mContext, dataSource);
        sharedPreference = new SharedPreference(getContext());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(getContext()).inflate(R.layout.item_setting, parent,false);
        SettingsViewHolder holder = new SettingsViewHolder(vi);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SettingsViewHolder eventsViewHolder = (SettingsViewHolder) holder;
        final Market cryptoEvent = (Market) getDataSource().get(position);

        eventsViewHolder.txtMarketName.setText(cryptoEvent.getMarketName());
        if(sharedPreference.checkFollow(cryptoEvent.getMarketName())){
            eventsViewHolder.swFollow.setChecked(true);
        }else {
            eventsViewHolder.swFollow.setChecked(false);
        }
        eventsViewHolder.swFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventsViewHolder.swFollow.isChecked()){
                    sharedPreference.addFollow(cryptoEvent.getMarketName());
                }else {
                    sharedPreference.removeFollow(cryptoEvent.getMarketName());
                }
            }
        });
    }

    class SettingsViewHolder extends ViewHolderPattern {
        @BindView(R.id.txtMarketName)
        TextView txtMarketName;
        @BindView(R.id.swFollow)
        Switch swFollow;
        SettingsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
