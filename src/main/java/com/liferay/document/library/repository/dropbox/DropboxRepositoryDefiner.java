package com.liferay.document.library.repository.dropbox;


import com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryConstants;
import com.liferay.portal.kernel.repository.DocumentRepository;
import com.liferay.portal.kernel.repository.RepositoryConfiguration;
import com.liferay.portal.kernel.repository.RepositoryConfigurationBuilder;
import com.liferay.portal.kernel.repository.RepositoryFactory;
import com.liferay.portal.kernel.repository.capabilities.PortalCapabilityLocator;
import com.liferay.portal.kernel.repository.capabilities.ProcessorCapability;
import com.liferay.portal.kernel.repository.registry.BaseRepositoryDefiner;
import com.liferay.portal.kernel.repository.registry.CapabilityRegistry;
import com.liferay.portal.kernel.repository.registry.RepositoryDefiner;
import com.liferay.portal.kernel.repository.registry.RepositoryFactoryRegistry;
import com.liferay.portal.kernel.util.CacheResourceBundleLoader;
import com.liferay.portal.kernel.util.ClassResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author andreshernandez
 */
@Component(
	immediate = true,
	property = {
		// TODO enter required service properties
	},
	service = RepositoryDefiner.class
)
public class DropboxRepositoryDefiner extends BaseRepositoryDefiner {

	public DropboxRepositoryDefiner() {
		RepositoryConfigurationBuilder repositoryConfigurationBuilder =
				new RepositoryConfigurationBuilder(
						getResourceBundleLoader(),
						DropboxRepositoryConstants.DROPBOX_REPOSITORY_TOKEN_PARAMETER
				);

		_repositoryConfiguration = repositoryConfigurationBuilder.build();
	}

	@Override
	public String getClassName() {
	    return DropboxRepository.class.getName();
	}

	@Override
	public RepositoryConfiguration getRepositoryConfiguration() {
	    return _repositoryConfiguration;
	}

	@Override
	public String getRepositoryTypeLabel(Locale locale) {
		return "Dropbox Repository";
	}

	@Override
	public boolean isExternalRepository() {
		return true;
	}

	@Override
	public void registerCapabilities(CapabilityRegistry<DocumentRepository> capabilityRegistry) {

            DocumentRepository documentRepository = capabilityRegistry.getTarget();

            PortalCapabilityLocator portalCapabilityLocator =
                    getPortalCapabilityLocator();

            capabilityRegistry.addSupportedCapability(
                    ProcessorCapability.class,
                    new RefreshingProcessorCapability(
                            portalCapabilityLocator.getProcessorCapability(
                                    documentRepository,
                                    ProcessorCapability.ResourceGenerationStrategy.REUSE)));
	}

	@Override
	public void registerRepositoryFactory(RepositoryFactoryRegistry repositoryFactoryRegistry) {
		repositoryFactoryRegistry.setRepositoryFactory(_repositoryFactory);
	}

	private final RepositoryConfiguration _repositoryConfiguration;

	private RepositoryFactory _repositoryFactory;

	@Reference(
			target = "(repository.target.class.name=" + DropboxRepositoryConstants.DROPBOX_REPOSITORY_CLASSNAME + ")",
			unbind = "-"
	)
	protected void setRepositoryFactory(RepositoryFactory repositoryFactory) {

		_repositoryFactory = repositoryFactory;
	}

    @Reference
    private PortalCapabilityLocator _portalCapabilityLocator;

    protected PortalCapabilityLocator getPortalCapabilityLocator() {
        return _portalCapabilityLocator;
    }

	protected ResourceBundleLoader getResourceBundleLoader() {
		return _resourceBundleLoader;
	}

	private final ResourceBundleLoader _resourceBundleLoader =
			new CacheResourceBundleLoader(
					new ClassResourceBundleLoader(
							"content.Language", DropboxRepositoryDefiner.class));

}