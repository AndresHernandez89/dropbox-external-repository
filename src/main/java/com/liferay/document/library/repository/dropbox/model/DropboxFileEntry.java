package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryFileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

public class DropboxFileEntry extends DropboxObject implements ExtRepositoryFileEntry {

    public DropboxFileEntry(FileMetadata metadata) {
        super(metadata);

        _downloadURL = GetterUtil.getString(metadata.getPathLower());
        _mimeType = GetterUtil.getString("");
        _title = GetterUtil.getString(metadata.getName());
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
