package com.xlntsmmr.xlnt_timeline.DTO;

public class ContentDTO {

    int status = 0;
    String content = "";
    String category_uuid = "";
    String rof_uuid = "";
    Boolean alarm = false;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory_uuid() {
        return category_uuid;
    }

    public void setCategory_uuid(String category_uuid) {
        this.category_uuid = category_uuid;
    }

    public String getRof_uuid() {
        return rof_uuid;
    }

    public void setRof_uuid(String rof_uuid) {
        this.rof_uuid = rof_uuid;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }
}
