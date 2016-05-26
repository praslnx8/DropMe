package com.prasilabs;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by prasi on 26/5/16.
 */
public class ValidateUtil
{
    private static final String MOBILE_PATTERN = "^(?:0091|\\+91|0|)[7-9][0-9]{9}$";

    public static boolean validateEmail(String email)
    {
        if(email != null)
        {
            return EmailValidator.getInstance().isValid(email);
        }

        return false;
    }

    public static boolean validateMobile(String mobileNo)
    {
        if(ValidateUtil.isStringEmpty(mobileNo))
        {
            return false;
        }
        String mobile = mobileNo.replace(" ", "");
        mobile = mobile.replace("-", "");
        Pattern mobileNoPattern = Pattern.compile(MOBILE_PATTERN);
        Matcher mat = mobileNoPattern.matcher(mobile);
        //OmniLogger.l(TAG, "Mobile Check" + mobile + " " + mat.matches());
        return mat.matches();
    }

    public static boolean isStringEmpty(String s)
    {
        return s == null || s.length() == 0;

    }
}
