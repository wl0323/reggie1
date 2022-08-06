package cn.wl.reggie.dato;

import cn.wl.reggie.entity.Setmeal;
import cn.wl.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
