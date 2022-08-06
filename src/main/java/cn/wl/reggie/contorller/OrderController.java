package cn.wl.reggie.contorller;

import cn.wl.reggie.common.R;
import cn.wl.reggie.entity.Orders;
import cn.wl.reggie.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    /**
     * 后台查询订单明细
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
//
    /**
     * 后台管理订单信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime){
        log.info("page = {},pageSize = {}",page,pageSize);

        //构造分页构造器
        Page<Orders> pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(number != null,Orders::getId, number);
        queryWrapper.ge(beginTime != null,Orders::getOrderTime,beginTime);
        queryWrapper.le(endTime != null,Orders::getOrderTime,endTime);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        //执行查询
        orderService.page(pageInfo,queryWrapper);

        List<Orders> records = pageInfo.getRecords();
        records = records.stream().map((item) -> {

            item.setUserName("用户" + item.getUserId());

            return item;
        }).collect(Collectors.toList());

        return R.success(pageInfo);
    }

    /**
     * 派送订单
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateOrder(@RequestBody Orders orders){
        //构造条件构造器
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤条件
        updateWrapper.eq(Orders::getId, orders.getId());
        updateWrapper.set(Orders::getStatus,orders.getStatus());
        orderService.update(updateWrapper);

        return R.success("订单派送成功");
    }

}
