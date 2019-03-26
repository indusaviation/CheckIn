package com.slatrack.gtms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.slatrack.gtms.model.ModelAppInfo;
import com.slatrack.gtms.model.ModelAuditInfo;
import com.slatrack.gtms.model.ModelCheckINData;
import com.slatrack.gtms.model.ModelCheckpoints;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.model.ModelEvents;
import com.slatrack.gtms.model.ModelFeatures;
import com.slatrack.gtms.model.ModelLastupdate;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.model.ModelSchedule;
import com.slatrack.gtms.model.ModelShift;
import com.slatrack.gtms.model.ModelSlaParam;
import com.slatrack.gtms.model.ModelSosMessage;
import com.slatrack.gtms.model.ModelSubscription;

import java.util.ArrayList;

public class ClassDatabase extends SQLiteOpenHelper {
    private static String DB_NAME = "SLAGTMS";
    private static int DB_VERSION = 1;

    public ClassDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static String READER = "reader";
    private static String name = "name";
    private static String imei1 = "imei1";
    private static String imei2 = "imei2";
    private static String role = "role";
    private static String firstname = "firstname";
    private static String middelname = "middelname";
    private static String lastname = "lastname";
    private static String amount = "amount";
    private static String user_id = "user_id";
    private static String expiry_date = "expiry_date";
    private static String readercode = "readercode";
    private static String society_id = "society_id";
    private static String organization_id = "organization_id";
    private static String facility_id = "facility_id";
    private static String subtype = "subtype";
    private static String readerstatus = "readerstatus";

    private static String APPINFO = "APPINFO";
    private static String appversion = "appversion";
    private static String apptype = "apptype";
    private static String supportphone = "supportphone";
    private static String supportemail = "supportemail";

    private static String AUDITINFO = "AUDITINFO";
    private static String lastlogintime = "lastlogintime";
    private static String lastvalidationdate = "lastvalidationdate";
    private static String latestupdate = "latestupdate";
    private static String totalrecordsupdated = "totalrecordsupdated";
    private static String firstinstalldate = "firstinstalldate";
    private static String lastsynchronizationtime = "lastsynchronizationtime";
    private static String lastregrequested = "lastregrequested";
    private static String lastsubrequested = "lastsubrequested";

    private static String CHECKINDATA = "punchrecords";
    private static String autoIncID = "autoIncID";
    private static String readerID = "readerID";
    private static String checkpoint = "checkpoint";
    private static String swipeTime = "swipeTime";
    private static String swipeDate = "swipeDate";


    private static String EVENTS = "events";
    private static String eventcode = "eventcode";
    private static String eventname = "eventname";
    private static String eventdescription = "eventdescription";
    private static String eventperiod = "eventperiod";
    private static String eventfrequency = "eventfrequency";


    private static String CHECKPOINTS = "checkpoints";
    private static String checkpointcode = "checkpointcode";
    private static String checkpointname = "checkpointname";


    private static String FEATURES = "features";
    private static String enable_camera = "enable_camera";
    private static String enable_gps = "enable_gps";
    private static String enable_sig = "enable_sig";
    private static String enable_selfie = "enable_selfie";
    private static String enable_overide = "enable_overide";
    private static String scan_mode = "scan_mode";

    private static String LASTUPDATE = "lastupdate";
    private static String id = "id";
    private static String lastupdate = "lastupdate";
    private static String needupdate = "needupdate";
    private static String deviceupdate = "deviceupdate";
    private static String serverupdate = "serverupdate";

    private static String SUBSCRIPTIONRATE = "subscription";
    private static String subid = "subid";
    private static String subname = "subname";
    private static String subrates = "subrates";
    private static String subperiod = "subperiod";
    private static String substatus = "substatus";
    private static String suboffertext = "suboffertext";
    private static String subamount = "subamount";

    private static String SHIFT = "shift";
    private static String SCHEDULE = "schedule";
    private static String slashift_id = "slashift_id";
    private static String shiftname = "shiftname";
    private static String shiftfrom = "shiftfrom";
    private static String shiftto = "shiftto";
    private static String slaschedule_id = "slaschedule_id";
    private static String schedulefrom = "schedulefrom";
    private static String scheduleto = "scheduleto";


    private static String ORGANIZATION = "organization";
    private static String orgname = "orgname";
    private static String enablesms = "enablesms";
    private static String enableemail = "enableemail";
    private static String isverified = "isverified";
    private static String primarysos = "primarysos";
    private static String updatemode = "updatemode";
    private static String secmobno = "secmobno";
    private static String societytype = "societytype";
    private static String logopath = "logopath";
    private static String orgstatus = "orgstatus";

    private static String SOSMSG = "sosmessage";
    private static String msgconst = "msgconst";
    private static String msgdetails = "msgdetails";


    private static String SLAPARAM = "slaparam";
    private static String enc_vi = "enc_vi";
    private static String enc_key = "enc_key";
    private static String sla_percent = "sla_percent";

    private static String EVENTDATA = "eventdata";
    private static String gpslat = "gpslat";
    private static String gpslong = "gpslong";

    private static String CreateTb_SLAPARAM = "CREATE TABLE " + SLAPARAM + "(" +
            id + " TEXT, " +
            enc_vi + " TEXT, " +
            enc_key + " TEXT, " +
            sla_percent + " TEXT) ";

    private static String CreateTb_READER = "CREATE TABLE " + READER + "(" +
            name + " TEXT, " +
            imei1 + " TEXT, " +
            imei2 + " TEXT, " +
            role + " TEXT, " +
            firstname + " TEXT, " +
            middelname + " TEXT, " +
            lastname + " TEXT, " +
            amount + " TEXT, " +
            user_id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            readercode + " TEXT, " +
            subname + " TEXT, " +
            readerstatus + " TEXT, " +
            subtype + " TEXT, " +
            substatus + " TEXT, " +
            expiry_date + " TEXT) ";

    private static String CreateTb_CHECKINDATA = "CREATE TABLE " + CHECKINDATA + "(" +
            autoIncID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            readerID + " INTEGER, " +
            checkpoint + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            swipeTime + " TEXT, " +
            swipeDate + " TEXT) ";

    private static String CreateTb_EVENTDATA = "CREATE TABLE " + EVENTDATA + "(" +
            autoIncID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            readerID + " INTEGER, " +
            checkpoint + " TEXT, " +
            eventcode + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            swipeTime + " TEXT, " +
            swipeDate + " TEXT, "+
            gpslat + " TEXT, "+
            gpslong + " TEXT) ";



    private static String CreateTb_EVENTS = "CREATE TABLE " + EVENTS + "(" +
            user_id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            eventcode + " TEXT, " +
            eventname + " TEXT, " +
            eventdescription + " TEXT, " +
            eventperiod + " TEXT, " +
            eventfrequency + " TEXT) ";

    private static String CreateTb_CHECKPOINTS = "CREATE TABLE " + CHECKPOINTS + "(" +
            user_id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            checkpointcode + " TEXT, " +
            checkpointname + " TEXT) ";


    private static String CreateTb_FEATURES = "CREATE TABLE " + FEATURES + "(" +
            user_id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            enable_camera + " TEXT, " +
            enable_gps + " TEXT, " +
            enable_sig + " TEXT, " +
            enable_selfie + " TEXT, " +
            enable_overide + " TEXT, " +
            scan_mode + " TEXT) ";




    private static String CreateTb_LASTUPDATE = "CREATE TABLE " + LASTUPDATE + "(" +
            id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            lastupdate + " TEXT,"+
            deviceupdate + " TEXT,"+
            serverupdate + " TEXT,"+
            needupdate + " TEXT) ";

    private static String CreateTb_SUBSCRIPTIONRATE = "CREATE TABLE " + SUBSCRIPTIONRATE + "(" +
            subid + " TEXT, " +
            subname + " TEXT, " +
            subrates + " TEXT,"+
            subperiod + " TEXT,"+
            substatus + " TEXT,"+
            suboffertext + " TEXT,"+
            subamount + " TEXT) ";


    private static String CreateTb_ORGANIZATION = "CREATE TABLE " + ORGANIZATION + "(" +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            user_id + " TEXT, " +
            orgname + " TEXT,"+
            expiry_date + " TEXT,"+
            enablesms + " TEXT,"+
            enableemail + " TEXT,"+
            isverified + " TEXT,"+
            substatus + " TEXT,"+
            primarysos + " TEXT,"+
            updatemode + " TEXT,"+
            secmobno + " TEXT,"+
            subtype + " TEXT,"+
            logopath + " TEXT,"+
            orgstatus + " TEXT,"+
            societytype + " TEXT) ";



    private static String CreateTb_SHIFT = "CREATE TABLE " + SHIFT + "(" +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            slashift_id + " TEXT, " +
            user_id + " TEXT,"+
            shiftname + " TEXT,"+
            shiftfrom + " TEXT,"+
            shiftto + " TEXT) ";


    private static String CreateTb_SCHEDULE = "CREATE TABLE " + SCHEDULE + "(" +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            slashift_id + " TEXT, " +
            slaschedule_id + " TEXT,"+
            user_id + " TEXT,"+
            shiftname + " TEXT,"+
            schedulefrom + " TEXT,"+
            scheduleto + " TEXT) ";


    private static String CreateTb_SOSMESSAGE = "CREATE TABLE " + SOSMSG + "(" +
            id + " TEXT, " +
            society_id + " TEXT, " +
            organization_id + " TEXT, " +
            facility_id + " TEXT, " +
            msgconst + " TEXT, " +
            msgdetails + " TEXT) ";

    private static String CreateTb_APPINFO = "CREATE TABLE " + APPINFO + "(" +
            id + " TEXT, " +
            appversion + " TEXT, " +
            apptype + " TEXT, " +
            supportemail + " TEXT, " +
            supportphone + " TEXT) ";

    private static String CreateTb_AUDITINFO = "CREATE TABLE " + AUDITINFO + "(" +
            id + " TEXT, " +
            lastlogintime + " TEXT, " +
            lastvalidationdate + " TEXT, " +
            latestupdate + " TEXT, " +
            totalrecordsupdated + " TEXT, " +
            firstinstalldate + " TEXT, " +
            lastsynchronizationtime + " TEXT, " +
            lastregrequested + " TEXT, " +
            lastsubrequested + " TEXT) ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CreateTb_READER);
        sqLiteDatabase.execSQL(CreateTb_CHECKINDATA);
        sqLiteDatabase.execSQL(CreateTb_CHECKPOINTS);
        sqLiteDatabase.execSQL(CreateTb_FEATURES);
        sqLiteDatabase.execSQL(CreateTb_EVENTS);
        sqLiteDatabase.execSQL(CreateTb_LASTUPDATE);
        sqLiteDatabase.execSQL(CreateTb_SUBSCRIPTIONRATE);
        sqLiteDatabase.execSQL(CreateTb_ORGANIZATION);
        sqLiteDatabase.execSQL(CreateTb_SHIFT);
        sqLiteDatabase.execSQL(CreateTb_SCHEDULE);
        sqLiteDatabase.execSQL(CreateTb_SOSMESSAGE);
        sqLiteDatabase.execSQL(CreateTb_APPINFO);
        sqLiteDatabase.execSQL(CreateTb_AUDITINFO);
        sqLiteDatabase.execSQL(CreateTb_EVENTDATA);
        sqLiteDatabase.execSQL(CreateTb_SLAPARAM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //APPINFO

    public void insertSlaParam(ModelSlaParam objSlaParam) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(id, objSlaParam.getId());
            values.put(enc_key, objSlaParam.getEnc_key());
            values.put(enc_vi, objSlaParam.getEnc_vi());
            values.put(sla_percent, objSlaParam.getSla_percent());

            db.insert(SLAPARAM, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSlaParam(ModelSlaParam objSlaParam) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objSlaParam.getId());
            values.put(enc_key, objSlaParam.getEnc_key());
            values.put(enc_vi, objSlaParam.getEnc_vi());
            values.put(sla_percent, objSlaParam.getSla_percent());

            db.update(SLAPARAM, values, id + "=?", new String[]{String.valueOf(objSlaParam.getId())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelSlaParam getSlaParam() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelSlaParam objInfo = new ModelSlaParam();
        try {


            Cursor cursor = db.query(SLAPARAM, new String[]{id, enc_vi, enc_key,sla_percent}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _id = cursor.getString(cursor.getColumnIndex(id));
                String _enc_vi = cursor.getString(cursor.getColumnIndex(enc_vi));
                String _enc_key = cursor.getString(cursor.getColumnIndex(enc_key));
                String _sla_percent = cursor.getString(cursor.getColumnIndex(sla_percent));

                objInfo.setId(_id);
                objInfo.setEnc_vi(_enc_vi);
                objInfo.setEnc_key(_enc_key);
                objInfo.setSla_percent(_sla_percent);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objInfo;
    }

    public void deleteSlaParam(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(SLAPARAM, id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int slaParamCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(SLAPARAM, new String[]{id}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void deleteAllSlaParam() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ SLAPARAM);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//APPINFO

    public void insertAppInfo(ModelAppInfo objAppInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(id, objAppInfo.getId());
            values.put(appversion, objAppInfo.getAppversion());
            values.put(apptype, objAppInfo.getApptype());
            values.put(supportphone, objAppInfo.getSupportphone());
            values.put(supportemail, objAppInfo.getSupportemail());

            db.insert(APPINFO, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAppInfo(ModelAppInfo objAppInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objAppInfo.getId());
            values.put(appversion, objAppInfo.getAppversion());
            values.put(apptype, objAppInfo.getApptype());
            values.put(supportphone, objAppInfo.getSupportphone());
            values.put(supportemail, objAppInfo.getSupportemail());

            db.update(APPINFO, values, id + "=?", new String[]{String.valueOf(objAppInfo.getId())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelAppInfo getAppInfo() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelAppInfo objInfo = new ModelAppInfo();
        try {


            Cursor cursor = db.query(APPINFO, new String[]{id, appversion, apptype,supportphone,supportemail}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _id = cursor.getString(cursor.getColumnIndex(id));
                String _appversion = cursor.getString(cursor.getColumnIndex(appversion));
                String _apptype = cursor.getString(cursor.getColumnIndex(apptype));
                String _supportphone = cursor.getString(cursor.getColumnIndex(supportphone));
                String _supportemail = cursor.getString(cursor.getColumnIndex(supportemail));

                objInfo.setId(_id);
                objInfo.setAppversion(_appversion);
                objInfo.setApptype(_apptype);
                objInfo.setSupportphone(_supportphone);
                objInfo.setSupportemail(_supportemail);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objInfo;
    }

    public void deleteAppInfo(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(APPINFO, id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int appInfoCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(APPINFO, new String[]{id}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void deleteAllAllInfo() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ APPINFO);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


// AUDITINFO


    public void insertAuditInfo(ModelAuditInfo objAuditInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

//            lastlogintime;
//            lastvalidationdate;
//            latestupdate;
//            totalrecordsupdated;
//            firstinstalldate;
//            lastsynchronizationtime;
//            lastregrequested;
//            lastsubrequested;

            ContentValues values = new ContentValues();
            values.put(id, objAuditInfo.getId());
            values.put(lastlogintime, objAuditInfo.getLastlogintime());
            values.put(lastvalidationdate, objAuditInfo.getLastvalidationdate());
            values.put(latestupdate, objAuditInfo.getLatestupdate());
            values.put(totalrecordsupdated, objAuditInfo.getTotalrecordsupdated());
            values.put(firstinstalldate, objAuditInfo.getFirstinstalldate());
            values.put(lastsynchronizationtime, objAuditInfo.getLastsynchronizationtime());
            values.put(lastregrequested, objAuditInfo.getLastregrequested());
            values.put(lastsubrequested, objAuditInfo.getLastsubrequested());

            db.insert(AUDITINFO, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAuditInfo(ModelAuditInfo objAuditInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objAuditInfo.getId());
            values.put(lastlogintime, objAuditInfo.getLastlogintime());
            values.put(lastvalidationdate, objAuditInfo.getLastvalidationdate());
            values.put(latestupdate, objAuditInfo.getLatestupdate());
            values.put(totalrecordsupdated, objAuditInfo.getTotalrecordsupdated());
            values.put(firstinstalldate, objAuditInfo.getFirstinstalldate());
            values.put(lastsynchronizationtime, objAuditInfo.getLastsynchronizationtime());
            values.put(lastregrequested, objAuditInfo.getLastregrequested());
            values.put(lastsubrequested, objAuditInfo.getLastsubrequested());



            db.update(AUDITINFO, values, id + "=?", new String[]{String.valueOf(objAuditInfo.getId())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAppInfoCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.query(APPINFO, new String[]{id}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getAuditInfoCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.query(AUDITINFO, new String[]{id}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ModelAuditInfo getAuditInfo() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelAuditInfo objAuditInfo = new ModelAuditInfo();
        try {


            Cursor cursor = db.query(APPINFO, new String[]{id, lastlogintime, lastvalidationdate,latestupdate,totalrecordsupdated,firstinstalldate,lastsynchronizationtime,lastregrequested,lastsubrequested}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {

                String _id = cursor.getString(cursor.getColumnIndex(id));
                String _lastlogintime = cursor.getString(cursor.getColumnIndex(lastlogintime));
                String _lastvalidationdate = cursor.getString(cursor.getColumnIndex(lastvalidationdate));
                String _latestupdate = cursor.getString(cursor.getColumnIndex(latestupdate));
                String _totalrecordsupdated = cursor.getString(cursor.getColumnIndex(totalrecordsupdated));
                String _firstinstalldate = cursor.getString(cursor.getColumnIndex(firstinstalldate));
                String _lastsynchronizationtime = cursor.getString(cursor.getColumnIndex(lastsynchronizationtime));
                String _lastregrequested = cursor.getString(cursor.getColumnIndex(lastregrequested));
                String _lastsubrequested = cursor.getString(cursor.getColumnIndex(lastsubrequested));

                objAuditInfo.setId(_id);
                objAuditInfo.setLastlogintime(_lastlogintime);
                objAuditInfo.setLastvalidationdate(_lastvalidationdate);
                objAuditInfo.setLatestupdate(_latestupdate);
                objAuditInfo.setTotalrecordsupdated(_totalrecordsupdated);
                objAuditInfo.setFirstinstalldate(_firstinstalldate);
                objAuditInfo.setLastsynchronizationtime(_lastsynchronizationtime);
                objAuditInfo.setLastregrequested(_lastregrequested);
                objAuditInfo.setLastsubrequested(_lastsubrequested);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objAuditInfo;
    }

    public void deleteAuditInfo(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(AUDITINFO, id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //SOS INFORMATION

    public void insertSOSMessage(ModelSosMessage objSosMessage) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(id, objSosMessage.getId());
            values.put(society_id, objSosMessage.getSociety_id());
            values.put(msgconst, objSosMessage.getMsgconst());
            values.put(msgdetails, objSosMessage.getMsgdetails());

            values.put(organization_id, objSosMessage.getOrganization_id());
            values.put(facility_id, objSosMessage.getFacility_id());

            db.insert(SOSMSG, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSOSMessage(ModelSosMessage objSosMessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objSosMessage.getId());
            values.put(society_id, objSosMessage.getSociety_id());
            values.put(msgconst, objSosMessage.getMsgconst());
            values.put(msgdetails, objSosMessage.getMsgdetails());

            values.put(organization_id, objSosMessage.getOrganization_id());
            values.put(facility_id, objSosMessage.getFacility_id());

            db.update(SOSMSG, values, id + "=?", new String[]{String.valueOf(objSosMessage.getId())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelSosMessage getSOSMessage() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelSosMessage objSos = new ModelSosMessage();
        try {


            Cursor cursor = db.query(SOSMSG, new String[]{id, msgconst, msgdetails,society_id,organization_id,facility_id}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {


                String _id = cursor.getString(cursor.getColumnIndex(id));
                String _societyid = cursor.getString(cursor.getColumnIndex(society_id));
                String _msgconst = cursor.getString(cursor.getColumnIndex(msgconst));
                String _msgdetails = cursor.getString(cursor.getColumnIndex(msgdetails));
                String _orgId = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facId = cursor.getString(cursor.getColumnIndex(facility_id));


                objSos.setId(_id);
                objSos.setSociety_id(_societyid);
                objSos.setMsgconst(_msgconst);
                objSos.setMsgdetails(_msgdetails);
                objSos.setOrganizationId(_orgId);
                objSos.setFacilityId(_facId);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSos;
    }

    public ArrayList<ModelSosMessage> getAllSOSMessage() {
        int iCounter = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ModelSosMessage> sosArrayList = new ArrayList<>();
        try {
            Cursor cursor = db.query(SOSMSG, new String[]{id, msgconst, msgdetails,society_id,organization_id,facility_id}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {

                do {
                    ModelSosMessage objSos = new ModelSosMessage();

                    String _id = cursor.getString(cursor.getColumnIndex(id));
                    String _societyid = cursor.getString(cursor.getColumnIndex(society_id));
                    String _msgconst = cursor.getString(cursor.getColumnIndex(msgconst));
                    String _msgdetails = cursor.getString(cursor.getColumnIndex(msgdetails));
                    String _orgId = cursor.getString(cursor.getColumnIndex(organization_id));
                    String _facId = cursor.getString(cursor.getColumnIndex(facility_id));


                    objSos.setId(_id);
                    objSos.setSociety_id(_societyid);
                    objSos.setMsgconst(_msgconst);
                    objSos.setMsgdetails(_msgdetails);
                    objSos.setOrganizationId(_orgId);
                    objSos.setFacilityId(_facId);

                    sosArrayList.add(objSos);
                } while (cursor.moveToNext());

                cursor.close();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return sosArrayList;
    }


    public void deleteSOSMessage(String msgconst) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(SOSMSG, msgconst+"=?", new String[]{String.valueOf(msgconst)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllSOSMessage() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ SOSMSG);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //SCHEDULE


    public void insertSchedule(ModelSchedule objSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(user_id, objSchedule.getUser_id());
            values.put(society_id, objSchedule.getSociety_id());
            values.put(slashift_id, objSchedule.getSlashift_id());
            values.put(slaschedule_id, objSchedule.getSlaschedule_id());
            values.put(shiftname, objSchedule.getShiftname());
            values.put(schedulefrom, objSchedule.getSchedulefrom());
            values.put(scheduleto, objSchedule.getScheduleto());

            values.put(organization_id, objSchedule.getOrganization_id());
            values.put(facility_id, objSchedule.getFacility_id());


            db.insert(SCHEDULE, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSchedule(ModelSchedule objSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, objSchedule.getUser_id());
            values.put(society_id, objSchedule.getSociety_id());
            values.put(slashift_id, objSchedule.getSlashift_id());
            values.put(slaschedule_id, objSchedule.getSlaschedule_id());
            values.put(shiftname, objSchedule.getShiftname());
            values.put(schedulefrom, objSchedule.getSchedulefrom());
            values.put(scheduleto, objSchedule.getScheduleto());
            values.put(organization_id, objSchedule.getOrganization_id());
            values.put(facility_id, objSchedule.getFacility_id());

            db.update(SCHEDULE, values, slaschedule_id + "=?", new String[]{String.valueOf(objSchedule.getSlaschedule_id())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelSchedule getSchedule() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelSchedule objSchedule = new ModelSchedule();
        try {


            Cursor cursor = db.query(SCHEDULE, new String[]{user_id, society_id, organization_id,facility_id, slashift_id,slaschedule_id,shiftname,schedulefrom,scheduleto}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {


                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _slashift_id = cursor.getString(cursor.getColumnIndex(slashift_id));
                String _slaschedule_id = cursor.getString(cursor.getColumnIndex(slaschedule_id));
                String _shiftname = cursor.getString(cursor.getColumnIndex(shiftname));
                String _schedulefrom = cursor.getString(cursor.getColumnIndex(schedulefrom));
                String _scheduleto = cursor.getString(cursor.getColumnIndex(scheduleto));

                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));



                objSchedule.setUser_id(_user_id);
                objSchedule.setSociety_id(_society_id);
                objSchedule.setSlashift_id(_slashift_id);
                objSchedule.setSlashift_id(_slaschedule_id);
                objSchedule.setShiftname(_shiftname);
                objSchedule.setSchedulefrom(_schedulefrom);
                objSchedule.setScheduleto(_scheduleto);
                objSchedule.setOrganizationId(_orgid);
                objSchedule.setFacilityId(_facid);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSchedule;
    }

    public ArrayList<ModelSchedule> getAllSchedules() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ModelSchedule> schedulesArrayList = new ArrayList<>();
        try {
            Cursor cursor = db.query(SCHEDULE, new String[]{user_id, society_id, organization_id,facility_id,slashift_id, slaschedule_id, shiftname, schedulefrom, scheduleto}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {

                do {
                    ModelSchedule objSchedule = new ModelSchedule();

                    String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                    String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                    String _slashift_id = cursor.getString(cursor.getColumnIndex(slashift_id));
                    String _slaschedule_id = cursor.getString(cursor.getColumnIndex(slaschedule_id));
                    String _shiftname = cursor.getString(cursor.getColumnIndex(shiftname));
                    String _schedulefrom = cursor.getString(cursor.getColumnIndex(schedulefrom));
                    String _scheduleto = cursor.getString(cursor.getColumnIndex(scheduleto));
                    String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                    String _facid = cursor.getString(cursor.getColumnIndex(facility_id));



                    objSchedule.setUser_id(_user_id);
                    objSchedule.setSociety_id(_society_id);
                    objSchedule.setSlashift_id(_slashift_id);
                    objSchedule.setSlashift_id(_slaschedule_id);
                    objSchedule.setShiftname(_shiftname);
                    objSchedule.setSchedulefrom(_schedulefrom);
                    objSchedule.setScheduleto(_scheduleto);
                    objSchedule.setOrganizationId(_orgid);
                    objSchedule.setFacilityId(_facid);
                    schedulesArrayList.add(objSchedule);

                } while (cursor.moveToNext());

                cursor.close();
            }

            } catch(Exception e){
                e.printStackTrace();
            }
            return schedulesArrayList;
        }


    public void deleteSchedule(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(SCHEDULE, slaschedule_id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllSchedule() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ SCHEDULE);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //SHIFT

    public void insertShift(ModelShift objShift) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(user_id, objShift.getUser_id());
            values.put(society_id, objShift.getSociety_id());
            values.put(slashift_id, objShift.getSlashift_id());
            values.put(shiftname, objShift.getShiftname());
            values.put(shiftfrom, objShift.getShiftfrom());
            values.put(shiftto, objShift.getShiftto());
            values.put(organization_id, objShift.getOrganization_id());
            values.put(facility_id, objShift.getFacility_id());

            db.insert(SHIFT, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShift(ModelShift objShift) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, objShift.getUser_id());
            values.put(society_id, objShift.getSociety_id());
            values.put(slashift_id, objShift.getSlashift_id());
            values.put(shiftname, objShift.getShiftname());
            values.put(shiftfrom, objShift.getShiftfrom());
            values.put(shiftto, objShift.getShiftto());
            values.put(organization_id, objShift.getOrganization_id());
            values.put(facility_id, objShift.getFacility_id());


            db.update(SHIFT, values, slashift_id + "=?", new String[]{String.valueOf(objShift.getSlashift_id())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelShift getShift() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelShift objShift = new ModelShift();
        try {


            Cursor cursor = db.query(SHIFT, new String[]{user_id, society_id, organization_id,facility_id,slashift_id,shiftname,shiftfrom,shiftto}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {


                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _slashift_id = cursor.getString(cursor.getColumnIndex(slashift_id));
                String _shiftname = cursor.getString(cursor.getColumnIndex(shiftname));
                String _shiftfrom = cursor.getString(cursor.getColumnIndex(shiftfrom));
                String _shiftto = cursor.getString(cursor.getColumnIndex(shiftto));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));




                objShift.setUser_id(_user_id);
                objShift.setSociety_id(_society_id);
                objShift.setSlashift_id(_slashift_id);
                objShift.setShiftname(_shiftname);
                objShift.setShiftfrom(_shiftfrom);
                objShift.setShiftto(_shiftto);
                objShift.setOrganizationId(_orgid);
                objShift.setFacilityId(_facid);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objShift;
    }

    public ArrayList<ModelShift> getAllShifts() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ModelShift> shiftArrayList = new ArrayList<>();
        try {
            Cursor cursor = db.query(SHIFT, new String[]{user_id, society_id,organization_id,facility_id, slashift_id,shiftname,shiftfrom,shiftto}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do{
                    ModelShift objShift = new ModelShift();
                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _slashift_id = cursor.getString(cursor.getColumnIndex(slashift_id));
                String _shiftname = cursor.getString(cursor.getColumnIndex(shiftname));
                String _shiftfrom = cursor.getString(cursor.getColumnIndex(shiftfrom));
                String _shiftto = cursor.getString(cursor.getColumnIndex(shiftto));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));



                objShift.setUser_id(_user_id);
                objShift.setSociety_id(_society_id);
                objShift.setSlashift_id(_slashift_id);
                objShift.setShiftname(_shiftname);
                objShift.setShiftfrom(_shiftfrom);
                objShift.setShiftto(_shiftto);
                objShift.setOrganizationId(_orgid);
                objShift.setFacilityId(_facid);

                shiftArrayList.add(objShift);
                }while (cursor.moveToNext());

            }
            cursor.close();

        } catch(Exception e){
            e.printStackTrace();
        }
        return shiftArrayList;
    }
    public void deleteShift(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(SHIFT, slashift_id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllShift() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ SHIFT);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ORGANIZATION

    public void insertOrganization(ModelOrganization objOrganization) {
        SQLiteDatabase db = this.getWritableDatabase();


        try {


            ContentValues values = new ContentValues();
            values.put(orgname, objOrganization.getOrgname());
            values.put(user_id, objOrganization.getUser_id());
            values.put(society_id, objOrganization.getSociety_id());
            values.put(expiry_date, objOrganization.getExpiry_date());
            values.put(enablesms, objOrganization.getEnablesms());
            values.put(enableemail, objOrganization.getEnableemail());
            values.put(isverified, objOrganization.getIsverified());
            values.put(primarysos, objOrganization.getPrimarysos());
            values.put(updatemode, objOrganization.getUpdatemode());
            values.put(secmobno, objOrganization.getSecmobno());
            values.put(subtype, objOrganization.getSubtype());
            values.put(societytype, objOrganization.getSocietytype());
            values.put(logopath, objOrganization.getLogopath());
            values.put(organization_id, objOrganization.getOrganization_id());
            values.put(facility_id, objOrganization.getFacility_id());
            values.put(substatus, objOrganization.getSubstatus());
            values.put(orgstatus, objOrganization.getOrgstatus());

            db.insert(ORGANIZATION, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrganization(ModelOrganization objOrganization) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(orgname, objOrganization.getOrgname());
            values.put(user_id, objOrganization.getUser_id());
            values.put(society_id, objOrganization.getSociety_id());
            values.put(expiry_date, objOrganization.getExpiry_date());
            values.put(enablesms, objOrganization.getEnablesms());
            values.put(enableemail, objOrganization.getEnableemail());
            values.put(isverified, objOrganization.getIsverified());
            values.put(primarysos, objOrganization.getPrimarysos());
            values.put(updatemode, objOrganization.getUpdatemode());
            values.put(secmobno, objOrganization.getSecmobno());
            values.put(subtype, objOrganization.getSubtype());
            values.put(societytype, objOrganization.getSocietytype());
            values.put(logopath, objOrganization.getLogopath());
            values.put(organization_id, objOrganization.getOrganization_id());
            values.put(facility_id, objOrganization.getFacility_id());
            values.put(substatus, objOrganization.getSubstatus());
            values.put(orgstatus, objOrganization.getOrgstatus());


            db.update(ORGANIZATION, values, organization_id + "=?", new String[]{String.valueOf(objOrganization.getOrganization_id())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelOrganization getOrganization() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelOrganization objOrganization = new ModelOrganization();
        try {


            Cursor cursor = db.query(ORGANIZATION, new String[]{orgname, user_id, society_id,organization_id,facility_id,expiry_date,enablesms,enableemail,isverified,primarysos,updatemode,secmobno,subtype,societytype,substatus,orgstatus,logopath}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {


                String _orgname = cursor.getString(cursor.getColumnIndex(orgname));
                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _ordid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));
                String _expiry_date = cursor.getString(cursor.getColumnIndex(expiry_date));
                String _enablesms = cursor.getString(cursor.getColumnIndex(enablesms));
                String _enableemail = cursor.getString(cursor.getColumnIndex(enableemail));
                String _isverified = cursor.getString(cursor.getColumnIndex(isverified));
                String _primarysos = cursor.getString(cursor.getColumnIndex(primarysos));
                String _updatemode = cursor.getString(cursor.getColumnIndex(updatemode));
                String _secmobno = cursor.getString(cursor.getColumnIndex(secmobno));
                String _subtype = cursor.getString(cursor.getColumnIndex(subtype));
                String _societytype = cursor.getString(cursor.getColumnIndex(societytype));
                String _substatus = cursor.getString(cursor.getColumnIndex(substatus));
                String _orgstatus = cursor.getString(cursor.getColumnIndex(orgstatus));
                String _logopath = cursor.getString(cursor.getColumnIndex(logopath));

                objOrganization.setOrgname(_orgname);
                objOrganization.setUser_id(_user_id);
                objOrganization.setSociety_id(_society_id);
                objOrganization.setExpiry_date(_expiry_date);
                objOrganization.setEnablesms(_enablesms);
                objOrganization.setEnableemail(_enableemail);
                objOrganization.setIsverified(_isverified);
                objOrganization.setPrimarysos(_primarysos);
                objOrganization.setUpdatemode(_updatemode);
                objOrganization.setSecmobno(_secmobno);
                objOrganization.setSubtype(_subtype);
                objOrganization.setSocietytype(_societytype);
                objOrganization.setLogopath(_logopath);
                objOrganization.setOrganizationId(_ordid);
                objOrganization.setFacilityId(_facid);
                objOrganization.setSubstatus(_substatus);
                objOrganization.setOrgstatus(_orgstatus);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objOrganization;
    }





    public void deleteOrganization(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(ORGANIZATION, organization_id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int organizationCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(ORGANIZATION, new String[]{orgname}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void deleteAllOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ ORGANIZATION);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //SUBSCRIPTIONRATE

    public void insertSubscriptionRate(ModelSubscription objSubRates) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(subid, objSubRates.getSubid());
            values.put(subname, objSubRates.getSubname());
            values.put(subrates, objSubRates.getSubrates());
            values.put(subperiod, objSubRates.getSubperiod());
            values.put(substatus, objSubRates.getSubstatus());
            values.put(suboffertext, objSubRates.getSuboffertext());
            values.put(subamount, objSubRates.getSubamount());

            db.insert(SUBSCRIPTIONRATE, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSubscriptionRate(ModelSubscription objSubRates) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(subid, objSubRates.getSubid());
            values.put(subname, objSubRates.getSubname());
            values.put(subrates, objSubRates.getSubrates());
            values.put(subperiod, objSubRates.getSubperiod());
            values.put(substatus, objSubRates.getSubstatus());
            values.put(suboffertext, objSubRates.getSuboffertext());
            values.put(subamount, objSubRates.getSubamount());


            db.update(SUBSCRIPTIONRATE, values, subid + "=?", new String[]{String.valueOf(objSubRates.getSubid())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelSubscription getSubscriptionRates() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelSubscription objSubRates = new ModelSubscription();
        try {


            Cursor cursor = db.query(SUBSCRIPTIONRATE, new String[]{subid, subname, subrates,subperiod,substatus,suboffertext,subamount}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _subid = cursor.getString(cursor.getColumnIndex(subid));
                String _subname = cursor.getString(cursor.getColumnIndex(subname));
                String _subrates = cursor.getString(cursor.getColumnIndex(subrates));
                String _subperiod = cursor.getString(cursor.getColumnIndex(subperiod));
                String _substatus = cursor.getString(cursor.getColumnIndex(substatus));
                String _suboffertext = cursor.getString(cursor.getColumnIndex(suboffertext));
                String _subamount = cursor.getString(cursor.getColumnIndex(subamount));

                objSubRates.setSubid(_subid);
                objSubRates.setSubname(_subname);
                objSubRates.setSubrates(_subrates);
                objSubRates.setSubperiod(_subperiod);
                objSubRates.setSubstatus(_substatus);
                objSubRates.setSuboffertext(_suboffertext);
                objSubRates.setSubamount(_subamount);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSubRates;
    }

    public ArrayList<ModelSubscription>  getAllSubscriptionRates() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ModelSubscription> subrateArrayList = new ArrayList<>();
        try {

             Cursor cursor = db.query(SUBSCRIPTIONRATE, new String[]{subid, subname, subrates,subperiod,substatus,suboffertext,subamount}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    ModelSubscription subRates = new ModelSubscription();
                    String _subid = cursor.getString(cursor.getColumnIndex(subid));
                    String _subname = cursor.getString(cursor.getColumnIndex(subname));
                    String _subrates = cursor.getString(cursor.getColumnIndex(subrates));
                    String _subperiod = cursor.getString(cursor.getColumnIndex(subperiod));
                    String _substatus = cursor.getString(cursor.getColumnIndex(substatus));
                    String _suboffertext = cursor.getString(cursor.getColumnIndex(suboffertext));
                    String _subamount = cursor.getString(cursor.getColumnIndex(subamount));

                    subRates.setSubid(_subid);
                    subRates.setSubname(_subname);
                    subRates.setSubrates(_subrates);
                    subRates.setSubperiod(_subperiod);
                    subRates.setSubstatus(_substatus);
                    subRates.setSuboffertext(_suboffertext);
                    subRates.setSubamount(_subamount);
                    subrateArrayList.add(subRates);

                }while (cursor.moveToNext());

                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return subrateArrayList;
    }

    public void deleteSubscriptionRates(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(SUBSCRIPTIONRATE, subid+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllSubscriptionRates() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ SUBSCRIPTIONRATE);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //LAST UPDATE

    public void insertLastupdate(ModelLastupdate objlastupdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objlastupdate.getId());
            values.put(society_id, objlastupdate.getSociety_id());
            values.put(lastupdate, objlastupdate.getLastupdate());
            values.put(needupdate, objlastupdate.getNeedupdate());
            values.put(organization_id, objlastupdate.getOrganization_id());
            values.put(facility_id, objlastupdate.getFacility_id());
            values.put(serverupdate, objlastupdate.getServerupdate());
            values.put(deviceupdate, objlastupdate.getDeviceupdate());

            db.insert(LASTUPDATE, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLastupdate(ModelLastupdate objlastupdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(id, objlastupdate.getId());
            values.put(society_id, objlastupdate.getSociety_id());
            values.put(lastupdate, objlastupdate.getLastupdate());
            values.put(needupdate, objlastupdate.getNeedupdate());
            values.put(organization_id, objlastupdate.getOrganization_id());
            values.put(facility_id, objlastupdate.getFacility_id());
            values.put(serverupdate, objlastupdate.getServerupdate());
            values.put(deviceupdate, objlastupdate.getDeviceupdate());

            db.update(LASTUPDATE, values, facility_id + "=?", new String[]{String.valueOf(objlastupdate.getFacility_id())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelLastupdate getLastupdate() {

        SQLiteDatabase db = this.getReadableDatabase();
        ModelLastupdate objlastupdate = new ModelLastupdate();
        try {

            Cursor cursor = db.query(LASTUPDATE, new String[]{id, society_id,organization_id,facility_id,lastupdate,needupdate,serverupdate,deviceupdate}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _id = cursor.getString(cursor.getColumnIndex(id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _lastupdate = cursor.getString(cursor.getColumnIndex(lastupdate));
                String _needupdate = cursor.getString(cursor.getColumnIndex(needupdate));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));
                String _serverupdate = cursor.getString(cursor.getColumnIndex(serverupdate));
                String _deviceupdate = cursor.getString(cursor.getColumnIndex(deviceupdate));

                objlastupdate.setId(_id);
                objlastupdate.setSociety_id(_society_id);
                objlastupdate.setLastupdate(_lastupdate);
                objlastupdate.setNeedupdate(_needupdate);
                objlastupdate.setOrganizationId(_orgid);
                objlastupdate.setFacilityId(_facid);
                objlastupdate.setServerupdate(_serverupdate);
                objlastupdate.setDeviceupdate(_deviceupdate);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlastupdate;
    }

    public void deleteLastupdate(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(LASTUPDATE, facility_id+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllLastupdate() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ LASTUPDATE);

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLastUpdate( String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(lastupdate, "0");
            values.put(needupdate, "0");
            values.put(deviceupdate, "0");

            String where = "id="+id;

            db.update(LASTUPDATE, values, where, null);


            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public int lastupdateCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(LASTUPDATE, new String[]{id}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    //Checkpoint Data

    public void insertCheckpoint(ModelCheckpoints checkpoints) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, checkpoints.getUser_id());
            values.put(society_id, checkpoints.getSociety_id());
            values.put(checkpointcode, checkpoints.getCheckpointcode());
            values.put(checkpointname, checkpoints.getCheckpointname());

            values.put(organization_id, checkpoints.getOrganization_id());
            values.put(facility_id, checkpoints.getFacility_id());

            db.insert(CHECKPOINTS, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCheckpoint(ModelCheckpoints checkpoints) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, checkpoints.getUser_id());
            values.put(society_id, checkpoints.getSociety_id());
            values.put(checkpointcode, checkpoints.getCheckpointcode());
            values.put(checkpointname, checkpoints.getCheckpointname());
            values.put(organization_id, checkpoints.getOrganization_id());
            values.put(facility_id, checkpoints.getFacility_id());

            db.update(CHECKPOINTS, values, checkpointcode + "=?", new String[]{String.valueOf(checkpoints.getCheckpointcode())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkpointCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(CHECKPOINTS, new String[]{checkpointcode, checkpointname}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ModelCheckpoints getCheckpoints() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ModelCheckpoints checkpoints = new ModelCheckpoints();
        try {

            Cursor cursor = db.query(CHECKPOINTS, new String[]{user_id,society_id,organization_id,facility_id, checkpointcode, checkpointname}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _checkpointcode = cursor.getString(cursor.getColumnIndex(checkpointcode));
                String _checkpointname = cursor.getString(cursor.getColumnIndex(checkpointname));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));


                checkpoints.setUser_id(_user_id);
                checkpoints.setSociety_id(_society_id);
                checkpoints.setCheckpointcode(_checkpointcode);
                checkpoints.setCheckpointname(_checkpointname);
                checkpoints.setOrganizationId(_orgid);
                checkpoints.setFacilityId(_facid);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkpoints;
    }

    public ArrayList<ModelCheckpoints> getAllCheckpoints() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ModelCheckpoints> checkpointsArrayList = new ArrayList<>();
        try {

            Cursor cursor = db.query(CHECKPOINTS, new String[]{user_id,society_id,organization_id,facility_id, checkpointcode, checkpointname}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    ModelCheckpoints checkpoints = new ModelCheckpoints();
                    String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                    String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                    String _checkpointcode = cursor.getString(cursor.getColumnIndex(checkpointcode));
                    String _checkpointname = cursor.getString(cursor.getColumnIndex(checkpointname));
                    String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                    String _facid = cursor.getString(cursor.getColumnIndex(facility_id));

                    checkpoints.setUser_id(_user_id);
                    checkpoints.setSociety_id(_society_id);
                    checkpoints.setCheckpointcode(_checkpointcode);
                    checkpoints.setCheckpointname(_checkpointname);
                    checkpoints.setOrganizationId(_orgid);
                    checkpoints.setFacilityId(_facid);

                    checkpointsArrayList.add(checkpoints);
                }while (cursor.moveToNext());
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkpointsArrayList;
    }

    public void deleteCheckpoints(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(CHECKPOINTS, checkpointcode+"=?", new String[]{String.valueOf(code)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllCheckpoints() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ CHECKPOINTS);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ModelEvents Data

    public void insertEvents(ModelEvents events) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, events.getUser_id());
            values.put(society_id, events.getSociety_id());
            values.put(eventcode, events.getEventcode());
            values.put(eventname, events.getEventname());
            values.put(eventdescription, events.getEventdescription());
            values.put(eventfrequency, events.getEventfrequency());
            values.put(eventperiod, events.getEventperiod());

            values.put(organization_id, events.getOrganization_id());
            values.put(facility_id, events.getFacility_id());

            db.insert(EVENTS, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEvents(ModelEvents events) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, events.getUser_id());
            values.put(society_id, events.getSociety_id());
            values.put(eventcode, events.getEventcode());
            values.put(eventname, events.getEventname());
            values.put(eventdescription, events.getEventdescription());
            values.put(eventfrequency, events.getEventfrequency());
            values.put(eventperiod, events.getEventperiod());
            values.put(organization_id, events.getOrganization_id());
            values.put(facility_id, events.getFacility_id());

            String eventwhere  = "eventcode = "+ events.getEventcode();

            db.update(EVENTS, values, eventwhere,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int eventsCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(EVENTS, new String[]{eventcode, eventname}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ModelEvents getEvents() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ModelEvents events = new ModelEvents();
        try {

            Cursor cursor = db.query(EVENTS, new String[]{user_id,society_id,organization_id,facility_id, eventcode, eventname}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _eventcode = cursor.getString(cursor.getColumnIndex(eventcode));
                String _eventname = cursor.getString(cursor.getColumnIndex(eventname));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));


                events.setUser_id(_user_id);
                events.setSociety_id(_society_id);
                events.setEventcode(_eventcode);
                events.setEventname(_eventname);
                events.setUser_id(_user_id);
                events.setSociety_id(_society_id);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }


    public ArrayList<ModelEvents> getAllEvents() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ModelEvents> eventsArrayList = new ArrayList<>();
        try {

            Cursor cursor = db.query(EVENTS, new String[]{organization_id,facility_id,user_id,society_id, eventcode, eventname}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    ModelEvents events = new ModelEvents();
                    String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                    String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                    String _eventcode = cursor.getString(cursor.getColumnIndex(eventcode));
                    String _eventname = cursor.getString(cursor.getColumnIndex(eventname));
                    String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                    String _facid = cursor.getString(cursor.getColumnIndex(facility_id));


                    events.setUser_id(_user_id);
                    events.setSociety_id(_society_id);
                    events.setEventcode(_eventcode);
                    events.setEventname(_eventname);
                    events.setUser_id(_user_id);
                    events.setSociety_id(_society_id);
                    events.setOrganizationId(_orgid);
                    events.setFacilityId(_facid);

                    eventsArrayList.add(events);
                }while (cursor.moveToNext());
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventsArrayList;
    }


    public void deleteEvents(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(EVENTS, eventcode+"=?", new String[]{String.valueOf(code)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ EVENTS);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //FEATURE DATA

    public void insertFeatures(ModelFeatures features) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, features.getUser_id());
            values.put(society_id, features.getSociety_id());
            values.put(enable_camera, features.getEnable_camera());
            values.put(enable_gps, features.getEnable_gps());
            values.put(enable_sig, features.getEnable_sig());
            values.put(enable_selfie, features.getEnable_selfie());
            values.put(enable_overide, features.getEnable_overide());
            values.put(scan_mode, features.getScan_mode());

            values.put(organization_id, features.getOrganization_id());
            values.put(facility_id, features.getFacility_id());

            db.insert(FEATURES, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFeatures(ModelFeatures features) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(user_id, features.getUser_id());
            values.put(society_id, features.getSociety_id());
            values.put(enable_camera, features.getEnable_camera());
            values.put(enable_gps, features.getEnable_gps());
            values.put(enable_sig, features.getEnable_sig());
            values.put(enable_selfie, features.getEnable_selfie());
            values.put(enable_overide, features.getEnable_overide());
            values.put(scan_mode, features.getScan_mode());
            values.put(organization_id, features.getOrganization_id());
            values.put(facility_id, features.getFacility_id());

           // db.update(FEATURES, values, user_id + "=?", new String[]{String.valueOf(features.getUser_id())});  TODO
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int featuresCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(FEATURES, new String[]{enable_camera}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ModelFeatures getFeature() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ModelFeatures features = new ModelFeatures();
        try {

            Cursor cursor = db.query(FEATURES, new String[]{enable_camera,enable_gps,enable_sig,enable_overide,enable_selfie,scan_mode,society_id,organization_id,facility_id}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _camera = cursor.getString(cursor.getColumnIndex(enable_camera));
                String _gps = cursor.getString(cursor.getColumnIndex(enable_gps));
                String _sig = cursor.getString(cursor.getColumnIndex(enable_sig));
                String _scanmode = cursor.getString(cursor.getColumnIndex(scan_mode));
                String _override = cursor.getString(cursor.getColumnIndex(enable_overide));
                String _selfie = cursor.getString(cursor.getColumnIndex(enable_overide));
                String _socid = cursor.getString(cursor.getColumnIndex(society_id));
                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));

                features.setEnable_camera(_camera);
                features.setEnable_gps(_gps);
                features.setEnable_sig(_sig);
                features.setEnable_selfie(_selfie);
                features.setEnable_overide(_override);
                features.setScan_mode(_scanmode);


                features.setSociety_id(_socid);
                features.setFacility_id(_facid);
                features.setOrganization_id(_orgid);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return features;
    }

    public void deleteFeatures(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(FEATURES, facility_id+"=?", new String[]{String.valueOf(id)});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllFeatures() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ FEATURES);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //User Data

    public void insertUser(ModelReader objReader) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(name, objReader.getName());
            values.put(imei1, objReader.getImei1());
            values.put(imei2, objReader.getImei2());
            values.put(role, objReader.getRole());
            values.put(firstname, objReader.getFirstname());
            values.put(middelname, objReader.getMiddelname());
            values.put(lastname, objReader.getLastname());
            values.put(amount, objReader.getAmount());
            values.put(user_id, objReader.getUser_id());
            values.put(society_id, objReader.getSociety_id());
            values.put(readercode, objReader.getReadercode());
            values.put(expiry_date, objReader.getExpiryDate());
            values.put(organization_id, objReader.getOrganization_id());
            values.put(facility_id, objReader.getFacility_id());

            values.put(subname, objReader.getSubname());
            values.put(readerstatus, objReader.getReaderstatus());
            values.put(subtype, objReader.getSubtype());
            values.put(substatus, objReader.getSubstatus());

            db.insert(READER, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(ModelReader objReader) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(name, objReader.getName());
            values.put(imei1, objReader.getImei1());
            values.put(imei2, objReader.getImei2());
            values.put(role, objReader.getRole());
            values.put(firstname, objReader.getFirstname());
            values.put(middelname, objReader.getMiddelname());
            values.put(lastname, objReader.getLastname());
            values.put(amount, objReader.getAmount());
            //values.put(user_id,objReader.getUser_id());
            values.put(readercode, objReader.getReadercode());
            values.put(society_id, objReader.getSociety_id());
            values.put(expiry_date, objReader.getExpiryDate());
            values.put(organization_id, objReader.getOrganization_id());
            values.put(facility_id, objReader.getFacility_id());

            values.put(subname, objReader.getSubname());
            values.put(readerstatus, objReader.getReaderstatus());
            values.put(subtype, objReader.getSubtype());
            values.put(substatus, objReader.getSubstatus());

            db.update(READER, values, user_id + "=?", new String[]{String.valueOf(objReader.getUser_id())});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int userCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(READER, new String[]{name, firstname, lastname}, null, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getUserID() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(READER, new String[]{user_id}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(user_id));
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public ModelReader getUser() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ModelReader objReader = new ModelReader();
        try {

            //RD.subtype,RD.substatus,RD.subname,RD.readerstatus
            Cursor cursor = db.query(READER, new String[]{name, imei1, imei2, role, firstname, middelname, lastname, amount, user_id, society_id,organization_id,facility_id,  readercode,expiry_date,subtype,substatus,subname,readerstatus}
                    , null, null, null, null, null);
            if (cursor.moveToFirst()) {
                String _name = cursor.getString(cursor.getColumnIndex(name));
                String _imei1 = cursor.getString(cursor.getColumnIndex(imei1));
                String _imei2 = cursor.getString(cursor.getColumnIndex(imei2));
                String _role = cursor.getString(cursor.getColumnIndex(role));
                String _firstname = cursor.getString(cursor.getColumnIndex(firstname));
                String _middelname = cursor.getString(cursor.getColumnIndex(middelname));
                String _lastname = cursor.getString(cursor.getColumnIndex(lastname));
                String _amount = cursor.getString(cursor.getColumnIndex(amount));
                String _user_id = cursor.getString(cursor.getColumnIndex(user_id));
                String _society_id = cursor.getString(cursor.getColumnIndex(society_id));
                String _readercode = cursor.getString(cursor.getColumnIndex(readercode));
                String _expiry_date = cursor.getString(cursor.getColumnIndex(expiry_date));

                String _orgid = cursor.getString(cursor.getColumnIndex(organization_id));
                String _facid = cursor.getString(cursor.getColumnIndex(facility_id));

                String _subtype = cursor.getString(cursor.getColumnIndex(subtype));
                String _substatus = cursor.getString(cursor.getColumnIndex(substatus));
                String _subname = cursor.getString(cursor.getColumnIndex(subname));
                String _readerstatus = cursor.getString(cursor.getColumnIndex(readerstatus));



                objReader.setName(_name);
                objReader.setImei1(_imei1);
                objReader.setImei2(_imei2);
                objReader.setRole(_role);
                objReader.setFirstname(_firstname);
                objReader.setMiddelname(_middelname);
                objReader.setLastname(_lastname);
                objReader.setAmount(_amount);
                objReader.setUser_id(_user_id);
                objReader.setSociety_id(_society_id);
                objReader.setReadercode(_readercode);
                objReader.setExpiryDate(_expiry_date);
                objReader.setOrganization_id(_orgid);
                objReader.setFacility_id(_facid);

                objReader.setSubtype(_subtype);
                objReader.setSubstatus(_substatus);
                objReader.setSubname(_subname);
                objReader.setReaderstatus(_readerstatus);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objReader;
    }

    public void deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(READER, user_id+"=?", new String[]{String.valueOf(userId)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from "+ READER);
            // db.delete(SUBSCRIPTIONRATE,null);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EVENT DATA
    
    public void insertEventData(ModelEventData eventData) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(readerID, eventData.getReaderID());
            values.put(checkpoint, eventData.getCheckpoint());
            values.put(eventcode, eventData.getEventcode());
            values.put(swipeTime, eventData.getSwipeTime());
            values.put(swipeDate, eventData.getSwipeDate());

            values.put(gpslat, eventData.getGpsLat());
            values.put(gpslong, eventData.getGpsLong());

            db.insert(EVENTDATA, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ModelEventData> getEventDataList(String readerCode) {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ModelEventData> eventDataList = new ArrayList<>();
        try {


            String wherereader = "readerID = "+readerCode;
            Cursor cursor = db.query(EVENTDATA, new String[]{autoIncID, readerID, checkpoint, eventcode,swipeTime,swipeDate,gpslat,gpslong}
                    , wherereader, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int _autoIncID = cursor.getInt(cursor.getColumnIndex(autoIncID));
                    String _readerID = cursor.getString(cursor.getColumnIndex(readerID));
                    String _checkpoint = cursor.getString(cursor.getColumnIndex(checkpoint));
                    String _eventcode = cursor.getString(cursor.getColumnIndex(eventcode));
                    String _swipeTime= cursor.getString(cursor.getColumnIndex(swipeTime));
                    String _swipeDate= cursor.getString(cursor.getColumnIndex(swipeDate));
                    String _gpslat= cursor.getString(cursor.getColumnIndex(gpslat));
                    String _gpslong= cursor.getString(cursor.getColumnIndex(gpslong));


                    ModelEventData eventData = new ModelEventData(_readerID, _checkpoint, _eventcode,_swipeTime,_swipeDate,_gpslat,_gpslong);
                    eventData.setAutoIncID(_autoIncID);
                    eventDataList.add(eventData);

                } while (cursor.moveToNext());

                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return eventDataList;
    }

    public void deleteEventData(int autoID) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(EVENTDATA, autoIncID+"=?", new String[]{String.valueOf(autoID)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllEventData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(EVENTDATA, readerID+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void insertCheckInData(ModelCheckINData checkINData) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(readerID, checkINData.getReaderID());
            values.put(checkpoint, checkINData.getCheckpoint());
            values.put(swipeTime, checkINData.getSwipeTime());
            values.put(swipeDate, checkINData.getSwipeDate());

            db.insert(CHECKINDATA, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ModelCheckINData> getCheckInDataList(String readerCode) {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ModelCheckINData> checkINDataList = new ArrayList<>();
        try {

           // Cursor cursor = db.query(CHECKINDATA, new String[]{autoIncID, readerID, checkpoint, swipeTime,swipeDate}
            //        , readerID=?, null, null, null, null);
            String wherereader = "readerID = "+readerCode;
            Cursor cursor = db.query(CHECKINDATA, new String[]{autoIncID, readerID, checkpoint, swipeTime,swipeDate}
                    , wherereader, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int _autoIncID = cursor.getInt(cursor.getColumnIndex(autoIncID));
                    String _readerID = cursor.getString(cursor.getColumnIndex(readerID));
                    String _checkpoint = cursor.getString(cursor.getColumnIndex(checkpoint));
                    String _swipeTime= cursor.getString(cursor.getColumnIndex(swipeTime));
                    String _swipeDate= cursor.getString(cursor.getColumnIndex(swipeDate));

                    ModelCheckINData checkINData = new ModelCheckINData(_readerID, _checkpoint, _swipeTime,_swipeDate);
                    checkINData.setAutoIncID(_autoIncID);
                    checkINDataList.add(checkINData);

                } while (cursor.moveToNext());

                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return checkINDataList;
    }

    public void deleteCheckIn(int autoID) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(CHECKINDATA, autoIncID+"=?", new String[]{String.valueOf(autoID)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllCheckIn(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(CHECKINDATA, readerID+"=?", new String[]{String.valueOf(id)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
