package com.atm.spring.activiti.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 19/07/2017.
 */
public class LeaveModel implements Serializable {
    
    private String id;

    private String userId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date realityStartTime;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date realityEndTime;
    private Date applyTime;
    private String leaveType;
    private String reason;

    private String taskId;

    /**
     * Gets leaveType.
     *
     * @return Value of leaveType.
     */
    public String getLeaveType() {
        return leaveType;
    }

    /**
     * Gets reason.
     *
     * @return Value of reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets new endTime.
     *
     * @param endTime New value of endTime.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets new applyTime.
     *
     * @param applyTime New value of applyTime.
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * Gets realityEndTime.
     *
     * @return Value of realityEndTime.
     */
    public Date getRealityEndTime() {
        return realityEndTime;
    }

    /**
     * Gets realityStartTime.
     *
     * @return Value of realityStartTime.
     */
    public Date getRealityStartTime() {
        return realityStartTime;
    }

    /**
     * Sets new realityEndTime.
     *
     * @param realityEndTime New value of realityEndTime.
     */
    public void setRealityEndTime(Date realityEndTime) {
        this.realityEndTime = realityEndTime;
    }

    /**
     * Sets new startTime.
     *
     * @param startTime New value of startTime.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets applyTime.
     *
     * @return Value of applyTime.
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * Sets new reason.
     *
     * @param reason New value of reason.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets startTime.
     *
     * @return Value of startTime.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Gets endTime.
     *
     * @return Value of endTime.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Gets userId.
     *
     * @return Value of userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets new leaveType.
     *
     * @param leaveType New value of leaveType.
     */
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * Sets new realityStartTime.
     *
     * @param realityStartTime New value of realityStartTime.
     */
    public void setRealityStartTime(Date realityStartTime) {
        this.realityStartTime = realityStartTime;
    }

    /**
     * Sets new userId.
     *
     * @param userId New value of userId.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * Gets taskId.
     *
     * @return Value of taskId.
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Sets new taskId.
     *
     * @param taskId New value of taskId.
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public String getId() {
        return id;
    }
}
