package pojo;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * 存放文本内容的类
 */
@Setter
@Getter
public class ImageDTO {
    //文字内容
    private String text;
    //字体颜色和透明度
    private Color color;
    //字体和大小
    private Font font;
    //所在图片的x坐标
    private int x;
    //所在图片的y坐标
    private int y;
}
