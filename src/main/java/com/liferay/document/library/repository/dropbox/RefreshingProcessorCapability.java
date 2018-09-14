package com.liferay.document.library.repository.dropbox;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.capabilities.ProcessorCapability;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;

public class RefreshingProcessorCapability implements ProcessorCapability {

    public RefreshingProcessorCapability(
            ProcessorCapability processorCapability) {

        _processorCapability = processorCapability;
    }

    @Override
    public void cleanUp(FileEntry fileEntry) throws PortalException {
        _refresh(fileEntry);

        _processorCapability.cleanUp(fileEntry);
    }

    @Override
    public void cleanUp(FileVersion fileVersion) throws PortalException {
        _refresh(fileVersion);

        _processorCapability.cleanUp(fileVersion);
    }

    @Override
    public void copy(FileEntry fileEntry, FileVersion fileVersion)
            throws PortalException {

        _refresh(fileEntry);
        _refresh(fileVersion);

        _processorCapability.copy(fileEntry, fileVersion);
    }

    @Override
    public void generateNew(FileEntry fileEntry) throws PortalException {
        _refresh(fileEntry);

        _processorCapability.generateNew(fileEntry);
    }

    private void _refresh(FileEntry fileEntry) {
//			Document document = (Document)fileEntry.getModel();
//
//			document.refresh();
    }

    private void _refresh(FileVersion fileVersion) throws PortalException {
//			Document document = (Document)fileVersion.getModel();
//
//			document.refresh();

        _refresh(fileVersion.getFileEntry());
    }

    private final ProcessorCapability _processorCapability;
}
