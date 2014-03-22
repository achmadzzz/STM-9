package id.nci.stm_9;

//import java.io.File;
//import java.security.Security;

//import org.spongycastle.jce.provider.BouncyCastleProvider;

import id.nci.stm_9.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;

//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Environment;
import android.view.View;
//import android.app.Application;
//import android.widget.Toast;

public class MainMenu extends SherlockActivity {

	public void manageKeysOnClick(View view) {
		startActivityForResult(new Intent(this, KeyListPublicActivity.class), 0);	
	}
	
	public void myKeysOnClick(View view) {
		startActivityForResult(new Intent(this, KeyListSecretActivity.class), 0);
		
	}
	
	public void encryptOnClick(View view) {
		Intent intent = new Intent(MainMenu.this, EncryptActivity.class);
		intent.setAction(EncryptActivity.ACTION_ENCRYPT);
		startActivityForResult(intent, 0);
	
	}
	
	public void decryptOnClick(View view) {
		Intent intent = new Intent(MainMenu.this, DecryptActivity.class);
		intent.setAction(DecryptActivity.ACTION_DECRYPT);
		startActivityForResult(intent, 0);
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);
        
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 0x21070008, 0, R.string.menu_preferences)
//                .setIcon(R.drawable.ic_menu_settings)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        menu.add(0, 0x21070023, 0, R.string.menu_api_app_settings)
//                .setIcon(R.drawable.ic_menu_settings)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        return true;
//    }
}
