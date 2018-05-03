package pamobile.co.bittrexwalletinfo.Service.Interface;

import android.content.Context;

import pamobile.co.bittrexwalletinfo.BittrexAPI.Bittrex;

/**
 * Created by admin on 1/26/18.
 */

public interface ILocalDataService {
    Bittrex GetBittrexAPI(Context context);
    void SetBittrexAPI(Bittrex bittrex);
}
