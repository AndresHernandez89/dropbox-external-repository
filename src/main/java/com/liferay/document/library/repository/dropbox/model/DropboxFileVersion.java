package com.liferay.document.library.repository.dropbox.model;

import com.liferay.document.library.repository.external.ExtRepositoryFileVersion;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;

public class DropboxFileVersion extends DropboxModel implements ExtRepositoryFileVersion {

	public DropboxFileVersion(String extRepositoryFileEntryKey, int version) {

		super(new Date(), extRepositoryFileEntryKey, GetterUtil.getLong(1), GetterUtil.getString(""));

		_extRepositoryFileEntryKey = extRepositoryFileEntryKey;
		_version = version + ".0";
	}

	@Override
	public String getChangeLog() {
		return StringPool.BLANK;
	}

	// public String getDownloadURL() {
	// return GetterUtil.getString(_revision.getDownloadUrl());
	// }

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

	// @Override
	// public String getMimeType() {
	// return GetterUtil.getString(_revision.getMimeType());
	// }

	@Override
	public String getVersion() {
		return _version;
	}

	private String _extRepositoryFileEntryKey;
	// private Revision _revision;
	private String _version;

	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return null;
	}

}