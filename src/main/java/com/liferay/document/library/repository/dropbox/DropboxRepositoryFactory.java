package com.liferay.document.library.repository.dropbox;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.document.library.kernel.service.DLAppHelperLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.repository.dropbox.configuration.DropboxRepositoryConfiguration;
import com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants;
import com.liferay.portal.kernel.repository.RepositoryFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RepositoryEntryLocalService;
import com.liferay.portal.kernel.service.RepositoryLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import java.util.Map;

@Component(
        immediate = true,
        property = "repository.target.class.name=" + DropboxRepositoryConstants.DROPBOX_REPOSITORY_CLASSNAME,
        service = RepositoryFactory.class
)
public class DropboxRepositoryFactory extends BaseDropboxRepositoryFactory<DropboxRepository>  {

    @Activate
    protected void activate(Map<String, Object> properties) {
        DropboxRepositoryConfiguration repositoryConfiguration =
                ConfigurableUtil.createConfigurable(
                        DropboxRepositoryConfiguration.class, properties);

        super.setDropboxRepositoryConfiguration(repositoryConfiguration);
    }

    @Override
    protected DropboxRepository createBaseRepository() {
        return new DropboxRepository();
    }

    @Override
    @Reference(unbind = "-")
    protected void setAssetEntryLocalService(
            AssetEntryLocalService assetEntryLocalService) {

        super.setAssetEntryLocalService(assetEntryLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setCompanyLocalService(
            CompanyLocalService companyLocalService) {

        super.setCompanyLocalService(companyLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setDLAppHelperLocalService(
            DLAppHelperLocalService dlAppHelperLocalService) {

        super.setDLAppHelperLocalService(dlAppHelperLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setDLFolderLocalService(
            DLFolderLocalService dlFolderLocalService) {

        super.setDLFolderLocalService(dlFolderLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setRepositoryEntryLocalService(
            RepositoryEntryLocalService repositoryEntryLocalService) {

        super.setRepositoryEntryLocalService(repositoryEntryLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setRepositoryLocalService(
            RepositoryLocalService repositoryLocalService) {

        super.setRepositoryLocalService(repositoryLocalService);
    }

    @Override
    @Reference(unbind = "-")
    protected void setUserLocalService(UserLocalService userLocalService) {
        super.setUserLocalService(userLocalService);
    }
}
