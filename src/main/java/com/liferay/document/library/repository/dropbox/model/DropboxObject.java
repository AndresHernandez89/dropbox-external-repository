package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryObject;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;

import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.ROLE_OWNER;
import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.ROLE_WRITER;

public class DropboxObject extends DropboxModel implements ExtRepositoryObject {

    public DropboxObject(Metadata metadata) {
        super(metadata);

        _description = GetterUtil.getString(metadata.getName());

        if (metadata instanceof FileMetadata) {
            //TODO: check if the extension is needed by file preview
            _extension = GetterUtil.getString("txt");
        }

        //TODO: get creation date from Dropbox SDK
        _createDateTime = new Date();

        //TODO: get creation date from Dropbox SDK
        _modifiedDate = new Date();

        //TODO: get the permission/role from Dropbox SDK
        _permission = ROLE_OWNER;
    }

    @Override
    public boolean containsPermission(
            ExtRepositoryPermission extRepositoryPermission) {

        if (extRepositoryPermission.equals(ExtRepositoryPermission.ACCESS) ||
                extRepositoryPermission.equals(ExtRepositoryPermission.VIEW)) {

            return true;
        }
        else if (extRepositoryPermission.equals(
                ExtRepositoryPermission.ADD_DOCUMENT) ||
                extRepositoryPermission.equals(
                        ExtRepositoryPermission.ADD_FOLDER) ||
                extRepositoryPermission.equals(
                        ExtRepositoryPermission.ADD_SUBFOLDER) ||
                extRepositoryPermission.equals(
                        ExtRepositoryPermission.UPDATE)) {

            return isOwnerOrWriter(_permission);
        }
        else if (extRepositoryPermission.equals(
                ExtRepositoryPermission.DELETE)) {

            return isOwner(_permission);
        }

        return false;
    }

    @Override
    public String getDescription() {
        return _description;
    }

    @Override
    public String getExtension() {
        return _extension;
    }

    @Override
    public Date getModifiedDate() {
        return _createDateTime;
    }

    public Date getCreateDateTime() {
        return _modifiedDate;
    }

    protected boolean isOwner(String role) {
        if (role.equals(ROLE_OWNER)) {
            return true;
        }

        return false;
    }

    protected boolean isOwnerOrWriter(String role) {
        if (role.equals(ROLE_OWNER) || role.equals(ROLE_WRITER)) {
            return true;
        }

        return false;
    }

    private String _description;
    private String _extension;
    private Date _modifiedDate;
    private Date _createDateTime;
    private String _permission;

}
