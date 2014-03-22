/*
 * Copyright (C) 2012-2013 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package id.nci.stm_9;

//import org.sufficientlysecure.keychain.Id;
import id.nci.stm_9.ProviderHelper;
import id.nci.stm_9.ExpandableListFragment;
import id.nci.stm_9.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class KeyListFragment extends ExpandableListFragment {
    protected KeyListActivity mKeyListActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mKeyListActivity = (KeyListActivity) getActivity();

        // register long press context menu
        registerForContextMenu(getListView());

        // Give some text to display if there is no data. In a real
        // application this would come from a resource.
        setEmptyText(getString(R.string.list_empty));
    }

    /**
     * Context Menu on Long Click
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0x21070001, 5, R.string.menu_export_key);
        menu.add(0, 0x21070002, 111, R.string.menu_delete_key);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        ExpandableListContextMenuInfo expInfo = (ExpandableListContextMenuInfo) item.getMenuInfo();

        // expInfo.id would also return row id of childs, but we always want to get the row id of
        // the group item, thus we are using the following way
        int groupPosition = ExpandableListView.getPackedPositionGroup(expInfo.packedPosition);
        long keyRingRowId = getExpandableListAdapter().getGroupId(groupPosition);

        switch (item.getItemId()) {
        case 0x21070001:
            long masterKeyId = ProviderHelper.getPublicMasterKeyId(mKeyListActivity, keyRingRowId);
            if (masterKeyId == -1) {
                masterKeyId = ProviderHelper.getSecretMasterKeyId(mKeyListActivity, keyRingRowId);
            }

            mKeyListActivity.showExportKeysDialog(masterKeyId);
            return true;

        case 0x21070002:
            mKeyListActivity.showDeleteKeyDialog(keyRingRowId);
            return true;

        default:
            return super.onContextItemSelected(item);

        }
    }

}
