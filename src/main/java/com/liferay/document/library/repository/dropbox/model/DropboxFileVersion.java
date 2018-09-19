package com.liferay.document.library.repository.dropbox.model;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.liferay.document.library.repository.external.ExtRepositoryFileVersion;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;

public class DropboxFileVersion extends DropboxModel implements ExtRepositoryFileVersion {

	public DropboxFileVersion(Metadata metadata, String extRepositoryFileEntryKey, int version) {

		super(new Date(), extRepositoryFileEntryKey, ((FileMetadata)metadata).getSize(), "");

		_revision = ((FileMetadata)metadata).getRev();
		_extRepositoryFileEntryKey = extRepositoryFileEntryKey;
		_version = version + ".0";
		_downloadUrl = metadata.getPathLower();
	}

	@Override
	public String getChangeLog() {
		return StringPool.BLANK;
	}

	public String getDownloadURL() {
		return _downloadUrl;
	}

	@Override
	public String getExtRepositoryModelKey() {
		StringBundler sb = new StringBundler(5);

		sb.append(_extRepositoryFileEntryKey);
		sb.append(StringPool.COLON);
		// sb.append(DigesterUtil.digestHex(Digester.MD5, _revision.getId()));
		sb.append(StringPool.COLON);
		sb.append(_version);

		return sb.toString();
	}

	@Override
	public String getVersion() {
		return _version;
	}

	private String _extRepositoryFileEntryKey;
	private String _revision;
	private String _version;
	private String _downloadUrl;

	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return "";
	}

}