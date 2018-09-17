package com.liferay.document.library.repository.dropbox;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.document.library.kernel.service.DLAppHelperLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.repository.dropbox.configuration.DropboxRepositoryConfiguration;
import com.liferay.document.library.repository.external.ExtRepositoryAdapter;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.BaseRepository;
import com.liferay.portal.kernel.repository.LocalRepository;
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.RepositoryFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RepositoryEntryLocalService;
import com.liferay.portal.kernel.service.RepositoryLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

public abstract class BaseDropboxRepositoryFactory <T extends ExtRepositoryAdapter> implements RepositoryFactory{

    @Override
    public LocalRepository createLocalRepository(long repositoryId) throws PortalException {
        System.out.println("createLocalRepository");
        try (ContextClassLoaderSetter contextClassLoaderSetter =
                     new ContextClassLoaderSetter(
                             BaseDropboxRepositoryFactory.class.getClassLoader())) {

            BaseRepository baseRepository = createBaseRepository(repositoryId);

            return baseRepository.getLocalRepository();
        }
    }

    @Override
    public Repository createRepository(long repositoryId) throws PortalException {
        System.out.println("createRepository");
        try (ContextClassLoaderSetter contextClassLoaderSetter =
                     new ContextClassLoaderSetter(
                             BaseDropboxRepositoryFactory.class.getClassLoader())) {

            return new RepositoryProxyBean(
                    createBaseRepository(repositoryId),
                    BaseDropboxRepositoryFactory.class.getClassLoader());
        }
    }



    protected abstract T createBaseRepository();

    protected BaseRepository createBaseRepository(long repositoryId)
            throws PortalException {

        T baseRepository = createBaseRepository();

        com.liferay.portal.kernel.model.Repository repository =
                _repositoryLocalService.getRepository(repositoryId);

        System.out.println(repository.getTypeSettings());

        setupRepository(repositoryId, repository, baseRepository);


        //revisar init
        if (!ExportImportThreadLocal.isImportInProcess()) {
            System.out.println("calling initRepository");
            baseRepository.initRepository();
        }

        return baseRepository;
    }

    protected void setupRepository(long repositoryId, com.liferay.portal.kernel.model.Repository repository,
            BaseRepository baseRepository) {

        baseRepository.setAssetEntryLocalService(_assetEntryLocalService);
        baseRepository.setCompanyId(repository.getCompanyId());
        baseRepository.setCompanyLocalService(_companyLocalService);
        baseRepository.setDLAppHelperLocalService(_dlAppHelperLocalService);
        baseRepository.setDLFolderLocalService(_dlFolderLocalService);
        baseRepository.setGroupId(repository.getGroupId());
        baseRepository.setRepositoryEntryLocalService(
                _repositoryEntryLocalService);
        baseRepository.setRepositoryId(repositoryId);
        baseRepository.setTypeSettingsProperties(
                repository.getTypeSettingsProperties());
        baseRepository.setUserLocalService(_userLocalService);
    }

    private AssetEntryLocalService _assetEntryLocalService;
    private DropboxRepositoryConfiguration _dropboxRepositoryConfiguration;
    private CompanyLocalService _companyLocalService;
    private DLAppHelperLocalService _dlAppHelperLocalService;
    private DLFolderLocalService _dlFolderLocalService;
    private RepositoryEntryLocalService _repositoryEntryLocalService;
    private RepositoryLocalService _repositoryLocalService;
    private UserLocalService _userLocalService;

    protected void setAssetEntryLocalService(
            AssetEntryLocalService assetEntryLocalService) {

        _assetEntryLocalService = assetEntryLocalService;
    }

    protected void setDropboxRepositoryConfiguration(
            DropboxRepositoryConfiguration repositoryConfiguration) {

        _dropboxRepositoryConfiguration = repositoryConfiguration;
    }

    protected void setCompanyLocalService(
            CompanyLocalService companyLocalService) {

        _companyLocalService = companyLocalService;
    }

    protected void setDLAppHelperLocalService(
            DLAppHelperLocalService dlAppHelperLocalService) {

        _dlAppHelperLocalService = dlAppHelperLocalService;
    }

    protected void setDLFolderLocalService(
            DLFolderLocalService dlFolderLocalService) {

        _dlFolderLocalService = dlFolderLocalService;
    }

    protected void setRepositoryEntryLocalService(
            RepositoryEntryLocalService repositoryEntryLocalService) {

        _repositoryEntryLocalService = repositoryEntryLocalService;
    }

    protected void setRepositoryLocalService(
            RepositoryLocalService repositoryLocalService) {

        _repositoryLocalService = repositoryLocalService;
    }

    protected void setUserLocalService(UserLocalService userLocalService) {
        _userLocalService = userLocalService;
    }

}
