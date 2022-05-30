package com.myc.hrmanager.service.emp;


import com.myc.hrmanager.mapper.DepartmentMapper;
import com.myc.hrmanager.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author
 * @Description：部门处理
 */
@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    /**
     * 获取所有部门
     * @return
     */
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartmentsByParentId(-1);
    }

    /**
     * 添加部门
     * @param dep
     */
    public void addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
    }

    /**
     * 删除部门
     * @param dep
     */
    public void deleteDepById(Department dep) {
        departmentMapper.deleteDepById(dep);
    }

    /**
     * 获取所有部门的子部门
     * @return
     */
    public List<Department> getAllDepartmentsWithOutChildren() {
        return departmentMapper.getAllDepartmentsWithOutChildren();
    }
}
