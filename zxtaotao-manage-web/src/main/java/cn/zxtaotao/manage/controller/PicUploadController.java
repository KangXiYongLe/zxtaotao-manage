package cn.zxtaotao.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.zxtaotao.common.bean.PicUploadResult;
import cn.zxtaotao.manage.service.ItemService;
import cn.zxtaotao.manage.service.PropertieService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("pic")
public class PicUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicUploadController.class);

    @Autowired
    private PropertieService propertieService;
    
    @Autowired
    private ItemService itemService;

    //用于将对象序列化成json
    private static final ObjectMapper mapper = new ObjectMapper();

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletResponse response)
            throws Exception {

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        // 封装Result对象，并且将文件的byte数组放置到result对象中
        PicUploadResult fileUploadResult = new PicUploadResult();

        // 状态
        fileUploadResult.setError(isLegal ? 0 : 1);

        // 文件新路径
        String filePath = getFilePath(uploadFile.getOriginalFilename());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
        }

        // 生成图片的绝对引用地址
        String picUrl = StringUtils.replace(
                StringUtils.substringAfter(filePath, propertieService.REPOSITORY_PATH), "\\", "/");
        fileUploadResult.setUrl(propertieService.IMAGE_BASE_URL + picUrl);

        File newFile = new File(filePath);

        // 写文件到磁盘
        uploadFile.transferTo(newFile);

        // 校验图片是否合法
        isLegal = false;
        try {
            BufferedImage image = ImageIO.read(newFile);
            if (image != null) {
                fileUploadResult.setWidth(image.getWidth() + "");
                fileUploadResult.setHeight(image.getHeight() + "");
                isLegal = true;
            }
        } catch (IOException e) {
        }

        // 状态
        fileUploadResult.setError(isLegal ? 0 : 1);

        if (!isLegal) {
            // 不合法，将磁盘上的文件删除
            newFile.delete();
        }

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        return mapper.writeValueAsString(fileUploadResult);
    }

    private String getFilePath(String sourceFileName) {
        String baseFolder = propertieService.REPOSITORY_PATH + File.separator + "images";
        Date nowDate = new Date();
        // yyyy/MM/dd
        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy")
                + File.separator + new DateTime(nowDate).toString("MM") + File.separator
                + new DateTime(nowDate).toString("dd");
        File file = new File(fileFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目录
            file.mkdirs();
        }
        // 生成新的文件名
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
                + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(sourceFileName, ".");
        return fileFolder + File.separator + fileName;
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    public ResponseEntity<Void> deletePic(@RequestParam(value="id")Long id){
        try {
            //先查询出数据，然后把图片字段设为null,然后更新            
            Boolean b = this.itemService.delePic(id);            
            if(b){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            LOGGER.error("删除图片失败：cn.zxtaotao.manage.controller.PicUploadController.deletePic(id = {})", id,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        
    }
}
