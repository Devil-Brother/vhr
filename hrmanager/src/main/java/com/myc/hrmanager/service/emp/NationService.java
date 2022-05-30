package com.myc.hrmanager.service.emp;


import com.myc.hrmanager.mapper.NationMapper;
import com.myc.hrmanager.model.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *民族
 */
@Service
public class NationService {
    @Autowired
    NationMapper nationMapper;

    /**
     * 获取所有民族
     * @return
     */
    public List<Nation> getAllNations() {
        return nationMapper.getAllNations();
    }
}
