package pamobile.co.bittrexwalletinfo.DependencyInjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pamobile.co.bittrexwalletinfo.Service.Implement.LocalDataService;
import pamobile.co.bittrexwalletinfo.Service.Implement.VolleyService;
import pamobile.co.bittrexwalletinfo.Service.Interface.ILocalDataService;
import pamobile.co.bittrexwalletinfo.Service.Interface.IVolleyService;

/**
 * Created by Dev02 on 3/1/2017.
 */
@Module
public class GeneralModule {
    public GeneralModule() {
    }

    @Provides
    @Singleton
    IVolleyService provideVolleyService() {
        return new VolleyService();
    }

    @Provides
    @Singleton
    ILocalDataService provideLocalDataService() {
        return new LocalDataService();
    }


}
