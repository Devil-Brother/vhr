package com.myc.hrmanager.service.emp;


import com.myc.hrmanager.mapper.JobLevelMapper;
import com.myc.hrmanager.model.JobLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author
 * @Description：职称处理
 */
@Service
public class JobLevelService {
    @Autowired
    JobLevelMapper jobLevelMapper;

    /**
     *获得所剖所有职称
     * @return
     */
    public List<JobLevel> getAllJobLevels() {
        return jobLevelMapper.getAllJobLevels();
    }

    /**
     * 添加职称
     * @param jobLevel
     * @return
     */
    public Integer addJobLevel(JobLevel jobLevel) {
//        添加时间
        jobLevel.setCreateDate(new Date());
//        是否启用
        jobLevel.setEnabled(true);
        return jobLevelMapper.insertSelective(jobLevel);
    }

    /**
     * 修改职称
     * @param jobLevel
     * @return
     */
    public Integer updateJobLevelById(JobLevel jobLevel) {
        return jobLevelMapper.updateByPrimaryKeySelective(jobLevel);
    }

    /**
     * 删除职称
     * @param id
     * @return
     */
    public Integer deleteJobLevelById(Integer id) {
        return jobLevelMapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除职称
     * @param ids
     * @return
     */
    public Integer deleteJobLevelsByIds(Integer[] ids) {
        return jobLevelMapper.deleteJobLevelsByIds(ids);
    }
}
