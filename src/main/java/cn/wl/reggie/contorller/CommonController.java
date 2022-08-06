package cn.wl.reggie.contorller;

import cn.wl.reggie.common.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private  String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        

        
        //创建一个目录对象
        File dir=new File(basePath);
        if(!dir.exists()){
            dir.mkdir();
        }
        
        
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString();


        //将临时文件转存到指定位置
        file.transferTo(new File(basePath+fileName));
        return  R.success(fileName);
    }
    @GetMapping("/download")
    public  void  download(String name, HttpServletResponse response) {
        try {
            //通过输入流。通过输入流读取图片
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写到浏览器，在浏览器显示图片
            ServletOutputStream outputStream = response.getOutputStream();


            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
