package com.prasilabs.dropme.backend.datastore.pojo;

import com.prasilabs.util.DataUtil;

/**
 * Created by prasi on 27/5/16.
 */
public class VNumber
{
    private String stateName;
    private int districtNo;
    private String areaCode;
    private int no;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(int districtNo) {
        this.districtNo = districtNo;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public boolean isValid()
    {
        boolean isValid = true;

        if(DataUtil.isEmpty(stateName) || stateName.length() > 2)
        {
            isValid = false;
        }
        else if(districtNo == 0)
        {
            isValid = false;
        }
        else if(DataUtil.isEmpty(areaCode))
        {
            isValid = false;
        }
        else if(no == 0 || String.valueOf(no).length() != 4)
        {
            isValid = false;
        }

        return isValid;
    }
}
