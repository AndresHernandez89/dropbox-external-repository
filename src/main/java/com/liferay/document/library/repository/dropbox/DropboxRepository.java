package com.liferay.document.library.repository.dropbox;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.liferay.document.library.repository.external.*;
import com.liferay.document.library.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.RepositoryEntry;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DropboxRepository extends ExtRepositoryAdapter implements ExtRepository {

    private DbxClientV2 client;

    protected DropboxRepository() {
        super(null);
    }

    @Override
    public ExtRepositoryFileEntry addExtRepositoryFileEntry(String extRepositoryParentFolderKey, String mimeType, String title, String description, String changeLog, InputStream inputStream) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFolder addExtRepositoryFolder(String extRepositoryParentFolderKey, String name, String description) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFileVersion cancelCheckOut(String extRepositoryFileEntryKey) throws PortalException {
        return null;
    }

    @Override
    public void checkInExtRepositoryFileEntry(String extRepositoryFileEntryKey, boolean createMajorVersion, String changeLog) throws PortalException {

    }

    @Override
    public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(String extRepositoryFileEntryKey) throws PortalException {
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T copyExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFileEntryKey, String newExtRepositoryFolderKey, String newTitle) throws PortalException {
        return null;
    }

    @Override
    public void deleteExtRepositoryObject(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {

    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
        return null;
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileVersion extRepositoryFileVersion) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFileVersion getExtRepositoryFileVersion(ExtRepositoryFileEntry extRepositoryFileEntry, String version) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFileVersionDescriptor getExtRepositoryFileVersionDescriptor(String extRepositoryFileVersionKey) {
        return null;
    }

    @Override
    public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey, String title) throws PortalException {
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        System.out.println("getExtRepositoryObjects");
        return null;
    }

    @Override
    public int getExtRepositoryObjectsCount(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("");
        } catch (DbxException e) {
            e.printStackTrace();
        }

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            try {
                result = client.files().listFolderContinue(result.getCursor());
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Size: " + result.getEntries().size());
         return result.getEntries().size();
//        return 0;
    }

    @Override
    public ExtRepositoryFolder getExtRepositoryParentFolder(ExtRepositoryObject extRepositoryObject) throws PortalException {
        return null;
    }

    @Override
    public String getRootFolderKey() throws PortalException {
        return null;
    }

    @Override
    public List<String> getSubfolderKeys(String extRepositoryFolderKey, boolean recurse) throws PortalException {
        return null;
    }

    @Override
    public void initRepository(UnicodeProperties typeSettingsProperties, CredentialsProvider credentialsProvider) throws PortalException {
        //Create session
        UnicodeProperties typeProps = getTypeSettingsProperties();
        System.out.println("Init repository");
        System.out.println(typeProps.getProperty("DROPBOX_TOKEN"));
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/liferay").build();
        client = new DbxClientV2(config, typeProps.getProperty("DROPBOX_TOKEN"));
        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());
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

    @Override
    public List<RepositoryEntry> getFoldersAndFileEntriesAndFileShortcuts(long folderId, int status, String[] mimetypes,
                                                                          boolean includeMountFolders, int start, int end,
                                                                          OrderByComparator<?> obc)
            throws PortalException {
        RepositoryEntry re = new RepositoryEntry() {
            @Override
            public long getCompanyId() {
                return 0;
            }

            @Override
            public Date getCreateDate() {
                return new Date();
            }

            @Override
            public long getGroupId() {
                return 0;
            }

            @Override
            public Date getModifiedDate() {
                return null;
            }

            @Override
            public long getUserId() {
                return 0;
            }

            @Override
            public String getUserName() {
                return null;
            }

            @Override
            public String getUserUuid() {
                return null;
            }
        };
        List<RepositoryEntry> list = new ArrayList<>();
        list.add(re);
        return list;
    }

    public DbxClientV2 getClient() {
        return client;
    }

}
