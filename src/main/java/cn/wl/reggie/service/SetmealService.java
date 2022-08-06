package cn.wl.reggie.service;

import cn.wl.reggie.dato.SetmealDto;
import cn.wl.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDto
     */
    public void  saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐
     * @param ids
     */
    public  void  removeWithDish(List<Long> ids);

    //删除套餐，同时需要删除套餐和菜品的关联数据
//停售/启售套餐（单个/批量）
    public void status(Integer status, List<Long> ids);
    //根据 id 查询套餐信息
    public SetmealDto getById(Long id);
    //更新套餐信息，同时更新对应的菜品信息
    public void updateWithDish(SetmealDto setmealDto);

}
