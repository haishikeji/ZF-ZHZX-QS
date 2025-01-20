package cn.px.web.controller.bonus;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.system.domain.bonus.ZxBonus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.px.common.annotation.Log;
import cn.px.common.core.controller.BaseController;
import cn.px.common.core.domain.AjaxResult;
import cn.px.common.enums.BusinessType;
import cn.px.system.service.IZxBonusService;
import cn.px.common.core.page.TableDataInfo;

/**
 * 政协履职加分Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/zxBonus/bonus")
public class ZxBonusController extends BaseController
{
    @Autowired
    private IZxBonusService zxBonusService;

/**
 * 查询政协履职加分列表
 */
@PreAuthorize("@ss.hasPermi('zxBonus:bonus:list')")
@GetMapping("/list")
    public TableDataInfo list(ZxBonus zxBonus)
    {
        startPage();
        List<ZxBonus> list = zxBonusService.selectZxBonusList(zxBonus);
        return getDataTable(list);
    }

    /**
     * 导出政协履职加分列表
     */
    @PreAuthorize("@ss.hasPermi('zxBonus:bonus:export')")
    @Log(title = "政协履职加分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ZxBonus zxBonus)
    {
        List<ZxBonus> list = zxBonusService.selectZxBonusList(zxBonus);
        ExcelUtil<ZxBonus> util = new ExcelUtil<ZxBonus>(ZxBonus.class);
        util.exportExcel(response, list, "政协履职加分数据");
    }

    /**
     * 获取政协履职加分详细信息
     */
    @PreAuthorize("@ss.hasPermi('zxBonus:bonus:query')")
    @GetMapping(value = "/{bonusId}")
    public AjaxResult getInfo(@PathVariable("bonusId") Long bonusId)
    {
        return success(zxBonusService.selectZxBonusByBonusId(bonusId));
    }

    /**
     * 新增政协履职加分
     */
    @PreAuthorize("@ss.hasPermi('zxBonus:bonus:add')")
    @Log(title = "政协履职加分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ZxBonus zxBonus)
    {
        return toAjax(zxBonusService.insertZxBonus(zxBonus));
    }

    /**
     * 修改政协履职加分
     */
    @PreAuthorize("@ss.hasPermi('zxBonus:bonus:edit')")
    @Log(title = "政协履职加分", businessType = BusinessType.UPDATE)
    @PostMapping("/put")
    public AjaxResult edit(@RequestBody ZxBonus zxBonus)
    {
        return toAjax(zxBonusService.updateZxBonus(zxBonus));
    }

    /**
     * 删除政协履职加分
     */
    @PreAuthorize("@ss.hasPermi('zxBonus:bonus:remove')")
    @Log(title = "政协履职加分", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{bonusIds}")
    public AjaxResult remove(@PathVariable Long[] bonusIds)
    {
        return toAjax(zxBonusService.deleteZxBonusByBonusIds(bonusIds));
    }
}
