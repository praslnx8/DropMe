package com.prasilabs.dropme.backend.core;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.prasilabs.dropme.backend.datastore.CacheKeyValue;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by prasi on 11/3/16.
 * Core logic engine. every one should extend this guy
 */
public abstract class CoreLogicEngine
{
    private static final String TAG = CoreLogicEngine.class.getSimpleName();

    //should not be 0. 0  is default. default key not in the list
    protected final static int RETAIL_SPACE_PARENT_KEY = 1;
    protected final static int FEATURED_RETAIL_SPACE_PARENT_KEY = 2;
    protected final static int REPORT_PARENT_KEY = 3;
    protected final static int RATING_PARENT_KEY = 4;

    private static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    protected static <T> List<T> getListDataFromCache(String cacheKey)
    {
        ConsoleLog.i(TAG, "Getting data from cache : key : " + cacheKey);

        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        byte[] cacheValue = (byte[]) syncCache.get(cacheKey);

        if(cacheValue != null)
        {
            return getListFromByteArray(cacheValue);
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> T getDataFromCache(String cacheKey)
    {
        ConsoleLog.s(TAG, "Getting data from cache : key : " + cacheKey);

        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        byte[] cacheValue = (byte[]) syncCache.get(cacheKey);

        if(cacheValue != null)
        {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(cacheValue));
                Object data = objectInputStream.readObject();
                return (T) data;
            }
            catch (Exception e)
            {
                if(clearCache(cacheKey))
                {
                    ConsoleLog.w(TAG, "exception: cleared cache");
                }
                else
                {
                    ConsoleLog.s(TAG, "exception class cast unable to clear cache also");
                }
                ConsoleLog.e(e);
            }
            return null;
        }
        else
        {
            return null;
        }
    }

    protected static boolean clearCache(int parentID)
    {
        boolean isCleared = false;

        try
        {
            List<CacheKeyValue> cacheKeyList = OfyService.ofy().load().type(CacheKeyValue.class).filter("parentKey", parentID).list();

            for (CacheKeyValue cacheKey : cacheKeyList) {
                syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
                if (syncCache.contains(cacheKey.getCacheKey())) {
                    syncCache.delete(cacheKey.getCacheKey());
                }
            }

            OfyService.ofy().delete().entities(cacheKeyList);

            isCleared = true;
        }catch (Exception e)
        {
            ConsoleLog.e(e);
            isCleared = false;
        }

        return isCleared;
    }

    protected synchronized static boolean clearCache(String key)
    {
        boolean isSuccess = false;
        try
        {
            syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
            if (syncCache.contains(key))
            {
                isSuccess = syncCache.delete(key);

                CacheKeyValue cacheKey = OfyService.ofy().load().type(CacheKeyValue.class).filter("cacheKey", key).first().now();
                if (cacheKey != null)
                {
                    OfyService.ofy().delete().type(CacheKeyValue.class).id(cacheKey.getId()).now();
                }
            }
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
            isSuccess = false;
        }

        return isSuccess;
    }

    protected synchronized static <T> void storeListToCache(String cacheKey, List<T> list)
    {
        storeListToCache(cacheKey, list, 0);
    }

    protected synchronized static <T> void storeListToCache(String cacheKey, List<T> list, int parentKey)
    {
        try
        {
            ConsoleLog.s(TAG, "storing data to cache : key : " + cacheKey + " list size is : " + list.size());
            syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
            syncCache.put(cacheKey, getByteArrayFromList(list));

            if(parentKey != 0)
            {
                CacheKeyValue existCacheKeyVal = OfyService.ofy().load().type(CacheKeyValue.class).filter(CacheKeyValue.PARENT_KEY_STR, parentKey).filter(CacheKeyValue.CACHE_KEY_STR, cacheKey).first().now();

                CacheKeyValue cacheKeyObj = new CacheKeyValue();
                if(existCacheKeyVal != null)
                {
                    cacheKeyObj.setId(existCacheKeyVal.getId());
                    cacheKeyObj.setModified(new Date(System.currentTimeMillis()));
                }
                else
                {
                    cacheKeyObj.setCreated(new Date(System.currentTimeMillis()));
                }
                cacheKeyObj.setParentKey(parentKey);
                cacheKeyObj.setCacheKey(cacheKey);
                OfyService.ofy().save().entity(cacheKeyObj).now();

            }
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

    }

    protected synchronized static <T> void storeToCache(String cacheKey, T data)
    {
        storeToCache(cacheKey, data, 0);
    }

    protected synchronized static <T> void storeToCache(String cacheKey, T data, int parentKey)
    {
        try
        {
            ConsoleLog.s(TAG, "storing data to cache : key : " + cacheKey);
            syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
            syncCache.put(cacheKey, getByteArrayFromData(data));

            if(parentKey != 0)
            {
                CacheKeyValue existCacheKeyVal = OfyService.ofy().load().type(CacheKeyValue.class).filter(CacheKeyValue.PARENT_KEY_STR, parentKey).filter(CacheKeyValue.CACHE_KEY_STR, cacheKey).first().now();

                CacheKeyValue cacheKeyObj = new CacheKeyValue();
                if(existCacheKeyVal != null)
                {
                    cacheKeyObj.setId(existCacheKeyVal.getId());
                    cacheKeyObj.setModified(new Date(System.currentTimeMillis()));
                }
                else
                {
                    cacheKeyObj.setCreated(new Date(System.currentTimeMillis()));
                }
                cacheKeyObj.setParentKey(parentKey);
                cacheKeyObj.setCacheKey(cacheKey);
                OfyService.ofy().save().entity(cacheKeyObj).now();

            }
        }catch (Exception e)
        {
            ConsoleLog.e(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getListFromByteArray(byte[] listBytes)
    {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(listBytes));

            Object data = ois.readObject();

            List<T> list = null;
            if(data instanceof List)
            {
                list = (List<T>) data;
            }

            ois.close();
            return  list;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }
        return null;
    }

    private static <T> byte[] getByteArrayFromList(List<T> list)
    {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(list);//mArrayList is the array to convert
            return bos.toByteArray();
        }
        catch (IOException e)
        {
            ConsoleLog.e(e);
        }
        return  null;
    }

    private static <T> byte[] getByteArrayFromData(T data)
    {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            return bos.toByteArray();
        }
        catch (IOException e)
        {
            ConsoleLog.e(e);
        }
        return  null;
    }
}
