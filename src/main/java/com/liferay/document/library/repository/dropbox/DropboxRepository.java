package com.liferay.document.library.repository.dropbox;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry.File;
import com.dropbox.core.v2.DbxAppClientV2;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.ListFilesResult;
import com.dropbox.core.v2.users.FullAccount;
import com.liferay.document.library.repository.dropbox.model.DropboxFileEntry;
import com.liferay.document.library.repository.dropbox.model.DropboxFileVersion;
import com.liferay.document.library.repository.dropbox.model.DropboxFolder;
import com.liferay.document.library.repository.dropbox.model.DropboxObject;
import com.liferay.document.library.repository.external.*;
import com.liferay.document.library.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.RepositoryEntry;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DropboxRepository extends ExtRepositoryAdapter implements ExtRepository {

    private DbxClientV2 clientv2;

    protected DropboxRepository() {
        super(null);
    }

    @Override
    public ExtRepositoryFileEntry addExtRepositoryFileEntry(String extRepositoryParentFolderKey, String mimeType, String title, String description, String changeLog, InputStream inputStream) throws PortalException {
        _log.info("addExtRepositoryFileEntry");
    	return null;
    }

    @Override
    public ExtRepositoryFolder addExtRepositoryFolder(String extRepositoryParentFolderKey, String name, String description) throws PortalException {
        _log.info("addExtRepositoryFolder");
    	return null;
    }

    @Override
    public ExtRepositoryFileVersion cancelCheckOut(String extRepositoryFileEntryKey) throws PortalException {
        _log.info("cancelCheckOut");
        return null;
    }

    @Override
    public void checkInExtRepositoryFileEntry(String extRepositoryFileEntryKey, boolean createMajorVersion, String changeLog) throws PortalException {
        _log.info("checkInExtRepositoryFileEntry");
    }

    @Override
    public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(String extRepositoryFileEntryKey) throws PortalException {
        _log.info("checkOutExtRepositoryFileEntry");
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T copyExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFileEntryKey, String newExtRepositoryFolderKey, String newTitle) throws PortalException {
        _log.info("copyExtRepositoryObject");
    	return null;
    }

    @Override
    public void deleteExtRepositoryObject(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {
        _log.info("deleteExtRepositoryObject");
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
        _log.info("getContentStream");
        return null;
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileVersion extRepositoryFileVersion) throws PortalException {
        _log.info("getContentStream");
        return null;
    }

    @Override
    public ExtRepositoryFileVersion getExtRepositoryFileVersion(ExtRepositoryFileEntry extRepositoryFileEntry, String version) throws PortalException {
        _log.info("getExtRepositoryFileVersion");
//        try {
//
//            Drive.Revisions driveRevisions = client.files().revisions();
//
//            Drive.Revisions.List driveRevisionsList = driveRevisions.list(
//                    extRepositoryFileEntry.getExtRepositoryModelKey());
//
//            RevisionList revisionList = driveRevisionsList.execute();
//
//            List<Revision> revisions = revisionList.getItems();
//
//            int[] versionParts = StringUtil.split(
//                    version, StringPool.PERIOD, 0);
//
//            Revision revision = revisions.get(versionParts[0]);
//
//            return new DropboxFileVersion(
//                    revision, extRepositoryFileEntry.getExtRepositoryModelKey(),
//                    versionParts[0]);
//        }
//        catch (IOException ioe) {
//            _log.error(ioe, ioe);
//
//            throw new SystemException(ioe);
//        }
        return null;
    }

    @Override
    public ExtRepositoryFileVersionDescriptor getExtRepositoryFileVersionDescriptor(String extRepositoryFileVersionKey) {
        _log.info("getExtRepositoryFileVersionDescriptor");

        String[] extRepositoryFileVersionKeyParts = StringUtil.split(
                extRepositoryFileVersionKey, StringPool.COLON);

        String extRepositoryFileEntryKey = extRepositoryFileVersionKeyParts[0];
        String version = extRepositoryFileVersionKeyParts[2];

        return new ExtRepositoryFileVersionDescriptor(
                extRepositoryFileEntryKey, version);
    }

    @Override
    public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
        _log.info("getExtRepositoryFileVersions, fileEntry: " + extRepositoryFileEntry.getTitle());

        List<ExtRepositoryFileVersion> extRepositoryFileVersions = new ArrayList<>();
        int i = 0;

        try {
            ListRevisionsResult result =  clientv2.files().listRevisions("/" + extRepositoryFileEntry.getTitle());
            for (Metadata metadata : result.getEntries()) {

                _log.info(metadata.toStringMultiline());
                extRepositoryFileVersions.add(
                        new DropboxFileVersion(metadata,
                                extRepositoryFileEntry.getExtRepositoryModelKey(), i + 1));
                i++;
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }

		Collections.reverse(extRepositoryFileVersions);

		return extRepositoryFileVersions;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {
        _log.info("getExtRepositoryObject");
    	return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey, String title) throws PortalException {
        _log.info("getExtRepositoryObject");
    	return null;
    }

    @Override
    public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        _log.info("getExtRepositoryObjects, path: " + extRepositoryFolderKey);

        ListFolderResult result = null;
        try {
            result = clientv2.files().listFolderBuilder("").withRecursive(true).withIncludeMediaInfo(true).start();
        } catch (DbxException e) {
            e.printStackTrace();
        }

        List<Metadata> fileList = new ArrayList<>();
        for (Metadata metadata : result.getEntries()) {
            fileList.add(metadata);
        }

		StringBundler sb = new StringBundler();

		if (extRepositoryFolderKey != null) {
			sb.append("'");
			sb.append(extRepositoryFolderKey);
			sb.append("' in parents and ");
		}

		if (!extRepositoryObjectType.equals(
				ExtRepositoryObjectType.OBJECT)) {

			sb.append("mimeType");

			if (extRepositoryObjectType.equals(
					ExtRepositoryObjectType.FILE)) {

				sb.append(" != '");
			}
			else {
				sb.append(" = '");
			}

			sb.append(_FOLDER_MIME_TYPE);
			sb.append("' and ");
		}

		sb.append("trashed = false");

		_log.info(sb.toString());

		List<T> extRepositoryObjects = new ArrayList<>();

		//GoogleDriveCache googleDriveCache = GoogleDriveCache.getInstance();

		for (Metadata metadata : fileList) {
            if (metadata instanceof FolderMetadata) {
				extRepositoryObjects.add(
					(T)new DropboxFolder(metadata, getRootFolderKey()));
			}else{
				extRepositoryObjects.add((T)new DropboxFileEntry(((FileMetadata) metadata)));
			}

			//googleDriveCache.put(file);
		}
        _log.info(extRepositoryObjects.size());
		return extRepositoryObjects;
    }

    @Override
    public int getExtRepositoryObjectsCount(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        _log.info("getExtRepositoryObjectsCount, path: " + extRepositoryFolderKey);
        ListFolderResult result = null;
        try {
            if(extRepositoryFolderKey.equals("/")){
                result = clientv2.files().listFolder("");
            }else{
                result = clientv2.files().listFolder(extRepositoryFolderKey);
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                _log.info(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            try {
                result = clientv2.files().listFolderContinue(result.getCursor());
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        _log.info("Size: " + result.getEntries().size());
         return result.getEntries().size();
//        return 0;
    }

    @Override
    public ExtRepositoryFolder getExtRepositoryParentFolder(ExtRepositoryObject extRepositoryObject) throws PortalException {
        _log.info("getExtRepositoryParentFolder");
        return null;
    }

    @Override
    public String getRootFolderKey() throws PortalException {
    	_log.info("getRootFolderKey");
        return "/";
    }

    @Override
    public List<String> getSubfolderKeys(String extRepositoryFolderKey, boolean recurse) throws PortalException {
        _log.info("getSubfolderKeys");
        return null;
    }

    @Override
    public void initRepository(UnicodeProperties typeSettingsProperties, CredentialsProvider credentialsProvider) throws PortalException {
        UnicodeProperties typeProps = getTypeSettingsProperties();
        _log.info("Init repository, token: " + typeProps.getProperty("DROPBOX_TOKEN"));
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/liferay").build();
        clientv2 = new DbxClientV2(config, typeProps.getProperty("DROPBOX_TOKEN"));
        FullAccount account = null;
        try {
            account = clientv2.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        _log.info(account.getName().getDisplayName());
    }

    @Override
    public <T extends ExtRepositoryObject> T moveExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryObjectKey, String newExtRepositoryFolderKey, String newTitle) throws PortalException {
        return null;
    }

    @Override
    public List<ExtRepositorySearchResult<?>> search(SearchContext searchContext, Query query, ExtRepositoryQueryMapper extRepositoryQueryMapper) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFileEntry updateExtRepositoryFileEntry(String extRepositoryFileEntryKey, String mimeType, InputStream inputStream) throws PortalException {
        return null;
    }

    private static final String _FOLDER_MIME_TYPE =
    		"folder";

    public DbxClientV2 getClientV2() {
        return clientv2;
    }

    private static final Log _log = LogFactoryUtil.getLog(DropboxRepository.class);

}
