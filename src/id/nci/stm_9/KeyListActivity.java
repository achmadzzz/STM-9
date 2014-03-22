package id.nci.stm_9;

//import org.sufficientlysecure.keychain.Constants;
//import org.sufficientlysecure.keychain.Id;
import id.nci.stm_9.DialogFragmentWorkaround;
import id.nci.stm_9.KeychainIntentService;
import id.nci.stm_9.KeychainIntentServiceHandler;
import id.nci.stm_9.DeleteKeyDialogFragment;
import id.nci.stm_9.FileDialogFragment;
import id.nci.stm_9.Log;
import id.nci.stm_9.R;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Messenger;
import android.os.Message;
import android.widget.Toast;
import android.os.Handler;
import android.app.ProgressDialog;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class KeyListActivity extends SherlockFragmentActivity {

    protected String mExportFilename = Environment.getExternalStorageDirectory() + "/";

    protected String mImportData;
    protected boolean mDeleteAfterImport = false;

    protected int mKeyType;

    FileDialogFragment mFileDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        handleActions(getIntent());
    }

    // TODO: needed?
    // @Override
    // protected void onNewIntent(Intent intent) {
    // super.onNewIntent(intent);
    // handleActions(intent);
    // }

    protected void handleActions(Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if (extras == null) {
            extras = new Bundle();
        }

        /**
         * Android Standard Actions
         */
        String searchString = null;
        if (Intent.ACTION_SEARCH.equals(action)) {
            searchString = extras.getString(SearchManager.QUERY);
            if (searchString != null && searchString.trim().length() == 0) {
                searchString = null;
            }
        }

        // if (searchString == null) {
        // mFilterLayout.setVisibility(View.GONE);
        // } else {
        // mFilterLayout.setVisibility(View.VISIBLE);
        // mFilterInfo.setText(getString(R.string.filterInfo, searchString));
        // }
        //
        // if (mListAdapter != null) {
        // mListAdapter.cleanup();
        // }
        // mListAdapter = new KeyListAdapter(this, searchString);
        // mList.setAdapter(mListAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//        case Id.request.filename: {
          case 0x00007003: {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    String path = data.getData().getPath();
                    Log.d("Stm-9", "path=" + path);

                    // set filename used in export/import dialogs
                    mFileDialog.setFilename(path);
                } catch (NullPointerException e) {
                    Log.e("Stm-9", "Nullpointer while retrieving path!", e);
                }
            }
            return;
        }

        default: {
            break;
        }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO: reimplement!
        // menu.add(3, Id.menu.option.search, 0, R.string.menu_search)
        // .setIcon(R.drawable.ic_menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        menu.add(0, Id.menu.o;ption.import_from_file, 5, R.string.menu_import_from_file)
        menu.add(0, 0x21070020, 5, R.string.menu_import_from_file)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        menu.add(0, Id.menu.option.export_keys, 6, R.string.menu_export_keys).setShowAsAction(
        menu.add(0, 0x21070007, 6, R.string.menu_export_keys).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case android.R.id.home:
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

//        case Id.menu.option.import_from_file: {
        case 0x21070020: {
            Intent intentImportFromFile = new Intent(this, ImportKeysActivity.class);
            intentImportFromFile.setAction(ImportKeysActivity.ACTION_IMPORT_KEY_FROM_FILE);
            startActivityForResult(intentImportFromFile, 0);
            return true;
        }

        case 0x21070007: {
            showExportKeysDialog(-1);
            return true;
        }

        // case Id.menu.option.search:
        // startSearch("", false, null, false);
        // return true;

        default: {
            return super.onOptionsItemSelected(item);
        }
        }
    }


    /**
     * Show dialog where to export keys
     * 
     * @param keyRingMasterKeyId
     *            if -1 export all keys
     */
    public void showExportKeysDialog(final long keyRingMasterKeyId) {
        // Message is received after file is selected
        Handler returnHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == FileDialogFragment.MESSAGE_OKAY) {
                    Bundle data = message.getData();
                    mExportFilename = data.getString(FileDialogFragment.MESSAGE_DATA_FILENAME);

                    exportKeys(keyRingMasterKeyId);
                }
            }
        };

        // Create a new Messenger for the communication back
        final Messenger messenger = new Messenger(returnHandler);

        DialogFragmentWorkaround.INTERFACE.runnableRunDelayed(new Runnable() {
            public void run() {
                String title = null;
                if (keyRingMasterKeyId != -1) {
                    // single key export
                    title = getString(R.string.title_export_key);
                } else {
                    title = getString(R.string.title_export_keys);
                }

                String message = null;
                if (mKeyType == 0x21070001) {
                    message = getString(R.string.specify_file_to_export_to);
                } else {
                    message = getString(R.string.specify_file_to_export_secret_keys_to);
                }

                mFileDialog = FileDialogFragment.newInstance(messenger, title, message,
                        mExportFilename, null, 0x00007003);

                mFileDialog.show(getSupportFragmentManager(), "fileDialog");
            }
        });
    }


    /**
     * Show dialog to delete key
     * 
     * @param keyRingId
     */
    public void showDeleteKeyDialog(long keyRingId) {
        // Message is received after key is deleted
        Handler returnHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == DeleteKeyDialogFragment.MESSAGE_OKAY) {
                    // no further actions needed
                }
            }
        };

        // Create a new Messenger for the communication back
        Messenger messenger = new Messenger(returnHandler);

        DeleteKeyDialogFragment deleteKeyDialog = DeleteKeyDialogFragment.newInstance(messenger,
                keyRingId, mKeyType);

        deleteKeyDialog.show(getSupportFragmentManager(), "deleteKeyDialog");
    }

    /**
     * Export keys
     * 
     * @param keyRingMasterKeyId
     *            if -1 export all keys
     */
    public void exportKeys(long keyRingMasterKeyId) {
        Log.d("Stm-9", "exportKeys started");

        // Send all information needed to service to export key in other thread
        Intent intent = new Intent(this, KeychainIntentService.class);

        intent.setAction(KeychainIntentService.ACTION_EXPORT_KEYRING);

        // fill values for this action
        Bundle data = new Bundle();

        data.putString(KeychainIntentService.EXPORT_FILENAME, mExportFilename);
        data.putInt(KeychainIntentService.EXPORT_KEY_TYPE, mKeyType);

        if (keyRingMasterKeyId == -1) {
            data.putBoolean(KeychainIntentService.EXPORT_ALL, true);
        } else {
            data.putLong(KeychainIntentService.EXPORT_KEY_RING_MASTER_KEY_ID, keyRingMasterKeyId);
        }

        intent.putExtra(KeychainIntentService.EXTRA_DATA, data);

        // Message is received after exporting is done in ApgService
        KeychainIntentServiceHandler exportHandler = new KeychainIntentServiceHandler(this,
                R.string.progress_exporting, ProgressDialog.STYLE_HORIZONTAL) {
            public void handleMessage(Message message) {
                // handle messages by standard ApgHandler first
                super.handleMessage(message);

                if (message.arg1 == KeychainIntentServiceHandler.MESSAGE_OKAY) {
                    // get returned data bundle
                    Bundle returnData = message.getData();

                    int exported = returnData.getInt(KeychainIntentService.RESULT_EXPORT);
                    String toastMessage;
                    if (exported == 1) {
                        toastMessage = getString(R.string.key_exported);
                    } else if (exported > 0) {
                        toastMessage = getString(R.string.keys_exported, exported);
                    } else {
                        toastMessage = getString(R.string.no_keys_exported);
                    }
                    Toast.makeText(KeyListActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                }
            };
        };

        // Create a new Messenger for the communication back
        Messenger messenger = new Messenger(exportHandler);
        intent.putExtra(KeychainIntentService.EXTRA_MESSENGER, messenger);

        // show progress dialog
        exportHandler.showProgressDialog(this);

        // start service with intent
        startService(intent);
    }
}