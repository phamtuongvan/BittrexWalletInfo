package pamobile.co.bittrexwalletinfo.DependencyInjection;

import javax.inject.Singleton;
import dagger.Component;
import pamobile.co.bittrexwalletinfo.CryptoEvents.CryptoEventFragment;
import pamobile.co.bittrexwalletinfo.HomeActivity;
import pamobile.co.bittrexwalletinfo.Service.BackgroundService;
import pamobile.co.bittrexwalletinfo.Settings.SettingsFragment;
import pamobile.co.bittrexwalletinfo.Wallet.WalletFragment;

/**
 * Created by Dev02 on 3/1/2017.
 */
@Singleton
@Component(modules = {GeneralModule.class})
public interface GeneralComponent {
    void Inject(HomeActivity activity);
    void Inject(CryptoEventFragment fragment);

    void Inject(BackgroundService backgroundService);

    void Inject(WalletFragment walletFragment);

    void Inject(SettingsFragment settingsFragment);
}