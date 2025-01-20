package cn.px.system.mapper;


import cn.px.system.domain.conference.ZxConference;
import cn.px.system.domain.conference.ZxConferenceUser;
import java.util.List;

/**
 * 政协会议活动Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ZxConferenceMapper
{
    /**
     * 查询政协会议活动
     *
     * @param conferenceId 政协会议活动主键
     * @return 政协会议活动
     */
    public ZxConference selectZxConferenceByConferenceId(Long conferenceId);
    public ZxConference selectZxConferenceInfo(ZxConference zxConference);
    /**
     *查询政协会议活动人员详情
     * @param conferenceId 政协会议活动主键
     * @return
     */
    public List<ZxConferenceUser> selectZxConferenceUserByConferenceId(Long conferenceId);
    /**
     *查询政协会议活动人员详情
     * @return
     */
    public List<ZxConferenceUser> selectZxConferenceUser(ZxConferenceUser zxConferenceUser);

    /**
     * 查询政协会议活动列表
     *
     * @param zxConference 政协会议活动
     * @return 政协会议活动集合
     */
    public List<ZxConference> selectZxConferenceList(ZxConference zxConference);

    /**
     * 新增政协会议活动
     *
     * @param zxConference 政协会议活动
     * @return 结果
     */
    public int insertZxConference(ZxConference zxConference);

    /**
     * 修改政协会议活动
     *
     * @param zxConference 政协会议活动
     * @return 结果
     */
    public int updateZxConference(ZxConference zxConference);
    /**
     * 修改会议答复信息
     */
    public int updateReply(ZxConferenceUser zxConferenceUser);

    /**
     * 签到时间
     * @param zxConferenceUser
     * @return
     */
    public int signIn(ZxConferenceUser zxConferenceUser);

    /**
     * 删除政协会议活动
     *
     * @param conferenceId 政协会议活动主键
     * @return 结果
     */
    public int deleteZxConferenceByConferenceId(Long conferenceId);

    /**
     * 批量删除政协会议活动
     *
     * @param conferenceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteZxConferenceByConferenceIds(Long[] conferenceIds);

    /**
     * 批量删除政协会议活动-用户关联
     *
     * @param conferenceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteZxConferenceUserByConferenceIds(Long[] conferenceIds);

    /**
     * 批量新增政协会议活动-用户关联
     *
     * @param zxConferenceUserList 政协会议活动-用户关联列表
     * @return 结果
     */
    public int batchZxConferenceUser(List<ZxConferenceUser> zxConferenceUserList);


    /**
     * 通过政协会议活动主键删除政协会议活动-用户关联信息
     *
     * @param conferenceId 政协会议活动ID
     * @return 结果
     */
    public int deleteZxConferenceUserByConferenceId(Long conferenceId);
}
