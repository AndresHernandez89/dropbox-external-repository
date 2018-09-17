package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v1.DbxEntry;
import com.liferay.document.library.repository.external.ExtRepositoryFileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

public class DropboxFileEntry extends DropboxObject implements ExtRepositoryFileEntry {

    public DropboxFileEntry(DbxEntry.File file) {
        super(file);

        _downloadURL = GetterUtil.getString(file.name);
        _mimeType = GetterUtil.getString("");
        _title = GetterUtil.getString("testFile");
    }

    @Override
    public String getCheckedOutBy() {
        return StringPool.BLANK;
    }

    public String getDownloadURL() {
        return _downloadURL;
    }

    @Override
    public String getMimeType() {
        return _mimeType;
    }

    @Override
    public String getTitle() {
        return _title;
    }

    private String _downloadURL;
    private String _mimeType;
    private String _title;
}
