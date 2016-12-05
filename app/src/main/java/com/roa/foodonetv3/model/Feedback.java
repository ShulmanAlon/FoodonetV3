package com.roa.foodonetv3.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Feedback implements Parcelable{
    private static final String FEEDBACK = "feedback";
    private static final String ACTIVE_DEVICE_DEV_UUID = "active_device_dev_uuid";
    private static final String REPORTER_NAME = "reporter_name";
    private static final String REPORT = "report";

    private String activeDeviceDevUUID, reporterName, report;

    public Feedback(String activeDeviceDevUUID, String reporterName, String report) {
        this.activeDeviceDevUUID = activeDeviceDevUUID;
        this.reporterName = reporterName;
        this.report = report;
    }

    protected Feedback(Parcel in) {
        activeDeviceDevUUID = in.readString();
        reporterName = in.readString();
        report = in.readString();
    }

    public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };

    private JSONObject getFeedbackJson(){
        JSONObject feedbackRoot = new JSONObject();
        JSONObject feedback = new JSONObject();
        try {
            feedback.put(ACTIVE_DEVICE_DEV_UUID, getActiveDeviceDevUUID());
            feedback.put(REPORTER_NAME, getReporterName());
            feedback.put(REPORT, getReport());

            feedbackRoot.put(FEEDBACK,feedback);
        } catch (JSONException e) {
            Log.e("getPublicationJson", e.getMessage());
        }
        return feedbackRoot;
    }

    public String getActiveDeviceDevUUID() {
        return activeDeviceDevUUID;
    }

    public void setActiveDeviceDevUUID(String activeDeviceDevUUID) {
        this.activeDeviceDevUUID = activeDeviceDevUUID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activeDeviceDevUUID);
        dest.writeString(reporterName);
        dest.writeString(report);
    }
}
