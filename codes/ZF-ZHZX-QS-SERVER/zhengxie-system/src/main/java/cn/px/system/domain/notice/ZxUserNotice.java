package cn.px.system.domain.notice;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 用户与政协通知公告关联对象 zx_user_notice
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class ZxUserNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;
    /**
     * Y：已读
     */
    private String zxNoticeType;

    /** 公告ID */
    private Long zxNoticeId;

    public String getZxNoticeType() {
        return zxNoticeType;
    }

    public void setZxNoticeType(String zxNoticeType) {
        this.zxNoticeType = zxNoticeType;
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
    public void setZxNoticeId(Long zxNoticeId)
    {
        this.zxNoticeId = zxNoticeId;
    }

    public Long getZxNoticeId()
    {
        return zxNoticeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("zxNoticeId", getZxNoticeId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
