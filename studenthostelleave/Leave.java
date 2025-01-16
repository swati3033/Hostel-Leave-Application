package com.example.studenthostelleave;

public class Leave {
    private int id;
    private String name;
    private String hostelName;
    private String prnNumber;
    private String startDate;
    private String endDate;
    private String status;
    private  String reason;
    private  String reply;

    public Leave(int id, String name, String hostelName, String prnNumber, String startDate, String endDate, String status,String reason,String reply) {
        this.id = id;
        this.name = name;
        this.hostelName = hostelName;
        this.prnNumber = prnNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason=reason;
    }

    public Leave() {
        
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHostelName() {
        return hostelName;
    }

    public String getPrnNumber() {
        return prnNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public void setId(int lId) {
        this.id=lId;
    }

    public void setPrnNumber(String prn) {
        this.prnNumber=prn;

    }

    public void setName(String name) {
        this.name=name;
    }

    public void setHostelName(String hostelName) {
        this.hostelName=hostelName;
    }

    public void setStartDate(String startDate) {
        this.startDate=startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate=endDate;
    }
    public void setStatus(String status) {
        this.status=status;
    }

    public void setReason(String reason) {
        this.reason=reason;
    }

    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply=reply;
    }
}
