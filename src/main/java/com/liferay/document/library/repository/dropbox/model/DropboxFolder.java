package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v1.DbxEntry;
import com.liferay.document.library.repository.external.ExtRepositoryFolder;
import com.liferay.portal.kernel.util.GetterUtil;

public class DropboxFolder extends DropboxObject implements ExtRepositoryFolder {

    public DropboxFolder(DbxEntry.File file, String rootFolderKey) {
        super(file);

        _name = GetterUtil.getString("testFile");
        _rootFolderKey = rootFolderKey;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean isRoot() {
        return _rootFolderKey.equals(getExtRepositoryModelKey());
    }

    private String _name;
    private final String _rootFolderKey;

}
