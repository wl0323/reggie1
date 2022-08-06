package cn.wl.reggie.service.impl;

import cn.wl.reggie.common.CustomException;
import cn.wl.reggie.dato.DishDto;
import cn.wl.reggie.entity.Dish;
import cn.wl.reggie.entity.DishFlavor;
import cn.wl.reggie.mapper.DishMapper;
import cn.wl.reggie.service.DishFlavorService;
import cn.wl.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DishServiceImpl  extends ServiceImpl<DishMapper, Dish>  implements DishService {
   @Autowired
   private DishFlavorService dishFlavorService;

    @Override
@Transactional
    public void savaWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return  item;

        }).collect(Collectors.toList());


        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
      //1.查询菜品的基本信息，从dish表查询
           Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //2.查询当前菜品口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor>  queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return  dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据--dish——flavor表的delect操作
      LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
      queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());


        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return  item;

        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void RemoveWithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Dish> dishqueryWrapper = new LambdaQueryWrapper<>();
        dishqueryWrapper.in(Dish::getId,ids);
        dishqueryWrapper.eq(Dish::getStatus,1); //查询是否在起售状态
        int count = this.count(dishqueryWrapper);

        if(count > 0 ){
            //如果不能删除，抛出业务异常
            throw new CustomException("删除的菜品中有正在售卖的，无法删除");
        }
        //如果可以删除套餐，执行删除操作，删除菜品表中的信息
        this.removeByIds(ids);
        //删除菜品口味表关联信息  执行SQL语句：delete from dish_flavor where id in (1,2,3)
        LambdaQueryWrapper<DishFlavor> dishflavorqueryWrapper = new LambdaQueryWrapper<>();
        dishflavorqueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishflavorqueryWrapper);


    }
}
