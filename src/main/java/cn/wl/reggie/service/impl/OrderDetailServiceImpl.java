package cn.wl.reggie.service.impl;

import cn.wl.reggie.entity.OrderDetail;
import cn.wl.reggie.mapper.OrderDetailMapper;
import cn.wl.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>  implements OrderDetailService {
}
