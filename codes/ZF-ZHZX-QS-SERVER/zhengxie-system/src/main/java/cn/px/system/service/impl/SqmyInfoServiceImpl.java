package cn.px.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.px.common.core.domain.AjaxResult;
import cn.px.common.core.domain.entity.SysRole;
import cn.px.common.core.domain.entity.SysUser;
import cn.px.common.utils.DateUtils;
import cn.px.common.utils.SecurityUtils;
import cn.px.common.utils.StringUtils;
import cn.px.system.domain.ZxFj;
import cn.px.system.domain.sqmy.SqmyInfo;
import cn.px.system.domain.sqmy.SqmyUnitReply;
import cn.px.system.mapper.SqmyUnitReplyMapper;
import cn.px.system.mapper.ZxFjMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.system.mapper.SqmyInfoMapper;
import cn.px.system.service.ISqmyInfoService;

import static cn.px.common.constant.CommonConstants.*;

/**
 * 社情民意信息Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class SqmyInfoServiceImpl implements ISqmyInfoService
{
    @Autowired
    private SqmyInfoMapper sqmyInfoMapper;
    @Autowired
    private ZxFjMapper zxFjMapper;
    @Autowired
    private SqmyUnitReplyMapper sqmyUnitReplyMapper;

    /**
     * 查询社情民意信息
     *
     * @param sqmyId 社情民意信息主键
     * @return 社情民意信息
     */
    @Override
    public SqmyInfo selectSqmyInfoBySqmyId(Long sqmyId)
    {

        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<SysRole> rols = user.getRoles();
        boolean bl = false;
        for (SysRole rol : rols) {
            if("sqmy".equals(rol.getRoleKey()) || "gly".equals(rol.getRoleKey()) ){
                bl = true;
                break;
            }
        }
        if(bl){
            SqmyInfo info = new SqmyInfo();
            info.setIsRead("0");
            info.setSqmyId(sqmyId);
            sqmyInfoMapper.updateSqmyInfo(info);
        }

        SqmyInfo sqmyInfo = sqmyInfoMapper.selectSqmyInfoBySqmyId(sqmyId);
        //查询附件
        ZxFj zxFj = new ZxFj();
        zxFj.setMainId(sqmyId);
        zxFj.setType("2");
        List<ZxFj> zxFjList = zxFjMapper.selectZxFjList(zxFj);
        List<ZxFj> fjList = new ArrayList<>();
        List<ZxFj> dfwyFjList = new ArrayList<>();
        if(zxFjList!=null && zxFjList.size()>0){
            for (ZxFj fj : zxFjList) {
                if("4".equals(fj.getStytle())){
                    fjList.add(fj);
                }
                if("6".equals(fj.getStytle())){
                    dfwyFjList.add(fj);
                }
            }
            sqmyInfo.setZxFjList(fjList);
            sqmyInfo.setDfFjList(dfwyFjList);
        }
        //答复附件

        //查询答复
        /*SqmyUnitReply sqmyUnitReply = new SqmyUnitReply();
        sqmyUnitReply.setSqmyId(sqmyId);
        List<SqmyUnitReply> sqmyUnitReplyList = sqmyUnitReplyMapper.selectSqmyUnitReplyList(sqmyUnitReply);
        if(sqmyUnitReplyList!=null && sqmyUnitReplyList.size()>0){
            for (SqmyUnitReply unitReply : sqmyUnitReplyList) {
                List<ZxFj> dfFjList = new ArrayList<>();
                //查询答复附件
                if(zxFjList!=null && zxFjList.size()>0){
                    for (ZxFj fj : zxFjList) {
                        if(fj.getStytle().equals(unitReply.getType())){
                            dfFjList.add(fj);
                        }
                    }
                }
                unitReply.setZxFjList(dfFjList);
            }
            sqmyInfo.setSqmyUnitReplyList(sqmyUnitReplyList);
        }*/

        return sqmyInfo;
    }

    /**
     * 查询社情民意信息列表
     *
     * @param sqmyInfo 社情民意信息
     * @return 社情民意信息
     */
    @Override
    public List<SqmyInfo> selectSqmyInfoList(SqmyInfo sqmyInfo)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();

        for (SysRole role : roles) {
            if("jbdw".equals(role.getRoleKey())){
                sqmyInfo.setIsRecord("0");
            }
        }
        return sqmyInfoMapper.selectSqmyInfoList(sqmyInfo);
    }

    /**
     * 列表页统计
     */
    @Override
    public AjaxResult statistics(SqmyInfo sqmyInfo) {
        Map<String,Object> map = new HashMap<>();
        List<SqmyInfo> sqmyInfos = sqmyInfoMapper.selectSqmyInfoList(sqmyInfo);
        int totle = sqmyInfos.size();
        //已读
        int yd = 0;
        //未读
        int wd = 0;
        //已采用
        int ycy = 0;
        //未采用
        int wcy = 0;
        for (SqmyInfo info : sqmyInfos) {
            if(StringUtils.isNotEmpty(info.getIsRead()) && "0".equals(info.getIsRead())){
                yd++;
            }
            if(StringUtils.isNotEmpty(info.getIsRead()) && "1".equals(info.getIsRead())){
                wd++;
            }
            if("2".equals(info.getSqmyProgress())) {
                ycy++;
            }
            if(!"3".equals(info.getSqmyProgress())) {
                wcy++;
            }
        }
        map.put("totle",totle);


        map.put("yd",yd);
        map.put("wd",wd);
        map.put("ycy",ycy);
        map.put("wcy",wcy);

        return AjaxResult.success(map);
    }

    @Override
    public List<SqmyInfo> sqmycbList(SqmyInfo sqmyInfo) {
        return sqmyInfoMapper.sqmycbList(sqmyInfo);
    }

    @Override
    public int cbdwdfwy(SqmyInfo sqmyInfo) {
        if(sqmyInfo.getZxFjList()!=null && sqmyInfo.getZxFjList().size()>0){
            for (ZxFj zxFj : sqmyInfo.getZxFjList()) {
                zxFj.setMainId(sqmyInfo.getSqmyId());
                zxFj.setSourceId(sqmyInfo.getSqmyId());
                zxFj.setType("2");
                zxFj.setStytle("5");
                zxFjMapper.insertZxFj(zxFj);
            }
        }
        sqmyInfo.setSqmyProgress("8");
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        return sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
    }

    /**
     * 新增社情民意信息
     *
     * @param sqmyInfo 社情民意信息
     * @return 结果
     */
    @Override
    public int insertSqmyInfo(SqmyInfo sqmyInfo)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        sqmyInfo.setSqmyUserId(user.getUserId());
        sqmyInfo.setSqmyProgress("1");
        sqmyInfo.setCreateTime(DateUtils.getNowDate());
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.insertSqmyInfo(sqmyInfo);
        if(sqmyInfo.getZxFjList()!=null && sqmyInfo.getZxFjList().size()>0){
            for (ZxFj zxFj : sqmyInfo.getZxFjList()) {
                zxFj.setMainId(sqmyInfo.getSqmyId());
                zxFj.setSourceId(sqmyInfo.getSqmyId());
                zxFj.setType("2");
                zxFj.setStytle("4");
                zxFjMapper.insertZxFj(zxFj);
            }
        }

        return i;
    }

    /**
     * 修改社情民意信息
     *
     * @param sqmyInfo 社情民意信息
     * @return 结果
     */
    @Override
    public int updateSqmyInfo(SqmyInfo sqmyInfo)
    {
        ZxFj fj = new ZxFj();
        fj.setSourceId(sqmyInfo.getSqmyId());
        fj.setMainId(sqmyInfo.getSqmyId());
        fj.setType(TWO);
        fj.setStytle(FOR);
        //删除附件
        zxFjMapper.deleteZxFj(fj);
        if(sqmyInfo.getZxFjList()!=null && sqmyInfo.getZxFjList().size()>0){
            for (ZxFj zxFj : sqmyInfo.getZxFjList()) {
                zxFj.setMainId(sqmyInfo.getSqmyId());
                zxFj.setSourceId(sqmyInfo.getSqmyId());
                zxFj.setType("2");
                zxFj.setStytle("4");
                zxFjMapper.insertZxFj(zxFj);
            }
        }
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        return sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
    }

    /**
     * 批量删除社情民意信息
     *
     * @param sqmyIds 需要删除的社情民意信息主键
     * @return 结果
     */
    @Override
    public int deleteSqmyInfoBySqmyIds(Long[] sqmyIds)
    {
        return sqmyInfoMapper.deleteSqmyInfoBySqmyIds(sqmyIds);
    }

    /**
     * 删除社情民意信息信息
     *
     * @param sqmyId 社情民意信息主键
     * @return 结果
     */
    @Override
    public int deleteSqmyInfoBySqmyId(Long sqmyId)
    {
        return sqmyInfoMapper.deleteSqmyInfoBySqmyId(sqmyId);
    }

    /***
     * 是否立案
     */
    @Override
    public AjaxResult isRecord(SqmyInfo sqmyInfo) {
        //不立案，指定答复单位
        sqmyInfo.setIsRead("0");
        if("1".equals(sqmyInfo.getIsRecord())){
            sqmyInfo.setSqmyProgress("3");
            /*List<SqmyUnitReply> sqmyUnitReplyList = sqmyInfo.getSqmyUnitReplyList();
            for (SqmyUnitReply sqmyUnitReply : sqmyUnitReplyList) {
                sqmyUnitReply.setSqmyId(sqmyInfo.getSqmyId());
                sqmyUnitReply.setType("1");
                sqmyUnitReply.setStartTime(DateUtils.getNowDate());
                sqmyUnitReplyMapper.insertSqmyUnitReply(sqmyUnitReply);
            }*/
        }else{
            sqmyInfo.setSqmyProgress("2");
        }
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 交办
     */
    @Override
    public AjaxResult assign(SqmyInfo sqmyInfo) {
        List<SqmyUnitReply> sqmyUnitReplyList = sqmyInfo.getSqmyUnitReplyList();
        SqmyUnitReply zbUnitReply = sqmyInfo.getZbUnitReply();
        if(sqmyUnitReplyList!=null && sqmyUnitReplyList.size()>0){
            for (SqmyUnitReply sqmyUnitReply : sqmyUnitReplyList) {
                sqmyUnitReply.setSqmyId(sqmyInfo.getSqmyId());
                sqmyUnitReply.setStartTime(DateUtils.getNowDate());
                sqmyUnitReplyMapper.insertSqmyUnitReply(sqmyUnitReply);
            }
        }
        zbUnitReply.setSqmyId(sqmyInfo.getSqmyId());
        zbUnitReply.setStartTime(DateUtils.getNowDate());
        sqmyUnitReplyMapper.insertSqmyUnitReply(zbUnitReply);

        sqmyInfo.setSqmyProgress("4");
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 委员意见
     */
    @Override
    public AjaxResult idea(SqmyInfo sqmyInfo) {
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 政协意见
     */
    @Override
    public AjaxResult zxIdea(SqmyInfo sqmyInfo) {
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        ZxFj fj = new ZxFj();
        fj.setSourceId(sqmyInfo.getSqmyId());
        fj.setMainId(sqmyInfo.getSqmyId());
        fj.setType(TWO);
        fj.setStytle(SIX);
        //删除附件
        zxFjMapper.deleteZxFj(fj);

        if(sqmyInfo.getZxFjList()!=null && sqmyInfo.getZxFjList().size()>0){
            for (ZxFj zxFj : sqmyInfo.getZxFjList()) {
                zxFj.setMainId(sqmyInfo.getSqmyId());
                zxFj.setSourceId(sqmyInfo.getSqmyId());
                zxFj.setType("2");
                zxFj.setStytle("6");
                zxFjMapper.insertZxFj(zxFj);
            }
        }
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 推荐重点
     */
    @Override
    public AjaxResult keyPoint(SqmyInfo sqmyInfo) {
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 推荐优秀
     */
    @Override
    public AjaxResult outstanding(SqmyInfo sqmyInfo) {
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /***
     * 交办审核
     */
    @Override
    public AjaxResult assignsh(SqmyInfo sqmyInfo) {
        sqmyInfo.setUpdateTime(DateUtils.getNowDate());
        int i = sqmyInfoMapper.updateSqmyInfo(sqmyInfo);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    @Override
    public List<SqmyInfo> unitList(SqmyInfo sqmyInfo) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if(!"admin".equals(user.getUserName())){
            sqmyInfo.setDeptId(user.getDeptId());
        }
        return sqmyInfoMapper.selectSqmyInfoListByUnit(sqmyInfo);
    }

}
