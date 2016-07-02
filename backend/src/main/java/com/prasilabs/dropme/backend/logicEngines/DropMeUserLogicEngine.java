package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.annotions.PushQ;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.OTPData;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.DropMeUserDetail;
import com.prasilabs.dropme.backend.io.DropMeUserEditIO;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.security.HashGenerator;
import com.prasilabs.dropme.backend.services.encryption.EncryptionManager;
import com.prasilabs.dropme.backend.services.encryption.OTPManager;
import com.prasilabs.dropme.backend.services.pushquees.PushQueueController;
import com.prasilabs.dropme.backend.utils.EmailSendUtil;
import com.prasilabs.enums.LoginType;
import com.prasilabs.util.DataUtil;
import com.prasilabs.util.ValidateUtil;

import java.util.Date;

/**
 * Created by prasi on 26/5/16.
 */
public class DropMeUserLogicEngine extends CoreLogicEngine
{
    private static final String TAG = DropMeUserLogicEngine.class.getSimpleName();
    private static DropMeUserLogicEngine instance;

    private DropMeUserLogicEngine() {
    }

    public static DropMeUserLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new DropMeUserLogicEngine();
        }
        return instance;
    }

    private static boolean validateDropMeUser(DropMeUser dropMeUser)
    {
        boolean isValid = false;

        if (dropMeUser != null) {
            isValid = true;

            if (ValidateUtil.validateEmail(dropMeUser.getEmail()) && !ValidateUtil.isStringEmpty(dropMeUser.getName()) && dropMeUser.getGender() != null) {
                isValid = true;
            }
        }

        return isValid;
    }

    private static DropMeUser convertToDropMeUSer(VDropMeUser vDropMeUser) {
        if (vDropMeUser != null)
        {
            DropMeUser dropMeUser = new DropMeUser();

            if (vDropMeUser.getId() != 0)
            {
                dropMeUser.setId(vDropMeUser.getId());
            }
            dropMeUser.setName(vDropMeUser.getName());
            dropMeUser.setEmail(vDropMeUser.getEmail());
            dropMeUser.setLoginType(vDropMeUser.getLoginType());
            dropMeUser.setGender(vDropMeUser.getGender());
            dropMeUser.setMobile(vDropMeUser.getMobile());
            dropMeUser.setPicture(vDropMeUser.getPicture());
            dropMeUser.setMobileVerified(vDropMeUser.isMobileVerified());
            dropMeUser.setDob(vDropMeUser.getDob());
            dropMeUser.setMessage(vDropMeUser.getMessage());

            return dropMeUser;
        }

        return null;
    }

    private static VDropMeUser convertToVDropMeUser(DropMeUser dropMeUser, boolean isSendHash) {
        VDropMeUser vDropMeUser = new VDropMeUser();

        vDropMeUser.setId(dropMeUser.getId());
        if (isSendHash)
        {
            vDropMeUser.setHash(dropMeUser.getHash());
        }
        vDropMeUser.setName(dropMeUser.getName());
        vDropMeUser.setEmail(dropMeUser.getEmail());
        vDropMeUser.setGender(dropMeUser.getGender());
        vDropMeUser.setLoginType(dropMeUser.getLoginType());
        vDropMeUser.setMobile(dropMeUser.getMobile());
        vDropMeUser.setRoles(dropMeUser.getRoles());
        vDropMeUser.setCreated(dropMeUser.getCreated());
        vDropMeUser.setPicture(dropMeUser.getPicture());
        vDropMeUser.setDob(dropMeUser.getDob());
        vDropMeUser.setMessage(dropMeUser.getMessage());
        vDropMeUser.setMobileVerified(dropMeUser.isMobileVerified());

        return vDropMeUser;
    }

    private static void copyDropMeUser(DropMeUser source, DropMeUser dest, boolean isFromDb) {
        if (isFromDb) {
            if (source.getId() != null) {
                dest.setId(source.getId());
            }
            if (!DataUtil.isEmpty(source.getHash())) {
                dest.setHash(source.getHash());
            }
            if (source.getRoles() != null && source.getRoles().size() > 0) {
                dest.setRoles(source.getRoles());
            }
            if (source.getGender() != null) {
                dest.setGender(source.getGender());
            }
            if (!DataUtil.isEmpty(source.getEmail())) {
                dest.setEmail(source.getEmail());
            }
        }
        if (!DataUtil.isEmpty(source.getName())) {
            dest.setName(source.getName());
        }
        if (!DataUtil.isEmpty(source.getLoginType())) {
            dest.setLoginType(source.getLoginType());
        }
        if (!DataUtil.isEmpty(source.getPicture())) {
            dest.setPicture(source.getPicture());
        }
        if (!DataUtil.isEmpty(source.getMobile())) {
            dest.setMobile(source.getMobile());
        }
        if (source.getDob() != null) {
            dest.setDob(source.getDob());
        }
        if (!DataUtil.isEmpty(source.getLocation())) {
            dest.setLoginType(source.getLocation());
        }
        if (!DataUtil.isEmpty(source.getMessage())) {
            dest.setMessage(source.getMessage());
        }
    }

    private static void copyDropMeUserToDetail(DropMeUser dropMeUser, DropMeUserDetail dropMeUserDetail) {
        dropMeUserDetail.setId(dropMeUser.getId());
        dropMeUserDetail.setCreated(dropMeUser.getCreated());
        dropMeUserDetail.setDob(dropMeUser.getDob());
        dropMeUserDetail.setEmail(dropMeUser.getEmail());
        dropMeUserDetail.setGender(dropMeUser.getGender());
        dropMeUserDetail.setLastLogedIn(dropMeUser.getLastLogedIn());
        dropMeUserDetail.setLocation(dropMeUser.getLocation());
        dropMeUserDetail.setMessage(dropMeUser.getMessage());
        dropMeUserDetail.setMobile(dropMeUser.getMobile());
        dropMeUserDetail.setMobileVerified(dropMeUser.isMobileVerified());
        dropMeUserDetail.setRoles(dropMeUser.getRoles());
        dropMeUserDetail.setName(dropMeUser.getName());
        String picture = dropMeUser.getPicture();
        if (picture != null && dropMeUser.getLoginType().equals(LoginType.GPlus.name())) {
            picture = picture.replace("sz=50", "sz=200");
        }
        dropMeUserDetail.setPicture(picture);
    }

    public VDropMeUser loginSignup(User user, VDropMeUser input)
    {
        DropMeUser existingDropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, user.getEmail()).first().now();

        if (existingDropMeUser == null) {
            input.setEmail(user.getEmail());
            DropMeUser dropMeUser = convertToDropMeUSer(input);

            if (validateDropMeUser(dropMeUser)) {
                dropMeUser.setHash(HashGenerator.md5(String.valueOf(System.currentTimeMillis())));
                dropMeUser.setCreated(new Date(System.currentTimeMillis()));
                dropMeUser.setLastLogedIn(new Date(System.currentTimeMillis()));
                Key<DropMeUser> dropMeUserKey = OfyService.ofy().save().entity(dropMeUser).now();
                dropMeUser.setId(dropMeUserKey.getId());

                PushQueueController.sendNewUserRegistrationEmail(dropMeUserKey.getId());

                return convertToVDropMeUser(dropMeUser, true);
            }

            return null;
        } else {
            DropMeUser dropMeUser = convertToDropMeUSer(input);

            copyDropMeUser(dropMeUser, existingDropMeUser, false);
            existingDropMeUser.setModified(new Date(System.currentTimeMillis()));
            existingDropMeUser.setLastLogedIn(new Date(System.currentTimeMillis()));
            Key<DropMeUser> dropMeUserKey = OfyService.ofy().save().entity(existingDropMeUser).now();
            dropMeUser.setId(dropMeUserKey.getId());

            return convertToVDropMeUser(existingDropMeUser, true);
        }
    }

    public ApiResponse updateDropMeUser(DropMeUserEditIO dropMeUserEditIO) {
        ApiResponse apiResponse = new ApiResponse();


        return apiResponse;
    }

    public VDropMeUser getLoginInfo(User user) {
        DropMeUser dropMeUser = getDropMeUser(user.getEmail());

        return convertToVDropMeUser(dropMeUser, true);
    }

    public DropMeUser getDropMeUserById(long id)
    {
        DropMeUser dropMeUser = getDropMeUser(id);

        return dropMeUser;
    }

    public VDropMeUser getVDropMeUserById(long id)
    {
        DropMeUser dropMeUser = getDropMeUser(id);

        VDropMeUser vDropMeUser = convertToVDropMeUser(dropMeUser, false);

        return vDropMeUser;
    }

    public DropMeUser getDropMeUser(long id)
    {
        DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).id(id).now();

        return dropMeUser;
    }

    public DropMeUser getDropMeUser(String email)
    {
        DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, email).first().now();

        return dropMeUser;
    }

    public DropMeUser getDropMeUserByHash(String hash)
    {
        DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.HASH_STR, hash).first().now();

        return dropMeUser;
    }

    public DropMeUserDetail getUserDetail(long id) {
        DropMeUserDetail dropMeUserDetail = new DropMeUserDetail();

        DropMeUser dropMeUser = getDropMeUser(id);

        copyDropMeUserToDetail(dropMeUser, dropMeUserDetail);

        int rideCount = RideLogicEngine.getInstance().getRideCountOfUser(id);

        dropMeUserDetail.setRideCount(rideCount);

        return dropMeUserDetail;
    }

    public boolean sendOtp(User user, String phone) {
        DropMeUser dropMeUser = getDropMeUser(user.getEmail());
        dropMeUser.setMobile(phone);

        OTPData otpData = OfyService.ofy().load().type(OTPData.class).filter(OTPData.USER_ID_STR, dropMeUser.getId()).first().now();
        if (otpData == null) {
            otpData = new OTPData();
            otpData.setUserId(dropMeUser.getId());
            otpData.setCreated(new Date(System.currentTimeMillis()));
        }

        otpData.setModified(new Date(System.currentTimeMillis()));
        String otpNo = OTPManager.getOtp();
        String encryptedNo = EncryptionManager.encryptData(otpNo);
        otpData.setOtp(encryptedNo);

        OfyService.ofy().save().entity(otpData).now();
        OfyService.ofy().save().entity(dropMeUser).now();

        //return SmsSenderUtil.sendOtpSms(otpNo, dropMeUser.getName(), phone);

        return true;
    }

    public boolean verifyOtp(User user, String otp) {
        boolean verified = false;
        DropMeUser dropMeUser = getDropMeUser(user.getEmail());

        OTPData otpData = OfyService.ofy().load().type(OTPData.class).filter(OTPData.USER_ID_STR, dropMeUser.getId()).first().now();

        if (otpData != null) {
            String otpNoFromServer = otpData.getOtp();

            String decryptedOtp = "7777"; //TODO for temp. EncryptionManager.decryptData(otpNoFromServer);

            verified = decryptedOtp.equalsIgnoreCase(otp);

            if (verified) {
                dropMeUser.setMobileVerified(true);
                OfyService.ofy().save().entity(dropMeUser).now();
            }
        }

        return verified;
    }

    @PushQ
    public boolean sendNewRegistrationEmail(long userId)
    {
        ConsoleLog.i(TAG, "sending email for user id " + userId);
        boolean success = false;

        try
        {
            DropMeUser dropMeUser = getDropMeUserById(userId);

            if (dropMeUser != null)
            {
                //EmailSendUtil.sendRegisterEmailToAdmin(dropMeUser); Disabled as other email is bcc to admin
                EmailSendUtil.sendWelcomeEmailToUser(dropMeUser);
                success = true;
            } else
            {
                ConsoleLog.s(TAG, "dropemeuser not found for the id " + userId);
            }
        } catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return success;
    }
}
