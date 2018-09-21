package com.liferay.document.library.repository.dropbox;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import com.liferay.document.library.repository.dropbox.model.DropboxFileEntry;
import com.liferay.document.library.repository.dropbox.model.DropboxFileVersion;
import com.liferay.document.library.repository.dropbox.model.DropboxFolder;
import com.liferay.document.library.repository.dropbox.model.DropboxObject;
import com.liferay.document.library.repository.external.*;
import com.liferay.document.library.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.BLANK;
import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.COLON;
import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.FORWARD_SLASH;

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
                extRepositoryFileVersionKey, COLON);

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
            ListRevisionsResult result =  clientv2.files().listRevisions(FORWARD_SLASH + extRepositoryFileEntry.getTitle());
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
        _log.info("getExtRepositoryObject 1: extRepositoryObjectKey" + extRepositoryObjectKey + ", extRepositoryObjectType: " + extRepositoryObjectType);

        Metadata metadata = Metadata.newBuilder("").build();
        try {
            metadata = clientv2.files().getMetadata(extRepositoryObjectKey);
        } catch (DbxException e) {
            e.printStackTrace();
        }

        if (metadata instanceof FolderMetadata){
            return (T)new DropboxFolder(
                    metadata, getRootFolderKey());
        }else{
            return (T)new DropboxFileEntry((FileMetadata) metadata);
        }
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey, String title) throws PortalException {
        _log.info("getExtRepositoryObject 2");
    	return null;
    }

    @Override
    public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        _log.info("getExtRepositoryObjects, path: " + extRepositoryFolderKey);

        List<T> extRepositoryObjects = new ArrayList<>();

        try {
            String path = BLANK;
            if(!extRepositoryFolderKey.equals(FORWARD_SLASH)){
                path = extRepositoryFolderKey;
            }

            ListFolderResult result = clientv2.files().listFolderBuilder(path)
                    .withRecursive(true)
                    .withIncludeMediaInfo(true)
                    .start();


            List<Metadata> fileList = new ArrayList<>();
            for (Metadata metadata : result.getEntries()) {
                fileList.add(metadata);
            }

            //TODO: finish cache stuff
            //DropboxCache dropboxCache = DropboxCache.getInstance();

            for (Metadata metadata : fileList) {
                if (metadata instanceof FolderMetadata) {
                    extRepositoryObjects.add(
                        (T)new DropboxFolder(metadata, getRootFolderKey()));
                }else{
                    extRepositoryObjects.add((T)new DropboxFileEntry(((FileMetadata) metadata)));
                }

                //dropboxCache.put(metadata);
            }
        } catch (DbxException e) {
            _log.error(e.getMessage());
            e.printStackTrace();
        }

        _log.info(extRepositoryObjects.size());
		return extRepositoryObjects;
    }

    @Override
    public int getExtRepositoryObjectsCount(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        _log.info("getExtRepositoryObjectsCount, path: " + extRepositoryFolderKey);

        int size = 0;
        try {
            ListFolderResult result;

            if(extRepositoryFolderKey.equals(FORWARD_SLASH)){
                result = clientv2.files().listFolder(BLANK);
            }else{
                result = clientv2.files().listFolder(extRepositoryFolderKey);
            }

            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    _log.info(metadata.getPathLower());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = clientv2.files().listFolderContinue(result.getCursor());

            }

            size = result.getEntries().size();

        } catch (DbxException e) {
            e.printStackTrace();
        }

        _log.info("Size: " + size);

        return size;
    }

    @Override
    public ExtRepositoryFolder getExtRepositoryParentFolder(ExtRepositoryObject extRepositoryObject) throws PortalException {
        _log.info("getExtRepositoryParentFolder, key: " + extRepositoryObject.getExtRepositoryModelKey());

        extRepositoryObject.getExtRepositoryModelKey();
//        clientv2.files().
//        try {
//            Drive drive = getDrive();
//
//            File file = getFile(
//                    drive, extRepositoryObject.getExtRepositoryModelKey());
//
//            List<ParentReference> parentReferences = file.getParents();
//
//            if (!parentReferences.isEmpty()) {
//                ParentReference parentReference = parentReferences.get(0);
//
//                File parentFile = getFile(drive, parentReference.getId());
//
//                return new GoogleDriveFolder(parentFile, getRootFolderKey());
//            }
//        }
//        catch (IOException ioe) {
//            //_log.error(ioe, ioe);
//        }
        return null;
    }

    @Override
    public String getRootFolderKey() throws PortalException {
    	_log.info("getRootFolderKey");
        return FORWARD_SLASH;
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

    public DbxClientV2 getClientV2() {
        return clientv2;
    }

    private static final Log _log = LogFactoryUtil.getLog(DropboxRepository.class);

}
