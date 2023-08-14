package com.base.sbc.module.review.vo;

import com.base.sbc.config.common.base.BaseDataEntity;

public class ReviewResultVo extends BaseDataEntity<String> {
    /** 禁用 0 启用 1 */
    private String status;
    /** 会议类型id */
    private String meetingType;
    /** 会议类型名称 */
    private String meetingTypeName;
    /** 通过 */
    private String passThrough;
    /** 通过评审结果 */
    private String passResult;
    /** 不通过 */
    private String noPass;
    /** 不通过评审结果 */
    private String noPassResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingTypeName() {
        return meetingTypeName;
    }

    public void setMeetingTypeName(String meetingTypeName) {
        this.meetingTypeName = meetingTypeName;
    }

    public String getPassThrough() {
        return passThrough;
    }

    public void setPassThrough(String passThrough) {
        this.passThrough = passThrough;
    }

    public String getPassResult() {
        return passResult;
    }

    public void setPassResult(String passResult) {
        this.passResult = passResult;
    }

    public String getNoPass() {
        return noPass;
    }

    public void setNoPass(String noPass) {
        this.noPass = noPass;
    }

    public String getNoPassResult() {
        return noPassResult;
    }

    public void setNoPassResult(String noPassResult) {
        this.noPassResult = noPassResult;
    }
}
