# 青年大学习截图批量生成
⚠️该项目用于**本地**批量生成当期青年大学习图片

## 使用说明

1. 需要java运行环境，自行安装

2. [下载](https://github.com/LW-Maple/StudyPicGenerate/releases/download/v1/target.zip)zip文件并解压

3. 修改data.json

   ```json
   {
       "classname":"软件201",  //班级
       "usernames":[		   //姓名，自行添加
           "张三",
           "李四",
           "王五"
       ]
   }
   ```

4. 在当前文件夹运行cmd

   ![](https://files.catbox.moe/wetkaq.png)

5. 在命令行输入

   ```bash
   java -jar StudyPicsGenerate.jar
   ```

6. 生成的图片将放在images文件夹中

