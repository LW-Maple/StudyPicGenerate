import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pojo.ImageDTO;
import utils.FetchImgsUtil;
import utils.WaterMarkUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Maple
 * @Date 2022/9/4 15:45
 * @Version 1.0
 */
public class Main {

    private final static String stuUrl="http://news.cyol.com/gb/channels/vrGlAKDl/index.html";
    public static String local="";
    public static Map<String,Object> jsonMap=new HashMap<String, Object>();
    /**
     * 初始化操作
     * 1.获取运行时文件位置
     * 2.获取json数据
     * 3.创建图片文件夹
     */
    static {
        String jarDir = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file=new File(jarDir);
        String father=file.getParent();
        String path=father;
        local=path;
        getJsonData(local+"\\data.json");
        createFile();
    }

    public static void main(String[] args){
        downloadSrcImg();
        generateImgs();
        System.out.println("处理完成!");
    }

    /**
     * 获取json对象中的数据
     * @param infoPath json存储的位置
     * @return
     */
    public static void getJsonData(String infoPath){
        File file=new File(infoPath);
        String s = null;
        try {
            s = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            System.out.println("Json文件出错！");
        }
        jsonMap =  JSONObject.parseObject(s);
    }

    /**
     * 创建图片文件夹
     */
    public static void createFile(){
        File file=new File(local+"\\images");
        if (!file.exists()){
            file.mkdir();
        }
    }
    /**
     * 获取最新一期的大学习图片地址
     * @return url
     *
     */
    public  static String getLatestStuImg(){
        //设置请求头部信息
        Map<String,String> headers=new HashMap<String, String>();
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "PostmanRuntime/7.25.0");

        //1.请求页面
        Connection.Response response = null;
        try {
            response = Jsoup.connect(stuUrl)
                    .headers(headers)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2.转换为doc对象，选择器获取最新一期的url
        Document document = null;
        try {
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.获取完成页面图片的url
        Elements select = document.select("body > div.item > div > ul > li:nth-child(1) > a");
        String href = select.attr("href");
        String realUrl=href.replace("m.html","images/end.jpg");
        return realUrl;
    }

    /**
     * 下载原始图片到运行时位置
     */
    public static void downloadSrcImg(){
        String imgUrl = getLatestStuImg();
        FetchImgsUtil.downImages(local, imgUrl);
    }

    /**
     * 批量生成图片
     */
    public static void generateImgs(){
        String srcImgPath=local+"\\end.jpg";    //源图片地址
        String tarImgPathTmp=local+"\\images";   //目标图片的地址
        String classname=jsonMap.get("classname").toString();
        List<String> usernames=JSON.parseArray(jsonMap.get("usernames").toString(), String.class);

        ArrayList<ImageDTO> list = new ArrayList<ImageDTO>();
        list.add(WaterMarkUtils.createImageDTO(classname,new Color(255,59,48),new Font("微软雅黑", Font.PLAIN, 48), 335, 572));
        list.add(WaterMarkUtils.createImageDTO("",new Color(255,59,48),new Font("微软雅黑", Font.PLAIN, 48), 335, 620));
        //批量绘制图片
        for (int i = 0; i < usernames.size() ; i++) {
            list.get(1).setText(usernames.get(i));
            String realpath=tarImgPathTmp+"\\img"+i+".jpg";
            WaterMarkUtils.writeImage(srcImgPath, realpath, list);
        }
    }

}

