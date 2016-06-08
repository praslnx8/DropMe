package com.prasilabs.dropme.services.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by prasi on 8/6/16.
 */
public class DropMeInstanceIdListenerService extends InstanceIDListenerService
{
    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();

        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        DropMeGcmListenerService.startIntentService(this);
    }
}
