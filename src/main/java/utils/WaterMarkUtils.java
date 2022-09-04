package utils;

import lombok.extern.slf4j.Slf4j;
import pojo.ImageDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @use 利用Java代码给图片添加文字(透明图调低点,也可以当做水印)
 */
@Slf4j
public class WaterMarkUtils {
    /**
     * 编辑图片,往指定位置添加文字
     * @param srcImgPath    :源图片路径
     * @param targetImgPath :保存图片路径
     * @param list          :文字集合
     */
    public static void writeImage(String srcImgPath, String targetImgPath, List<ImageDTO> list) {
        FileOutputStream outImgStream = null;
        try {
            //读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
 
            //添加文字:
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            for (ImageDTO imgDTO : list) {
                g.setColor(imgDTO.getColor());                                  //根据图片的背景设置水印颜色
                g.setFont(imgDTO.getFont());                                    //设置字体
                g.drawString(imgDTO.getText(), imgDTO.getX(), imgDTO.getY());   //画出水印
            }
            g.dispose();
 
            // 输出图片
            outImgStream = new FileOutputStream(targetImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
        } catch (Exception e) {
            log.error("==== 系统异常::{} ====",e);
        }finally {
            try {
                if (null != outImgStream){
                    outImgStream.flush();
                    outImgStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    /**
     * 创建ImageDTO, 每一个对象,代表在该图片中要插入的一段文字内容:
     * @param text  : 文本内容;
     * @param color : 字体颜色(前三位)和透明度(第4位,值越小,越透明);
     * @param font  : 字体的样式和字体大小;
     * @param x     : 当前字体在该图片位置的横坐标;
     * @param y     : 当前字体在该图片位置的纵坐标;
     * @return
     */
    public static ImageDTO createImageDTO(String text, Color color, Font font, int x, int y){
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setText(text);
        imageDTO.setColor(color);
        imageDTO.setFont(font);
        imageDTO.setX(x);
        imageDTO.setY(y);
        return imageDTO;
    }
}
 
 
