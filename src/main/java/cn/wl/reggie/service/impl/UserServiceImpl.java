package cn.wl.reggie.service.impl;

import cn.wl.reggie.entity.User;
import cn.wl.reggie.mapper.UserMapper;
import cn.wl.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
