package cn.px.system.domain.conference;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 政协会议活动-用户关联对象 zx_conference_user
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class ZxConferenceUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议活动ID */
    private Long conferenceId;

    /** 用户ID/委员信息表id */
    private Long userId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String userName;
    /**
     * 手机号码
     */
    private String phonenumber;

    /** 委员职务 */
    @Excel(name = "委员职务")
    private String postName;

    /** 级别 */
    @Excel(name = "级别")
    private String userLevel;
    /**
     * 父级别
     */
    private String parentGrade;


    /** 答复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "答复时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签到时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signIn;

    /** 是否参会 N:不参加 Y：参加 */
    @Excel(name = "是否参会 N:不参加 Y：参加")
    private String isJoin;

    /** 请假类型 */
    @Excel(name = "请假类型")
    private String leaveType;

    /** 请假原因 */
    @Excel(name = "请假原因")
    private String leaveReason;
    /**
     * 是否审批 1：待审批 2：已同意 3：未同意
     */
    private String approval;

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getParentGrade() {
        return parentGrade;
    }

    public void setParentGrade(String parentGrade) {
        this.parentGrade = parentGrade;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getSignIn() {
        return signIn;
    }

    public void setSignIn(Date signIn) {
        this.signIn = signIn;
    }

    public void setConferenceId(Long conferenceId)
    {
        this.conferenceId = conferenceId;
    }

    public Long getConferenceId()
    {
        return conferenceId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setPostName(String postName)
    {
        this.postName = postName;
    }

    public String getPostName()
    {
        return postName;
    }
    public void setUserLevel(String userLevel)
    {
        this.userLevel = userLevel;
    }

    public String getUserLevel()
    {
        return userLevel;
    }
    public void setReplyTime(Date replyTime)
    {
        this.replyTime = replyTime;
    }

    public Date getReplyTime()
    {
        return replyTime;
    }
    public void setIsJoin(String isJoin)
    {
        this.isJoin = isJoin;
    }

    public String getIsJoin()
    {
        return isJoin;
    }
    public void setLeaveType(String leaveType)
    {
        this.leaveType = leaveType;
    }

    public String getLeaveType()
    {
        return leaveType;
    }
    public void setLeaveReason(String leaveReason)
    {
        this.leaveReason = leaveReason;
    }

    public String getLeaveReason()
    {
        return leaveReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("conferenceId", getConferenceId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("postName", getPostName())
            .append("userLevel", getUserLevel())
            .append("replyTime", getReplyTime())
            .append("isJoin", getIsJoin())
            .append("leaveType", getLeaveType())
            .append("leaveReason", getLeaveReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
