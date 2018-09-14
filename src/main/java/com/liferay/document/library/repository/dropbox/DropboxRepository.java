package com.liferay.document.library.repository.dropbox;


import com.liferay.document.library.repository.external.*;
import com.liferay.document.library.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.InputStream;
import java.util.List;

public class DropboxRepository extends ExtRepositoryAdapter implements ExtRepository {


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
        return null;
    }

    @Override
    public int getExtRepositoryObjectsCount(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        return 0;
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

}
