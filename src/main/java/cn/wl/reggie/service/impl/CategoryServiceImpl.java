package cn.wl.reggie.service.impl;

import cn.wl.reggie.common.CustomException;
import cn.wl.reggie.entity.Category;
import cn.wl.reggie.entity.Dish;
import cn.wl.reggie.entity.Setmeal;
import cn.wl.reggie.mapper.CategoryMapper;
import cn.wl.reggie.service.CategoryService;
import cn.wl.reggie.service.DishService;
import cn.wl.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
   private DishService dishService;
   @Autowired
   private SetmealService setmealService;

    /**
     *
     * @param id
     */


    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果关联，抛出一个业务异常
       if(count1>0){
           //已经关联菜品，抛出一个业务异常
           throw  new CustomException("当前分类下关联了菜品，不能删除");
       }

        //查询当前分类是否关联了套餐，，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
       setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            //已经关联套餐，抛出一个业务异常
            throw  new CustomException("当前分类下关联了套餐，不能删除");
        }



        //正常删除分类
         super.removeById(id);
    }
}
