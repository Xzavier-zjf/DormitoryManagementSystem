package System;

import java.sql.Timestamp;

public class EntryExitLog {
    private int logId;
    private String studentNumber;
    private Timestamp entryTime;
    private Timestamp exitTime;

    public EntryExitLog(int logId, String studentNumber, Timestamp entryTime, Timestamp exitTime) {
        this.logId = logId;
        this.studentNumber = studentNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public int getLogId() {
        return logId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }
}
