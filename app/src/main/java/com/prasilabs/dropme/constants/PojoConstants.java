package com.prasilabs.dropme.constants;

/**
 * Created by prasi on 27/5/16.
 */
public class PojoConstants
{
    public static class UserConstant
    {
        private static final String TAG = "user";
        public static final String ID_STR = TAG + "id";
        public static final String EMAIL_STR = TAG + "email";
        public static final String LOGIN_TYPE_STR = TAG + "login_type";
        public static final String ACCES_TOKEN_STR = TAG + "acces_token";
        public static final String HASH_STR = TAG + "hash";
        public static final String NAME_STR = TAG + "name";
        public static final String MOBILE_STR = TAG + "mobile";
        public static final String MOBILE_VERIFIED_STR = TAG + "mobile_verified";
        public static final String GENDER_STR = TAG + "gender";
        public static final String ROLES_STR = TAG + "roles";
        public static final String PICTURE_STR = TAG + "picture";
        public static final String CREATED_STR = TAG + "created";
    }

    public static class RideConstant
    {
        private static final String TAG = "ride";

        public static final String ID_STR = TAG + "id";
        public static final String USER_ID_STR = TAG + "userId";
        public static final String DEVICE_ID_STR = TAG + "deviceId";
        public static final String VEHICEL_ID_STR = TAG + "vehicleId";
        public static final String SOURCE_LOC_STR = TAG + "sourceLoc";
        public static final String DEST_LOC_NAME_STR = TAG + "destLocName";
        public static final String DEST_LOC_STR = TAG + "destLoc";
        public static final String CURRENT_LOC_STR = TAG + "currentLoc";
        public static final String IS_CLOSED_STR = TAG + "isClosed";
        public static final String FARE_PER_KM_STR = TAG + "farePerKm";
        public static final String EXPIRY_DATE_STR = TAG + "expiryDate";
        public static final String START_DATE_STR = TAG + "startDate";
        public static final String CLOSED_DATE_STR = TAG + "closedDate";
    }

}
