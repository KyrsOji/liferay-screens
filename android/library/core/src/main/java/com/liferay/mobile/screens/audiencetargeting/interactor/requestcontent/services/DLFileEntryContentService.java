package com.liferay.mobile.screens.audiencetargeting.interactor.requestcontent.services;

import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.v62.dlfileentry.DLFileEntryService;
import com.liferay.mobile.screens.audiencetargeting.interactor.requestcontent.AudienceTargetingJSONRequestedCallback;
import com.liferay.mobile.screens.context.SessionContext;

/**
 * @author Javier Gamarra
 */
public class DLFileEntryContentService extends AudienceTargetingContentService {
	@Override
	public void retrieveBySessionAndClassPK(final Integer classPK, String className, Integer screenletId) throws Exception {
		Session session = SessionContext.createSessionFromCurrentSession();
		session.setCallback(new AudienceTargetingJSONRequestedCallback(screenletId, className));
		DLFileEntryService fileEntryService = new DLFileEntryService(session);
		fileEntryService.getFileEntry(classPK);
	}
}