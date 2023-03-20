package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.pdm.mapper.BandMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 卞康
 * @date 2023/3/18 17:54:12
 */
@RestController
public class BandController extends BaseController {
    @Resource
    private BandMapper bandMapper;
    @GetMapping("/listQuery")
    public void listQuery(){
        bandMapper.selectList(null);
    }
}
