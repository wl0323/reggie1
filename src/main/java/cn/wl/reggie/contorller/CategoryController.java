package cn.wl.reggie.contorller;


import cn.wl.reggie.common.R;
import cn.wl.reggie.entity.Category;
import cn.wl.reggie.service.CategoryService;
import cn.wl.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    /**
     *新增分类
     *
     *
     */
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public  R<String> save(@RequestBody Category category){
        log.info("category：{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }








    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo=new Page<>( page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByDesc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);


        return R.success(pageInfo);
    }

     @DeleteMapping
    public  R<String> delect(Long id){

        log.info("删除分类，id为：{}",id);
        //categoryService.removeById(id);
         categoryService.remove(id);
        return R.success("分类信息删除成功");
    }
    @PutMapping
    public  R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return  R.success("修改信息成功");

    }

    /**
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list (Category category){
        //条件构造器
      LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
      //添加条件
      queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

      //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
