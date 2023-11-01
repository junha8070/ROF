package com.xlntsmmr.xlnt_timeline.DTO;

public class CalendarDayDTO {
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean isSelected;

    public CalendarDayDTO(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isSelected = false; // 초기에는 선택되지 않은 상태로 초기화
    }

    public CalendarDayDTO(int year, int month, int dayOfMonth, boolean isSelected) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isSelected = isSelected; // 초기에는 선택되지 않은 상태로 초기화
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
