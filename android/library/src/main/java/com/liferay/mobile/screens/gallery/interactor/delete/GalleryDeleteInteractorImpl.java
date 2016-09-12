package com.liferay.mobile.screens.gallery.interactor.delete;

import com.liferay.mobile.android.v7.dlapp.DLAppService;
import com.liferay.mobile.screens.base.thread.BaseCachedWriteThreadRemoteInteractor;
import com.liferay.mobile.screens.gallery.interactor.GalleryInteractorListener;
import com.liferay.mobile.screens.gallery.interactor.load.GalleryEvent;

/**
 * @author Víctor Galán Grande
 */
public class GalleryDeleteInteractorImpl
	extends BaseCachedWriteThreadRemoteInteractor<GalleryInteractorListener, GalleryEvent> {

	@Override
	public GalleryEvent execute(GalleryEvent event) throws Exception {

		long fileEntryId = event.getImageEntry().getFileEntryId();
		validate(fileEntryId);

		DLAppService dlAppService = new DLAppService(getSession());
		dlAppService.deleteFileEntry(fileEntryId);

		return event;
	}

	@Override
	public void onSuccess(GalleryEvent event) throws Exception {
		getListener().onImageEntryDeleted(event.getImageEntry().getFileEntryId());
	}

	@Override
	protected void onFailure(GalleryEvent event) {
		getListener().error(event.getException(), getActionName());
	}

	private void validate(long imageEntryId) {
		if (imageEntryId <= 0) {
			throw new IllegalArgumentException("Image entry Id must be greater than 0");
		}
	}
}