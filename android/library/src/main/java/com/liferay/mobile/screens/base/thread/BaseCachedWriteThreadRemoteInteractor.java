package com.liferay.mobile.screens.base.thread;

import com.liferay.mobile.screens.base.thread.event.BasicThreadEvent;
import com.liferay.mobile.screens.base.thread.event.ErrorThreadEvent;
import com.liferay.mobile.screens.base.thread.event.OfflineEventNew;
import com.liferay.mobile.screens.base.thread.listener.OfflineListenerNew;
import com.liferay.mobile.screens.cache.Cache;
import com.liferay.mobile.screens.cache.OfflinePolicy;
import com.liferay.mobile.screens.cache.executor.Executor;
import com.liferay.mobile.screens.util.EventBusUtil;
import com.liferay.mobile.screens.util.LiferayLogger;

/**
 * @author Javier Gamarra
 */
public abstract class BaseCachedWriteThreadRemoteInteractor<L extends OfflineListenerNew, E extends OfflineEventNew>
	extends BaseCachedThreadRemoteInteractor<L, E> {

	public void start(final E event) {
		Executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (offlinePolicy == OfflinePolicy.CACHE_ONLY) {
						storeToCacheAndLaunchEvent(event);
					} else if (offlinePolicy == OfflinePolicy.CACHE_FIRST) {
						try {
							storeToCacheAndLaunchEvent(event);
						} catch (Exception e) {
							online(event);
						}
					} else if (offlinePolicy == OfflinePolicy.REMOTE_FIRST) {
						try {
							online(event);
						} catch (Exception e) {
							storeToCacheAndLaunchEvent(event);
							LiferayLogger.i("Store online first failed, trying to store locally version");
						}
					} else {
						online(event);
					}
				} catch (Exception e) {
					BasicThreadEvent event = new ErrorThreadEvent(e);
					decorateBaseEvent(event);
					EventBusUtil.post(event);
				}
			}
		});
	}

	public void onEventMainThread(E event) {
		try {
			LiferayLogger.i("event = [" + event + "]");

			if (isInvalidEvent(event)) {
				return;
			}

			if (event.isFailed()) {
				event.setDirty(true);
				store(event);
				onSuccess(event);
			} else {
				if (event.isOnlineRequest()) {
					store(event);
				}
				onSuccess(event);
			}
		} catch (Exception e) {
			onFailure(event);
		}
	}

	protected abstract void onFailure(E event);

	public void onFailure(Exception e) {

	}

	public abstract E execute(E event) throws Exception;

	@Override
	public E execute(Object[] args) throws Exception {
		throw new AssertionError("Shouldn't be called");
	}

	protected void online(E onlineEvent) throws Exception {
		decorateEvent(onlineEvent, false);
		E event = execute(onlineEvent);
		EventBusUtil.post(event);
	}

	protected void storeToCacheAndLaunchEvent(E event) throws Exception {
		decorateEvent(event, true);
		event.setDirty(true);
		store(event);
		EventBusUtil.post(event);
	}

	protected void store(E event) throws Exception {
		Cache.storeObject(event);
	}

	@Override
	protected String getIdFromArgs(Object... args) {
		return null;
	}
}