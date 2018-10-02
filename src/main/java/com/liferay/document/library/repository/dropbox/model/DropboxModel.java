package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DropboxModel implements ExtRepositoryModel {

    public DropboxModel(Date createDateTime, String extRepositoryModelKey, long size,
                        String owner){
        _createDate = createDateTime;
        _extRepositoryModelKey = extRepositoryModelKey;
        _size = size;
        _owner = owner;
    }

    public DropboxModel(Metadata metadata){
        Date createDateTime = new Date();

        _createDate = createDateTime;

        _extRepositoryModelKey = metadata.getPathLower();

        if (metadata instanceof FileMetadata) {
            _size = GetterUtil.getLong(((FileMetadata)metadata).getSize());
        }

        List<String> ownerNames = new ArrayList<>();

        if (!ownerNames.isEmpty()) {
            _owner = "Test";
        }
    }

    @Override
    public Date getCreateDate() {
        return _createDate;
    }

    @Override
    public String getExtRepositoryModelKey() {
        return _extRepositoryModelKey;
    }

    @Override
    public String getOwner() {
        return _owner;
    }

    @Override
    public long getSize() {
        return _size;
    }

    private Date _createDate;
    private String _extRepositoryModelKey;
    private String _owner = StringPool.BLANK;
    private long _size;

}
