package com.prasilabs.dropme.managers;

import android.content.Context;
import android.text.TextUtils;

import com.google.api.client.util.DateTime;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.constants.UserConstant;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by prasi on 27/5/16.
 */
public class UserManager
{
    public static boolean saveDropMeUser(Context context, VDropMeUser vDropMeUser)
    {
        boolean isSucces = false;
        try
        {
            LocalPreference.saveLoginDataInShared(context, UserConstant.HASH_STR, vDropMeUser.getHash());
            LocalPreference.saveLoginDataInShared(context, UserConstant.NAME_STR, vDropMeUser.getName());
            LocalPreference.saveLoginDataInShared(context, UserConstant.EMAIL_STR, vDropMeUser.getEmail());
            LocalPreference.saveLoginDataInShared(context, UserConstant.LOGIN_TYPE_STR, vDropMeUser.getLoginType());
            LocalPreference.saveLoginDataInShared(context, UserConstant.CREATED_STR, vDropMeUser.getCreated().getValue());
            LocalPreference.saveLoginDataInShared(context, UserConstant.PICTURE_STR, vDropMeUser.getPicture());
            LocalPreference.saveLoginDataInShared(context, UserConstant.GENDER_STR, vDropMeUser.getGender());
            LocalPreference.saveLoginDataInShared(context, UserConstant.ID_STR, vDropMeUser.getId());
            LocalPreference.saveLoginDataInShared(context, UserConstant.MOBILE_STR, vDropMeUser.getMobile());
            LocalPreference.saveLoginDataInShared(context, UserConstant.MOBILE_VERIFIED_STR, vDropMeUser.getMobileVerified());
            if(vDropMeUser.getRoles() != null)
            {
                LocalPreference.saveLoginDataInShared(context, UserConstant.ROLES_STR, Arrays.toString(vDropMeUser.getRoles().toArray()));
            }
            else
            {
                LocalPreference.saveLoginDataInShared(context, UserConstant.ROLES_STR, null);
            }

            isSucces = true;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return isSucces;
    }

    public static VDropMeUser getDropMeUser(Context context)
    {
        VDropMeUser vDropMeUser = null;

        String name = LocalPreference.getLoginDataFromShared(context, UserConstant.NAME_STR, null);
        String hash = LocalPreference.getLoginDataFromShared(context, UserConstant.HASH_STR, null);
        String loginType = LocalPreference.getLoginDataFromShared(context, UserConstant.LOGIN_TYPE_STR, null);
        String email = LocalPreference.getLoginDataFromShared(context, UserConstant.EMAIL_STR, null);
        long created = LocalPreference.getLoginDataFromShared(context, UserConstant.LOGIN_TYPE_STR, 0L);
        DateTime createdDate = null;
        if(created != 0L)
        {
            createdDate = new DateTime(created);
        }
        String picture = LocalPreference.getLoginDataFromShared(context, UserConstant.PICTURE_STR, null);
        String gender = LocalPreference.getLoginDataFromShared(context, UserConstant.GENDER_STR, null);
        long id = LocalPreference.getLoginDataFromShared(context, UserConstant.ID_STR, 0L);
        Long idL = null;
        if(id != 0L)
        {
            idL = id;
        }
        String mobile = LocalPreference.getLoginDataFromShared(context, UserConstant.MOBILE_STR, null);
        boolean mobileVerified = LocalPreference.getLoginDataFromShared(context, UserConstant.MOBILE_VERIFIED_STR, false);
        List<String> roles = LocalPreference.getSessionStringArrayData(context, UserConstant.ROLES_STR, null);

        if(!TextUtils.isEmpty(hash) && !TextUtils.isEmpty(name) && idL != null && !TextUtils.isEmpty(loginType) && !TextUtils.isEmpty(email))
        {
            vDropMeUser = new VDropMeUser();
            vDropMeUser.setName(name);
            vDropMeUser.setHash(hash);
            vDropMeUser.setEmail(email);
            vDropMeUser.setLoginType(loginType);
            vDropMeUser.setCreated(createdDate);
            vDropMeUser.setPicture(picture);
            vDropMeUser.setGender(gender);
            vDropMeUser.setId(idL);
            vDropMeUser.setMobile(mobile);
            vDropMeUser.setMobileVerified(mobileVerified);
            vDropMeUser.setRoles(roles);
        }

        return vDropMeUser;
    }
}
