package cn.wl.reggie.service.impl;

import cn.wl.reggie.entity.AddressBook;
import cn.wl.reggie.mapper.AddressBookMapper;
import cn.wl.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
