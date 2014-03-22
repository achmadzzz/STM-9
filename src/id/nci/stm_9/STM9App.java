package id.nci.stm_9;

import java.io.File;
import java.security.Provider;
//import java.security.Provider;
import java.security.Security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import id.nci.stm_9.Log;
import id.nci.stm_9.PRNGFixes;

import android.app.Application;
import android.os.Environment;

public class STM9App extends Application {

    //Called when the application is starting, before any activity, service, or receiver objects
     
    @Override
    public void onCreate() {
        super.onCreate();

        //Sets Bouncy (Spongy) Castle as preferred security provider
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        
        //apply rng fixes
        PRNGFixes.apply();
        Log.d("stm-9", "Bouncy Castle set and PRNG Fixes applied!");

        if (BuildConfig.DEBUG) {
            Provider[] providers = Security.getProviders();
            Log.d("stm-9", "Installed Security Providers:");
            for (Provider p : providers) {
                Log.d("stm-9", "provider class: " + p.getClass().getName());
            }
        }
        // Create APG directory on sdcard if not existing
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      	  	File dir = new File(Environment.getExternalStorageDirectory() + "/STM-9");
            if (!dir.exists() && !dir.mkdirs()) {
            }
        }
    }
}
