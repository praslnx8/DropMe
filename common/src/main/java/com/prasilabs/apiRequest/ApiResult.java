package com.prasilabs.apiRequest;

/**
 * Created by prasi on 10/3/16.
 */
public class ApiResult
{
    private boolean isSuccess;
    private String result;
    private String error;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
