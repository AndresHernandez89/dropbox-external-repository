package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryObject;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;

public class DropboxObject extends DropboxModel implements ExtRepositoryObject {

    public DropboxObject(Metadata metadata) {
        super(metadata);

        _description = GetterUtil.getString(metadata.getName());

        if (metadata instanceof FileMetadata) {
            _extension = GetterUtil.getString("txt");
        }

        Date createDateTime = new Date();

        _modifiedDate = new Date();

//        _permission = "view";
    }

    @Override
    public boolean containsPermission(
            ExtRepositoryPermission extRepositoryPermission) {

        System.out.println("containsPermission");
        String role = "owner";

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

            return isOwnerOrWriter(role);
        }
        else if (extRepositoryPermission.equals(
                ExtRepositoryPermission.DELETE)) {

            return isOwner(role);
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
        return _modifiedDate;
    }

    protected boolean isOwner(String role) {
        if (role.equals("owner")) {
            return true;
        }

        return false;
    }

    protected boolean isOwnerOrWriter(String role) {
        if (role.equals("owner") || role.equals("writer")) {
            return true;
        }

        return false;
    }

    private String _description;
    private String _extension;
    private Date _modifiedDate;
//    private String _permission;

}
