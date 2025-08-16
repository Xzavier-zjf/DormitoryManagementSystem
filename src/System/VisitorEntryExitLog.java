package System;

import java.sql.Timestamp;

public class VisitorEntryExitLog {
    private int logId;
    private String visitorName;
    private String visitorPhone;
    private String visitorPurpose;
    private Timestamp entryTime;
    private Timestamp exitTime;

    public VisitorEntryExitLog(int logId, String visitorName, String visitorPhone, String visitorPurpose, Timestamp entryTime, Timestamp exitTime) {
        this.logId = logId;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.visitorPurpose = visitorPurpose;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorPurpose() {
        return visitorPurpose;
    }

    public void setVisitorPurpose(String visitorPurpose) {
        this.visitorPurpose = visitorPurpose;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }
}
