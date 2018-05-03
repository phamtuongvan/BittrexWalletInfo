package PACore.Listener;

/**
 * Created by Dev04 on 8/18/2017.
 */

public interface NetworkAsyncListener {
    void OnSuccess(String response);
    void OnError(String errorMessage);
}
