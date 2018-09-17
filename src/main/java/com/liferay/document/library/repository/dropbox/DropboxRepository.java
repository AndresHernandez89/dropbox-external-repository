package com.liferay.document.library.repository.dropbox;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxEntry.File;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
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
import com.liferay.portal.kernel.repository.model.RepositoryEntry;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DropboxRepository extends ExtRepositoryAdapter implements ExtRepository {

    private DbxClientV2 client;

    protected DropboxRepository() {
        super(null);
    }

    @Override
    public ExtRepositoryFileEntry addExtRepositoryFileEntry(String extRepositoryParentFolderKey, String mimeType, String title, String description, String changeLog, InputStream inputStream) throws PortalException {
    	System.out.println("addExtRepositoryFileEntry");
    	return null;
    }

    @Override
    public ExtRepositoryFolder addExtRepositoryFolder(String extRepositoryParentFolderKey, String name, String description) throws PortalException {
    	System.out.println("addExtRepositoryFolder");
    	return null;
    }

    @Override
    public ExtRepositoryFileVersion cancelCheckOut(String extRepositoryFileEntryKey) throws PortalException {
    	  System.out.println("cancelCheckOut");
        return null;
    }

    @Override
    public void checkInExtRepositoryFileEntry(String extRepositoryFileEntryKey, boolean createMajorVersion, String changeLog) throws PortalException {
    	  System.out.println("checkInExtRepositoryFileEntry");
    }

    @Override
    public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(String extRepositoryFileEntryKey) throws PortalException {
  	  System.out.println("checkOutExtRepositoryFileEntry");
        return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T copyExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFileEntryKey, String newExtRepositoryFolderKey, String newTitle) throws PortalException {
  	  System.out.println("copyExtRepositoryObject");
    	return null;
    }

    @Override
    public void deleteExtRepositoryObject(ExtRepositoryObjectType<? extends ExtRepositoryObject> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {
    	  System.out.println("deleteExtRepositoryObject");
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
    	  System.out.println("getContentStream");
        return null;
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileVersion extRepositoryFileVersion) throws PortalException {
    	  System.out.println("getContentStream");
        return null;
    }

    @Override
    public ExtRepositoryFileVersion getExtRepositoryFileVersion(ExtRepositoryFileEntry extRepositoryFileEntry, String version) throws PortalException {
  	  System.out.println("getExtRepositoryFileVersion");
    	return null;
    }

    @Override
    public ExtRepositoryFileVersionDescriptor getExtRepositoryFileVersionDescriptor(String extRepositoryFileVersionKey) {
    	  System.out.println("getExtRepositoryFileVersionDescriptor");
        return null;
    }

    @Override
    public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
    	  System.out.println("getExtRepositoryFileVersions");
    	  List<ExtRepositoryFileVersion> extRepositoryFileVersions = new ArrayList<>();

		extRepositoryFileVersions.add(
				new DropboxFileVersion(
					extRepositoryFileEntry.getExtRepositoryModelKey(), 2));
		Collections.reverse(extRepositoryFileVersions);

		return extRepositoryFileVersions;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryObjectKey) throws PortalException {
        System.out.println("getExtRepositoryObject");
    	return null;
    }

    @Override
    public <T extends ExtRepositoryObject> T getExtRepositoryObject(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey, String title) throws PortalException {
        System.out.println("getExtRepositoryObject");
    	return null;
    }

    @Override
    public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(ExtRepositoryObjectType<T> extRepositoryObjectType, String extRepositoryFolderKey) throws PortalException {
        System.out.println("getExtRepositoryObjects");
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        List<File> stringList = new ArrayList<>();
        for (Metadata child : result.getEntries()) {
           // ((UnicodeProperties) stringList).put(child.getPathDisplay(),child.getPathDisplay());
            stringList.add(new File(child.getPathDisplay(), extRepositoryFolderKey, false, this.getCompanyId(), extRepositoryFolderKey, null, null, extRepositoryFolderKey));
        }
        
        List<File> driveFilesList = stringList;

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



		List<T> extRepositoryObjects = new ArrayList<>();

		//GoogleDriveCache googleDriveCache = GoogleDriveCache.getInstance();

		for (File file : driveFilesList) {
			if (file.isFolder()) {
				extRepositoryObjects.add(
					(T)new DropboxFolder(file, getRootFolderKey()));
			}
			else {
				extRepositoryObjects.add((T)new DropboxFileEntry(file));
			}

			//googleDriveCache.put(file);
		}
		System.out.println(extRepositoryObjects.size());
		return extRepositoryObjects;
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
      	System.out.println("getExtRepositoryParentFolder");
        return null;
    }

    @Override
    public String getRootFolderKey() throws PortalException {
    	System.out.println("getRootFolderKey");
        return "/";
    }

    @Override
    public List<String> getSubfolderKeys(String extRepositoryFolderKey, boolean recurse) throws PortalException {
     	System.out.println("getSubfolderKeys");
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

    private static final String _FOLDER_MIME_TYPE =
    		"application/vnd.google-apps.folder";

    public DbxClientV2 getClient() {
        return client;
    }

}
