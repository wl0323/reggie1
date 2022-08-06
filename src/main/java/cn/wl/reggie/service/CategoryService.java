package cn.wl.reggie.service;

import cn.wl.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    public  void  remove(Long id);
}
