package id.nci.stm_9;

//import org.sufficientlysecure.keychain.Constants;
//import id.nci.stm_9.Id;
import id.nci.stm_9.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class KeyListSecretActivity extends KeyListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mKeyType = 0x21070002;

        setContentView(R.layout.key_list_secret_activity);

        mExportFilename = Environment.getExternalStorageDirectory() + "/STM-9" + "/secexport.asc";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 0x21070002, 1, R.string.menu_create_key).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(1, 0x21070024, 2, R.string.menu_create_key_expert).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0x21070002: {
            createKey();
            return true;
        }

        case 0x21070024: {
            createKeyExpert();
            return true;
        }

        default: {
            return super.onOptionsItemSelected(item);
        }
        }
    }

    private void createKey() {
        Intent intent = new Intent(this, EditKeyActivity.class);
        intent.setAction(EditKeyActivity.ACTION_CREATE_KEY);
        intent.putExtra(EditKeyActivity.EXTRA_GENERATE_DEFAULT_KEYS, true);
        intent.putExtra(EditKeyActivity.EXTRA_USER_IDS, ""); // show user id view
        startActivityForResult(intent, 0);
    }

    private void createKeyExpert() {
        Intent intent = new Intent(this, EditKeyActivity.class);
        intent.setAction(EditKeyActivity.ACTION_CREATE_KEY);
        startActivityForResult(intent, 0);
    }

    void editKey(long masterKeyId, boolean masterCanSign) {
        Intent intent = new Intent(this, EditKeyActivity.class);
        intent.setAction(EditKeyActivity.ACTION_EDIT_KEY);
        intent.putExtra(EditKeyActivity.EXTRA_MASTER_KEY_ID, masterKeyId);
        intent.putExtra(EditKeyActivity.EXTRA_MASTER_CAN_SIGN, masterCanSign);
        startActivityForResult(intent, 0);
    }

}
