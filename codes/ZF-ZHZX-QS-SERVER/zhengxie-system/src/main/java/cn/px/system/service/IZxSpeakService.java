package cn.px.system.service;


import cn.px.system.domain.speak.ZxSpeak;

import java.util.List;

/**
 * 政协发言Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IZxSpeakService
{
    /**
     * 查询政协发言
     *
     * @param speakId 政协发言主键
     * @return 政协发言
     */
    public ZxSpeak selectZxSpeakBySpeakId(Long speakId);

    /**
     * 查询政协发言列表
     *
     * @param zxSpeak 政协发言
     * @return 政协发言集合
     */
    public List<ZxSpeak> selectZxSpeakList(ZxSpeak zxSpeak);

    /**
     * 新增政协发言
     *
     * @param zxSpeak 政协发言
     * @return 结果
     */
    public int insertZxSpeak(ZxSpeak zxSpeak);

    /**
     * 修改政协发言
     *
     * @param zxSpeak 政协发言
     * @return 结果
     */
    public int updateZxSpeak(ZxSpeak zxSpeak);

    /**
     * 批量删除政协发言
     *
     * @param speakIds 需要删除的政协发言主键集合
     * @return 结果
     */
    public int deleteZxSpeakBySpeakIds(Long[] speakIds);

    /**
     * 删除政协发言信息
     *
     * @param speakId 政协发言主键
     * @return 结果
     */
    public int deleteZxSpeakBySpeakId(Long speakId);
}
