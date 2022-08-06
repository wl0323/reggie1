package cn.wl.reggie.service.impl;

import cn.wl.reggie.entity.ShoppingCart;
import cn.wl.reggie.mapper.ShoppingCartMapper;
import cn.wl.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
