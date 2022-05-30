package com.myc.hrmanager.service.system;

import com.myc.hrmanager.mapper.PositionMapper;
import com.myc.hrmanager.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author 布卡布卡飞
 * @Description 职位处理
 */
@Service
public class PositionService {

    @Autowired
    PositionMapper positionMapper;

    /**
     * 获取所有职位
     * @return
     */
    public List<Position> getAllPositions() {
        return positionMapper.getAllPositions();
    }

    /**
     * 添加职位
     * @param position
     * @return
     */
    public Integer addPosition(Position position) {
//        enable表示是否启用该职位
        position.setEnabled(true);
//        添加该职位的时间
        position.setCreateDate(new Date());
        return positionMapper.insertSelective(position);
    }

    /**
     * 职位修改
     * @param position
     * @return
     */
    public Integer updatePositions(Position position) {
        return positionMapper.updateByPrimaryKeySelective(position);
    }

    /**
     * 删除职位
     * @param id
     * @return
     */
    public Integer deletePositionById(Integer id) {
        return positionMapper.deleteByPrimaryKey(id);
    }
}