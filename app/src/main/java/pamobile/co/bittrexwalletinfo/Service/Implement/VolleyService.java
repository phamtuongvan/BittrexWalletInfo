package pamobile.co.bittrexwalletinfo.Service.Implement;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import pamobile.co.bittrexwalletinfo.Service.Interface.IVolleyService;

/**
 * Created by Dev02 on 4/13/2017.
 */

public class VolleyService implements IVolleyService {
    private static final String TAG = "VolleyService";
    private RequestQueue mRequestQueue;

    public VolleyService() {
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }
}