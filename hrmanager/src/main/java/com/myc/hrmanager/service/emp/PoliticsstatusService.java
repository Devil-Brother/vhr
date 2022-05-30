package com.myc.hrmanager.service.emp;


import com.myc.hrmanager.mapper.PoliticsstatusMapper;
import com.myc.hrmanager.model.Politicsstatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *政治状态
 */
@Service
public class PoliticsstatusService {
    @Autowired
    PoliticsstatusMapper politicsstatusMapper;

    /**
     * 获取所有政治状态
     * @return
     */
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusMapper.getAllPoliticsstatus();
    }
}
