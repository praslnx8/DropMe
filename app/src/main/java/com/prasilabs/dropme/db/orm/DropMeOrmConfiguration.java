package com.prasilabs.dropme.db.orm;

import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;

import java.util.ArrayList;
import java.util.List;

import za.co.cporm.model.CPOrmConfiguration;

/**
 * Created by prasi on 8/6/16.
 */
public class DropMeOrmConfiguration implements CPOrmConfiguration
{
    private static final String DROPME_DB = "dropme_db";

    @Override
    public String getDatabaseName()
    {
        return DROPME_DB;
    }

    @Override
    public int getDatabaseVersion()
    {
        return 1;
    }

    @Override
    public boolean isQueryLoggingEnabled()
    {
        return CoreApp.appDebug;
    }

    @Override
    public String upgradeResourceDirectory()
    {
        return null;
    }

    @Override
    public List<Class<?>> getDataModelObjects()
    {
        List<Class<?>> domainObjects = new ArrayList<Class<?>>();

        domainObjects.add(DropMeNotifs.class);

        return domainObjects;
    }
}
