package com.xlntsmmr.xlnt_timeline.DTO;

public class TimeLineDTO {

    String UUID;
    TimeDTO timeDTO;
    ContentDTO contentDTO;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public TimeDTO getTimeDTO() {
        return timeDTO;
    }

    public void setTimeDTO(TimeDTO timeDTO) {
        this.timeDTO = timeDTO;
    }

    public void setContentDTO(ContentDTO contentDTO) {
        this.contentDTO = contentDTO;
    }

    public ContentDTO getContentDTO() {
        return contentDTO;
    }
}
