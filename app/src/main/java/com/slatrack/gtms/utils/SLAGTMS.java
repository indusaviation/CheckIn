package com.slatrack.gtms.utils;


import android.hardware.usb.UsbDevice;
import android.app.Application;
import 	android.content.res.Configuration;

import com.slatrack.gtms.model.ModelReader;

public class SLAGTMS extends Application {


    private UsbDevice Device;





    public ModelReader getUser() {
        return User;
    }

    public void setUser(ModelReader user) {
        User = user;
    }

    private ModelReader User;
    private String SocietyId;
    private String OrganizationName;
    private String UserName;
    private String ExpiryDate;
    private String PendingUpdates;
    private String LastUpdate;
    private String LastLogin;
    private String TotalSwipeCount = "NA";
    private String TotalExpectedCount ="NA";
    private String TotalMissedCount ="NA";
    private String SwipePercentage ="NA";
    private String LogoPath ="";
    private String IsUpdateNeeded ="YES";
    private String supportphone ="+91 77200 19485";
    private String supportemail ="info@slatrack.com";
    private String appversion ="";
    private String apptype ="";
    private String IsIMEIValidated = ClassCommon.IMEIVALIDATIONERROR;
    private String ServerUpdate;
    private String DeviceUpdate;
    private String IsReaderConnected = "NO";
    private String userRole = "User";
    private String OrganizationId = "0";
    private String FacilityId ="0";
    private String ScanningMode = "0";
    private String enc_vi = "fedcba9876543210";
    private String enc_key = "0123456789abcdef";
    private String sla_percent = "100";
    private String eventcode = "0000";

    public String getEnc_vi() {
        return enc_vi;
    }

    public void setEnc_vi(String enc_vi) {
        this.enc_vi = enc_vi;
    }

    public String getEnc_key() {
        return enc_key;
    }

    public void setEnc_key(String enc_key) {
        this.enc_key = enc_key;
    }

    public String getSla_percent() {
        return sla_percent;
    }

    public void setSla_percent(String sla_percent) {
        this.sla_percent = sla_percent;
    }

    public String getScanningMode() {
        return ScanningMode;
    }

    public void setScanningMode(String scanningMode) {
        ScanningMode = scanningMode;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getIsReaderConnected() {
        return IsReaderConnected;
    }

    public void setIsReaderConnected(String isReaderConnected) {
        IsReaderConnected = isReaderConnected;
    }

    public String getServerUpdate() {
        return ServerUpdate;
    }

    public void setServerUpdate(String serverUpdate) {
        ServerUpdate = serverUpdate;
    }

    public String getDeviceUpdate() {
        return DeviceUpdate;
    }

    public void setDeviceUpdate(String deviceUpdate) {
        DeviceUpdate = deviceUpdate;
    }



    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getSupportphone() {
        return supportphone;
    }

    public void setSupportphone(String supportphone) {
        this.supportphone = supportphone;
    }

    public String getSupportemail() {
        return supportemail;
    }

    public void setSupportemail(String supportemail) {
        this.supportemail = supportemail;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getFacilityId() {
        return FacilityId;
    }

    public void setFacilityId(String facilityId) {
        FacilityId = facilityId;
    }



    public String getIsIMEIValidated() {
        return IsIMEIValidated;
    }

    public void setIsIMEIValidated(String isIMEIValidated) {
        IsIMEIValidated = isIMEIValidated;
    }



    public String getUpdateNeeded() {
        return IsUpdateNeeded;
    }

    public void setUpdateNeeded(String updateNeeded) {
        IsUpdateNeeded = updateNeeded;
    }

    public String getLogoPath() {
        return LogoPath;
    }

    public void setLogoPath(String logoPath) {
        LogoPath = logoPath;
    }

    public String getTotalSwipeCount() {
        return TotalSwipeCount;
    }

    public void setTotalSwipeCount(String totalSwipeCount) {
        TotalSwipeCount = totalSwipeCount;
    }

    public String getTotalExpectedCount() {
        return TotalExpectedCount;
    }

    public void setTotalExpectedCount(String totalExpectedCount) {
        TotalExpectedCount = totalExpectedCount;
    }

    public String getTotalMissedCount() {
        return TotalMissedCount;
    }

    public void setTotalMissedCount(String totalMissedCount) {
        TotalMissedCount = totalMissedCount;
    }

    public String getSwipePercentage() {
        return SwipePercentage;
    }

    public void setSwipePercentage(String swipePercentage) {
        SwipePercentage = swipePercentage;
    }



    public String getReaderCode() {
        return ReaderCode;
    }

    public void setReaderCode(String readerCode) {
        ReaderCode = readerCode;
    }

    private String ReaderCode;

    public UsbDevice getDevice() {
        return Device;
    }

    public void setDevice(UsbDevice device) {
        Device = device;
    }

    public void resetDevice() {
        IsReaderConnected = "NO";
    }

    public String getSocietyId() {
        return SocietyId;
    }

    public void setSocietyId(String societyId) {
        SocietyId = societyId;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getPendingUpdates() {
        return PendingUpdates;
    }

    public void setPendingUpdates(String pendingUpdates) {
        PendingUpdates = pendingUpdates;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }

    public String getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(String lastLogin) {
        LastLogin = lastLogin;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    private String imei;


    @Override
    public void onCreate() {
        super.onCreate();

        SocietyId = "0";
        OrganizationName= "Indus Aviation Systems";
        UserName = "Indus Aviation";
        ExpiryDate = "0";
        PendingUpdates = "0";
        LastUpdate = "0";
        LastLogin = "0";
        TotalSwipeCount = "0";
        TotalExpectedCount = "0";
        TotalMissedCount = "0";
        LogoPath = ClassCommon.DEFAULT_LOGOPATH;
        IsUpdateNeeded="YES";
        IsReaderConnected = "NO";
        userRole = "User";
        ScanningMode = "0";
        enc_vi = "fedcba9876543210";
        enc_key = "0123456789abcdef";
        sla_percent = "100";
        eventcode = "0000";

        //reinitialize variable
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode;
    }
}