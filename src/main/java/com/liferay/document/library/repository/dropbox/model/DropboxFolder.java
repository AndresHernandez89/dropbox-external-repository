package com.liferay.document.library.repository.dropbox.model;

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
    	String root = getExtRepositoryModelKey() != null ? getExtRepositoryModelKey() : "/";
        return _rootFolderKey.equals(root);
    }

    private String _name;
    private final String _rootFolderKey;

}
