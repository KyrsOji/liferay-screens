package com.liferay.mobile.screens.ddl.form.interactor.upload;

import com.liferay.mobile.screens.base.interactor.JSONObjectEvent;
import com.liferay.mobile.screens.ddl.model.DocumentField;
import com.liferay.mobile.screens.userportrait.interactor.upload.RemoteWrite;

import org.json.JSONObject;

/**
 * @author Javier Gamarra
 */
public class DDLFormDocumentUploadEvent extends JSONObjectEvent implements RemoteWrite {

	public DDLFormDocumentUploadEvent(int targetScreenletId, DocumentField documentField,
									  Long userId, Long groupId, Long repositoryId,
									  Long folderId, String filePrefix, JSONObject jsonObject) {
		super(targetScreenletId, jsonObject);

		_documentField = documentField;
		_userId = userId;
		_groupId = groupId;
		_repositoryId = repositoryId;
		_folderId = folderId;
		_filePrefix = filePrefix;
	}

	public DDLFormDocumentUploadEvent(int targetScreenletId, DocumentField documentField,
									  Long userId, Long groupId, Long repositoryId,
									  Long folderId, String filePrefix, Exception e) {
		super(targetScreenletId, e);

		_documentField = documentField;
		_userId = userId;
		_groupId = groupId;
		_repositoryId = repositoryId;
		_folderId = folderId;
		_filePrefix = filePrefix;
	}

	public Long getUserId() {
		return _userId;
	}

	public Long getGroupId() {
		return _groupId;
	}

	public Long getRepositoryId() {
		return _repositoryId;
	}

	public Long getFolderId() {
		return _folderId;
	}

	public String getFilePrefix() {
		return _filePrefix;
	}

	public DocumentField getDocumentField() {
		return _documentField;
	}

	@Override
	public boolean isRemote() {
		return _remote;
	}

	public void setRemote(boolean remote) {
		_remote = remote;
	}

	private final DocumentField _documentField;
	private final Long _userId;
	private final Long _groupId;
	private final Long _repositoryId;
	private final Long _folderId;
	private final String _filePrefix;
	private boolean _remote;
}
