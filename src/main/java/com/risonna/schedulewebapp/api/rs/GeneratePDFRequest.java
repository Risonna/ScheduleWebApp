package com.risonna.schedulewebapp.api.rs;

public class GeneratePDFRequest {
    private String html;
    private String taskId;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
