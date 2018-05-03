package pamobile.co.bittrexwalletinfo.Service.Interface;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Created by Dev02 on 4/13/2017.
 */

public interface IVolleyService {
    RequestQueue getRequestQueue(Context context);
}
