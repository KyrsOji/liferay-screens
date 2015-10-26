package com.liferay.mobile.screens.cache.sql;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.liferay.mobile.screens.cache.Cache;
import com.liferay.mobile.screens.cache.CachedContent;
import com.liferay.mobile.screens.cache.CachedType;
import com.liferay.mobile.screens.cache.executor.Executor;
import com.liferay.mobile.screens.context.LiferayScreensContext;
import com.liferay.mobile.screens.util.EventBusUtil;
import com.liferay.mobile.screens.util.LiferayLogger;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CacheSQL<E extends CachedContent> implements Cache<E> {

	public CacheSQL(CacheStrategyFactory cacheStrategyFactory) {
		_cacheStrategyFactory = cacheStrategyFactory;
	}

	/**
	 * Sync and blocking get
	 */
	@Override
	public List get(CachedType cachedType, String query, Object... args) {
		return _cacheStrategyFactory.recoverStrategy(cachedType).get(query, args);
	}

	/**
	 * Sync and blocking get by id
	 */
	@Override
	public E getById(CachedType cachedType, String id) {
		return (E) _cacheStrategyFactory.recoverStrategy(cachedType).getById(id);
	}

	/**
	 * Sync and blocking get by id with the other default params, userId, groupId and locale
	 */
	@Override
	public E getById(CachedType cachedType, String id, Long groupId, Long userId, Locale locale) {
		return (E) _cacheStrategyFactory.recoverStrategy(cachedType).getById(id, groupId, userId, locale);
	}

	/**
	 * Async and non-blocking save or update and posts the result in the bus
	 */
	@Override
	public void set(final E object) {
		Executor.execute(new Runnable() {
			@Override
			public void run() {
				Object result = save(object);
				EventBusUtil.post(result);
			}
		});
	}

	@Override
	public int clear(CachedType cachedType) {
		return _cacheStrategyFactory.recoverStrategy(cachedType).clear();
	}

	@Override
	public int clear(CachedType cachedType, String id) {
		return _cacheStrategyFactory.recoverStrategy(cachedType).clear(id);
	}

	@Override
	public boolean clear(Context context) {
		try {
			StorIOSQLite.getInstance().close();
			return context.deleteDatabase(ScreensSQLiteOpenHelper.SCREENS_CACHE_DB);
		}
		catch (IOException e) {
			LiferayLogger.e("Could not clear the database", e);
			return false;
		}
	}

	@Override
	public void resync() {
		Context context = LiferayScreensContext.getContext();
		context.sendBroadcast(new Intent("com.liferay.mobile.screens.cache.resync"));
	}

	public synchronized static Cache getInstance(DefaultStorIOSQLite customStorIOSQLite, CacheStrategyFactory cacheStrategyFactory) {
		if (_cache == null) {
			StorIOSQLite.initWithCustomStorIOSQLite(customStorIOSQLite);
			_cache = new CacheSQL(cacheStrategyFactory);
		}
		return _cache;
	}

	public synchronized static Cache getInstance() {
		if (_cache == null) {
			_cache = new CacheSQL(new CacheStrategyFactory());
		}
		return _cache;
	}

	@NonNull
	public static DatabaseResult querySet(Object object) {
		return StorIOSQLite.querySet(object);
	}

	public static List queryGet(Class tableClass, String tableName, String where, Object... queryArgs) {
		return StorIOSQLite.queryGet(tableClass, tableName, where, queryArgs);
	}

	private Object save(E object) {
		return _cacheStrategyFactory.recoverStrategy(object.getCachedType()).set(object);
	}

	private static Cache _cache;
	private static CacheStrategyFactory _cacheStrategyFactory;
}