package com.hills.mcs_02.dataBeans;

import java.util.Date;

public class Liveness {
    private Integer livenessId;

    private Integer userId;
    private Integer taskId;
    private Date onlineTime;                 /**  Date of launch */
    private Date deadlineTime;               /** Date of logging */

    private Integer temp;                       /** Variable from 0 to 1 */

    private Integer totalWeek;
    private Integer totalMouth;              /** Record the number of online entries per week */
    private Integer totalYear;               /** Record the number of online entries for a year */
    private Integer total;                    /** Record the total number of lines */

    public Liveness(Integer livenessId, Integer userId, Integer taskId, Date onlineTime, Date deadlineTime, Integer temp, Integer totalWeek, Integer totalMouth, Integer totalYear, Integer total) {
        this.livenessId = livenessId;
        this.userId = userId;
        this.taskId = taskId;
        this.onlineTime = onlineTime;
        this.deadlineTime = deadlineTime;
        this.temp = temp;
        this.totalWeek = totalWeek;
        this.totalMouth = totalMouth;
        this.totalYear = totalYear;
        this.total = total;
    }

    public void setLivenessId(Integer livenessId) {
        this.livenessId = livenessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getTotalYear() {
        return totalYear;
    }

    public void setTotalYear(Integer totalYear) {
        this.totalYear = totalYear;
    }

    @Override
    public String toString() {
        return "Liveness{" +
                "livenessId=" + livenessId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", onlineTime=" + onlineTime +
                ", deadlineTime=" + deadlineTime +
                ", temp=" + temp +
                ", totalWeek=" + totalWeek +
                ", totalMouth=" + totalMouth +
                ", totalYear=" + totalYear +
                ", total=" + total +
                '}';
    }
}
