package cn.wl.reggie.contorller;

import cn.wl.reggie.common.R;
import cn.wl.reggie.entity.Orders;
import cn.wl.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
public R<String> submit(@RequestBody Orders orders){
  log.info("订单数据：{}",orders);
   orderService.submit(orders);
  return R.success("下单成功");
}
}
