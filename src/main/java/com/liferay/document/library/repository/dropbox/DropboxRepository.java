package com.liferay.document.library.repository.dropbox;


import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.BLANK;
import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.COLON;
import static com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants.FORWARD_SLASH;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.ListRevisionsResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.MoveV2Builder;
import com.dropbox.core.v2.files.RelocationErrorException;
import com.dropbox.core.v2.files.RelocationResult;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;
import com.liferay.document.library.repository.dropbox.model.DropboxFileEntry;
import com.liferay.document.library.repository.dropbox.model.DropboxFileVersion;
import com.liferay.document.library.repository.dropbox.model.DropboxFolder;
import com.liferay.document.library.repository.dropbox.model.DropboxObject;
import com.liferay.document.library.repository.external.CredentialsProvider;
import com.liferay.document.library.repository.external.ExtRepository;
import com.liferay.document.library.repository.external.ExtRepositoryAdapter;
import com.liferay.document.library.repository.external.ExtRepositoryFileEntry;
import com.liferay.document.library.repository.external.ExtRepositoryFileVersion;
import com.liferay.document.library.repository.external.ExtRepositoryFileVersionDescriptor;
import com.liferay.document.library.repository.external.ExtRepositoryFolder;
import com.liferay.document.library.repository.external.ExtRepositoryObject;
import com.liferay.document.library.repository.external.ExtRepositoryObjectType;
import com.liferay.document.library.repository.external.ExtRepositorySearchResult;
import com.liferay.document.library.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DropboxRepository extends ExtRepositoryAdapter implements ExtRepository {

    private DbxClientV2 clientv2;

    protected DropboxRepository() {
        super(null);
    }

    @Override
    public ExtRepositoryFileEntry addExtRepositoryFileEntry(String extRepositoryParentFolderKey, String mimeType, String title, String description, String changeLog, InputStream inputStream) throws PortalException {
        _log.info("addExtRepositoryFileEntry");
        DropboxFileEntry fileEntry = null;
        try {
        	String path = extRepositoryParentFolderKey.equals(FORWARD_SLASH) ? extRepositoryParentFolderKey + title : extRepositoryParentFolderKey + FORWARD_SLASH + title;
			FileMetadata metadata = clientv2.files().uploadBuilder(path)
			        .withMode(WriteMode.ADD)
			        .uploadAndFinish(inputStream);
			fileEntry = new DropboxFileEntry(metadata);
		} catch (UploadErrorException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return fileEntry;
    }

    @Override
    public ExtRepositoryFolder addExtRepositoryFolder(String extRepositoryParentFolderKey, String name, String description) throws PortalException {
        _log.info("addExtRepositoryFolder");
        DropboxFolder folder = null;        
        try {
        	String path = extRepositoryParentFolderKey.equals(FORWARD_SLASH) ? extRepositoryParentFolderKey + name : extRepositoryParentFolderKey + FORWARD_SLASH + name;
        	CreateFolderResult result = clientv2.files().createFolderV2(path, true);
        	folder = new DropboxFolder(result.getMetadata(), extRepositoryParentFolderKey);
		} catch (CreateFolderErrorException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		}
    	return folder;
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
    	try {
			clientv2.files().deleteV2(extRepositoryObjectKey);
		} catch (DeleteErrorException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		}
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileEntry extRepositoryFileEntry) throws PortalException {
        _log.info("getContentStream");
        return null;
    }

    @Override
    public InputStream getContentStream(ExtRepositoryFileVersion extRepositoryFileVersion) throws PortalException {
        _log.info("getContentStream");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
        	String[] parts = extRepositoryFileVersion.getExtRepositoryModelKey().split(":");
        	DbxDownloader<FileMetadata> downloader = clientv2.files().download(parts[0]);        	
        	downloader.download(byteArrayOutputStream);
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        }
        
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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
            ListRevisionsResult result =  clientv2.files().listRevisions(extRepositoryFileEntry.getExtRepositoryModelKey());
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

        return generateExtRepositoryInstance(extRepositoryObjectType, metadata);
    }

	private <T extends ExtRepositoryObject> T generateExtRepositoryInstance(
			ExtRepositoryObjectType<T> extRepositoryObjectType, Metadata metadata) {
		if (metadata instanceof FolderMetadata && extRepositoryObjectType.equals(ExtRepositoryObjectType.FOLDER)){
        	List<String> partsList = new ArrayList<>();
        	if(metadata.getPathLower() != null) {
        		String[] parts = metadata.getPathLower().split(FORWARD_SLASH);
            	partsList = new ArrayList<>(Arrays.asList(parts));
            	partsList.remove(parts[parts.length-1]);                
        	}
        	String pathWithoutName = partsList.size() > 1 ? String.join("/", partsList) : FORWARD_SLASH;
        	return (T)new DropboxFolder(
                    metadata, pathWithoutName);
        }else if(metadata instanceof FileMetadata && extRepositoryObjectType.equals(ExtRepositoryObjectType.FILE)){
            return (T)new DropboxFileEntry((FileMetadata) metadata);
        } else {
        	return (T)new DropboxObject(metadata);
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
                    .withRecursive(false)
                    .start();


            List<Metadata> fileList = new ArrayList<>();
            for (Metadata metadata : result.getEntries()) {
                fileList.add(metadata);
            }

            //TODO: finish cache stuff
            //DropboxCache dropboxCache = DropboxCache.getInstance();

            for (Metadata metadata : fileList) {
            	if(metadata instanceof FolderMetadata && extRepositoryObjectType.equals(ExtRepositoryObjectType.FOLDER)) {//only folders will be included
            		generateDropboxFolder(extRepositoryObjects, metadata);
            	} 
            	
            	if(metadata instanceof FileMetadata && extRepositoryObjectType.equals(ExtRepositoryObjectType.FILE)) {//only files will be included
            		extRepositoryObjects.add((T)new DropboxFileEntry(((FileMetadata) metadata)));
            	} 
            	if(extRepositoryObjectType.equals(ExtRepositoryObjectType.OBJECT)) {//folders and files will be included
            		if (metadata instanceof FolderMetadata) {
                    	generateDropboxFolder(extRepositoryObjects, metadata);
                    } else {
                        extRepositoryObjects.add((T)new DropboxFileEntry(((FileMetadata) metadata)));
                    }
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

	private <T extends ExtRepositoryObject> void generateDropboxFolder(List<T> extRepositoryObjects,
			Metadata metadata) {
		String[] parts = metadata.getPathLower().split(FORWARD_SLASH);
		List<String> partsList = new ArrayList<>(Arrays.asList(parts));
		partsList.remove(parts[parts.length-1]);
		String pathWithoutName = partsList.size() > 1 ? String.join("/", partsList) : FORWARD_SLASH;
		extRepositoryObjects.add(
		    (T)new DropboxFolder(metadata, pathWithoutName));
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

        Metadata metadata = Metadata.newBuilder(FORWARD_SLASH).build();
        DropboxFolder folder = new DropboxFolder(metadata, FORWARD_SLASH);
        
        if(extRepositoryObject.getExtRepositoryModelKey() != null) {
        	String[] parts = extRepositoryObject.getExtRepositoryModelKey().split(FORWARD_SLASH);
        	List<String> partsList = new ArrayList<>(Arrays.asList(parts));
        	partsList.remove(parts[parts.length-1]);
            if(parts.length > 2) {
            	try {
                    metadata = clientv2.files().getMetadata(String.join("/", partsList));
                    folder = new DropboxFolder(metadata, metadata.getPathLower());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
            } 
        }

        return folder;
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
    	RelocationResult relocationResult = null;
    	try {
        	String newPath = newExtRepositoryFolderKey.equals(FORWARD_SLASH) ? newExtRepositoryFolderKey + newTitle : newExtRepositoryFolderKey + FORWARD_SLASH + newTitle;
			relocationResult = clientv2.files().moveV2Builder(extRepositoryObjectKey, newPath)
					.withAllowSharedFolder(true)
					.withAutorename(true)
					.start();
		} catch (RelocationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return generateExtRepositoryInstance(extRepositoryObjectType, relocationResult.getMetadata());
    }

    @Override
    public List<ExtRepositorySearchResult<?>> search(SearchContext searchContext, Query query, ExtRepositoryQueryMapper extRepositoryQueryMapper) throws PortalException {
        return null;
    }

    @Override
    public ExtRepositoryFileEntry updateExtRepositoryFileEntry(String extRepositoryFileEntryKey, String mimeType, InputStream inputStream) throws PortalException {
        DropboxFileEntry fileEntry = null;
        try {        	
			FileMetadata metadata = clientv2.files().uploadBuilder(extRepositoryFileEntryKey)
			        .withMode(WriteMode.OVERWRITE)
			        .uploadAndFinish(inputStream);
			fileEntry = new DropboxFileEntry(metadata);
		} catch (UploadErrorException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return fileEntry;
    }

    public DbxClientV2 getClientV2() {
        return clientv2;
    }

    private static final Log _log = LogFactoryUtil.getLog(DropboxRepository.class);

}
