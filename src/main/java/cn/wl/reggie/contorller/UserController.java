package cn.wl.reggie.contorller;

import cn.wl.reggie.common.R;
import cn.wl.reggie.entity.User;
import cn.wl.reggie.service.UserService;
import cn.wl.reggie.utis.SMSUtils;
import cn.wl.reggie.utis.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController

@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {

            //生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code=: {}", code);

            //调用阿里云提供的短息服务A皮完成发生短信
           // SMSUtils.sendMessage("瑞吉外卖"," ",phone,code);
            //需要将生成的验证码保存到Session
            //session.setAttribute(phone, code);
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

       log.info("打印"+map.toString());





        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

       // Object codeSession = session.getAttribute(phone);

        Object  codeSession  = redisTemplate.opsForValue().get(phone);
        if(codeSession!=null&&codeSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果用户登陆成功
            redisTemplate.delete(phone);
            return  R.success(user);
        }
        return R.error("短信发送失败");
    }


}
