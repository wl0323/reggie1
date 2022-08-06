package cn.wl.reggie.contorller;

import cn.wl.reggie.common.R;
import cn.wl.reggie.dato.DishDto;
import cn.wl.reggie.entity.Category;
import cn.wl.reggie.entity.Dish;
import cn.wl.reggie.entity.DishFlavor;
import cn.wl.reggie.service.CategoryService;
import cn.wl.reggie.service.DishFlavorService;
import cn.wl.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
     private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;
  @PostMapping
    public R<String> sava( @RequestBody DishDto dishDto){
      log.info(dishDto.toString());


dishService.savaWithFlavor(dishDto);
      return R.success("新增菜品成功");
  }
  @GetMapping("/page")
    public  R<Page> page(int page,int pageSize,String name){
      Page<Dish> pageinfo=new Page<>(page,pageSize);
      Page<DishDto> dishDtoPage=new Page<>();
      //条件构造器
      LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
      //过滤条件
      queryWrapper.like(name!=null,Dish::getName,name);
      //排序条件
      queryWrapper.orderByDesc(Dish::getUpdateTime);

      dishService.page(pageinfo,queryWrapper);


      //对象拷贝
      BeanUtils.copyProperties(pageinfo,dishDtoPage,"records");


      List<Dish> records = pageinfo.getRecords();

      List<DishDto> list = records.stream().map((item) -> {
          DishDto dishDto = new DishDto();
          BeanUtils.copyProperties(item, dishDto);
          Long categoryId = item.getCategoryId();
          Category category = categoryService.getById(categoryId);
           if(category!=null){
               String categoryName = category.getName();
               dishDto.setCategoryName(categoryName);
           }
           return dishDto;
      }).collect(Collectors.toList());

      dishDtoPage.setRecords(list);

      return R.success(dishDtoPage);
    }
    @PostMapping("/status/{status}")
    public  R<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        log.info("status:{}",status);
        log.info("ids:{}",ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null,Dish::getId,ids);
        List<Dish> list = dishService.list(queryWrapper);
        for (Dish dish : list) {
            if(dish != null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("售卖状态修改成功");
    }
 @DeleteMapping
 public R<String> remove(@RequestParam List<Long> ids){
      log.info("ids:{}",ids);
      dishService.RemoveWithFlavor(ids);
      return R.success("删除菜品成功");
 }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get( @PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return  R.success(dishDto);
    }
    @PutMapping
    public R<String> update( @RequestBody DishDto dishDto){
        log.info(dishDto.toString());


        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public  R<List<Dish>> list (Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        //排序条件;
//        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return  R.success(list);
//
//    }

    @GetMapping("/list")
    public  R<List<DishDto>> list (Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        //排序条件;
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList =list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());



        return  R.success(dishDtoList);

    }

}
