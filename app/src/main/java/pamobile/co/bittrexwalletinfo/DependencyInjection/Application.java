package pamobile.co.bittrexwalletinfo.DependencyInjection;

import java.util.HashMap;

/**
 * Created by Dev02 on 4/13/2017.
 */

public class Application extends android.app.Application {
    GeneralComponent generalComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        generalComponent = DaggerGeneralComponent.builder()
                .generalModule(new GeneralModule()).build();
    }

    public GeneralComponent getGeneralComponent() {
        return generalComponent;
    }


    public Application() {
        super();
    }
}
