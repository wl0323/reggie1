package cn.wl.reggie.service.impl;

import cn.wl.reggie.entity.Employee;
import cn.wl.reggie.mapper.EmployeeMapper;
import cn.wl.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {


}
