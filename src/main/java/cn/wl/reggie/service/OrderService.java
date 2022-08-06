package cn.wl.reggie.service;

import cn.wl.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {
 public  void submit(Orders order);

}
