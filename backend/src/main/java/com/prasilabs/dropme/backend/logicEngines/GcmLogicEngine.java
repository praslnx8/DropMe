package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.GcmRecord;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.GcmRecordIO;
import com.prasilabs.util.DataUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by prasi on 8/6/16.
 */
public class GcmLogicEngine extends CoreLogicEngine
{
    private static final String TAG = GcmLogicEngine.class.getSimpleName();

    private static GcmLogicEngine instance;

    public static GcmLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new GcmLogicEngine();
        }

        return instance;
    }

    private GcmLogicEngine(){}

    public ApiResponse addGcmRecord(GcmRecordIO gcmRecordIo)
    {
        ApiResponse apiResponse = new ApiResponse();

        GcmRecord gcmRecord = convertGcmRecortIO(gcmRecordIo);

        if(gcmRecord != null)
        {
            GcmRecord existing = OfyService.ofy().load().type(GcmRecord.class).filter(GcmRecord.DEVICE_ID_STR, gcmRecord.getDeviceId()).first().now();
            if(existing != null)
            {
                existing.setGcmId(gcmRecord.getGcmId());
                existing.setUserId(gcmRecord.getUserId());
                existing.setModified(new Date(System.currentTimeMillis()));

                Key<GcmRecord> gcmRecordKey = OfyService.ofy().save().entity(existing).now();
                apiResponse.setStatus(true);
                apiResponse.setId(gcmRecordKey.getId());
            }
            else
            {
                gcmRecord.setCreated(new Date(System.currentTimeMillis()));
                gcmRecord.setModified(new Date(System.currentTimeMillis()));
                Key<GcmRecord> gcmRecordKey = OfyService.ofy().save().entity(gcmRecord).now();
                apiResponse.setStatus(true);
                apiResponse.setId(gcmRecordKey.getId());
            }
        }
        else
        {
            ConsoleLog.w(TAG, "not a valid input");
        }

        return apiResponse;
    }

    public List<String> getGcmIdOfUsers(List<Long> userIds)
    {
        List<String> gcmIdList = new ArrayList<>();

        Query.Filter filter = new Query.FilterPredicate(GcmRecord.USER_ID_STR, Query.FilterOperator.IN, userIds);
        List<GcmRecord> gcmRecordList = OfyService.ofy().load().type(GcmRecord.class).filter(filter).filter(GcmRecord.IS_DELETED_STR, false).list();

        for(GcmRecord gcmRecord : gcmRecordList)
        {
            gcmIdList.add(gcmRecord.getGcmId());
        }

        return gcmIdList;
    }

    public void updateGcmRecord(String oldGcmID, String newGcmID)
    {
        GcmRecord gcmRecord = getGcmRecordByFcmId(oldGcmID);

        if(gcmRecord != null)
        {
            gcmRecord.setGcmId(newGcmID);
            gcmRecord.setDeleted(false);

            OfyService.ofy().save().entity(gcmRecord).now();
        }
        else
        {
            ConsoleLog.w(TAG, "no entry for the old gcm id");
        }
    }

    public void deleteGcmRecord(String gcmId)
    {
        GcmRecord gcmRecord = getGcmRecordByFcmId(gcmId);

        if(gcmRecord != null)
        {
            gcmRecord.setDeleted(true);
            OfyService.ofy().save().entity(gcmRecord).now();
        }
        else
        {
            ConsoleLog.w(TAG, "gcm entry is empty for id : " + gcmId);
        }
    }

    public GcmRecord getGcmRecordByFcmId(String gcmID)
    {
        GcmRecord gcmRecord = OfyService.ofy().load().type(GcmRecord.class).filter(GcmRecord.GCM_ID_STR, gcmID).first().now();

        return gcmRecord;
    }

    private static GcmRecord convertGcmRecortIO(GcmRecordIO gcmRecordIO)
    {
        GcmRecord gcmRecord = null;

        if(gcmRecordIO != null && !DataUtil.isEmpty(gcmRecordIO.getGcmId()) && !DataUtil.isEmpty(gcmRecordIO.getDeviceId()) && !DataUtil.isEmpty(gcmRecordIO.getUserId()))
        {
            gcmRecord = new GcmRecord();
            gcmRecord.setUserId(gcmRecordIO.getUserId());
            gcmRecord.setDeviceId(gcmRecordIO.getDeviceId());
            gcmRecord.setGcmId(gcmRecordIO.getGcmId());
        }

        return gcmRecord;
    }


}
