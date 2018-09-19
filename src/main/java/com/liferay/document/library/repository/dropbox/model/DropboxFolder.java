package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryFolder;
import com.liferay.portal.kernel.util.GetterUtil;

public class DropboxFolder extends DropboxObject implements ExtRepositoryFolder {

    public DropboxFolder(Metadata folder, String rootFolderKey) {
        super(folder);

        _name = GetterUtil.getString(folder.getName());
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
