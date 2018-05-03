package pamobile.co.bittrexwalletinfo.Service.Implement;

import android.content.Context;

import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;
import pamobile.co.bittrexwalletinfo.BittrexAPI.UserCredentials;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.SharedPreference;

/**
 * Created by admin on 1/26/18.
 */

public class LocalDataService implements ILocalDataService {
    Bittrex bittrex;
    @Override
    public Bittrex GetBittrexAPI(Context context) {
        if(bittrex == null){
            SharedPreference sharedPreference = new SharedPreference(context);
            bittrex = new Bittrex(sharedPreference.getApi()[0], sharedPreference.getApi()[1]);
        }
        return bittrex;
    }

    @Override
    public void SetBittrexAPI(Bittrex bittrex) {
        this.bittrex = bittrex;
    }
}
