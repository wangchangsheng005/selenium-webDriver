package com.education.webdriver.utils;

import com.education.webdriver.entity.UserInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @ClassName:SeleniumWebDriver
 * @Description:TODO
 * @Author:SH-WANGCS2
 * @Date:2020/5/11/0011 19:40
 * @Version 1.0
 **/
public class SeleniumWebDriver {

    private static final String newPassword = "Shkfdx@963852";

    private static final String newPasswordPath = "http://ids.shou.org.cn/authserver/passwordChange.do";

    private static final String forwardingPath = "http://myportal.shou.org.cn/_web/fusionportal/xIndex.jsp?_p=YXM9MSZwPTEmbT1OJg__";


    public static void main(String[] args) throws Exception {
        File file = ResourceUtils.getFile("classpath:chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getPath());
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        String url = "http://ids.shou.org.cn/authserver/login";

        List<UserInfo> userInfoList = new ArrayList<>();
        readSaveList(userInfoList);
        for (UserInfo userInfo:userInfoList) {
            driver.get(url);
            driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(userInfo.getUserId());
            driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(userInfo.getPassword());
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"signIn\"]")).click();

            updateNewPassword(driver,userInfo);
            Thread.sleep(1000);
            //我的作业
            driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div[2]/div/div[2]/div[1]/div[2]/a")).click();
            Thread.sleep(2000);
            switchToWindowByTitle(driver," - 学生空间 - 上海开放大学");
            System.out.println(driver.getCurrentUrl());
            Thread.sleep(2000);
            //判断课程是否存在
            driver.findElement(By.xpath("//*[@id=\"rightBox\"]/div[6]/table/tbody/tr[4]/td[1]/div/div/div/a")).click();
            Thread.sleep(2000);
            switchToWindowByTitle(driver,"上海开放大学");
            System.out.println(driver.getCurrentUrl());
            //切换IFRNAME
            WebElement webElement = driver.findElement(By.xpath("//*[@id=\"frame_content\"]"));
            driver.switchTo().frame(webElement);
            Thread.sleep(1000);
            driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/ul/li[1]/div[1]")).click();
            Thread.sleep(1000);
            switchToWindowByTitle(driver,"学习进度页面");
            System.out.println(driver.getCurrentUrl());
            Thread.sleep(1000);
            //点击课程
            driver.findElement(By.xpath("/html/body/div[5]/div[1]/div[2]/div[3]/div[1]/div[1]/h3/span[3]/a")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"dct2\"]")).click();
            Thread.sleep(1000);
            WebElement webElement1 = driver.findElement(By.xpath("//*[@id=\"iframe\"]"));
            driver.switchTo().frame(webElement1);
            Thread.sleep(1000);
            WebElement webElement2 = driver.findElement(By.xpath("//*[@id=\"ext-gen1038\"]/div/div/p/div/iframe"));
            driver.switchTo().frame(webElement2);
            Thread.sleep(1000);
            WebElement webElement3 = driver.findElement(By.xpath("//*[@id=\"frame_content\"]"));
            driver.switchTo().frame(webElement3);
            Thread.sleep(1000);

            //模拟答案
            Map<String,String> map = new HashMap<>();
            answerMap(map);
            Map<String,String> anwersMap = new HashMap<>();
            anwersMap.put("A","1");
            anwersMap.put("B","2");
            anwersMap.put("C","3");
            anwersMap.put("D","4");
            anwersMap.put("E","5");
            anwersMap.put("F","6");
            anwersMap.put("G","7");
            anwersMap.put("true","1");
            anwersMap.put("false","2");

            boolean flag = true;

            int i = 0;
            while(flag){
                //标题
                StringBuilder sBuilderCheck = new StringBuilder();
                sBuilderCheck.append("//*[@id=\"ZyBottom\"]/div");
                if(i != 0){
                    for (int j = 0; j < i; j++) {
                        sBuilderCheck.append("/div[4]");
                    }
                }
                sBuilderCheck.append("/div[1]/div");
                String xpathCheck = String.valueOf(sBuilderCheck);
                if(check(driver,By.xpath(xpathCheck))){
                    String text = driver.findElement(By.xpath(xpathCheck)).getText();
                    text = dataDeal(text);
                    System.out.println(text);
                    //答案
                    String anwers =  map.get(text);
                    if(StringUtils.hasText(anwers)){
                        String[] array = map.get(text).split(",");
                        StringBuilder sBuilderAnswer = new StringBuilder();
                        sBuilderAnswer.append("//*[@id=\"ZyBottom\"]/div");
                        if(i != 0){
                            for (int j = 0; j < i; j++) {
                                sBuilderAnswer.append("/div[4]");
                            }
                        }//*[@id="ZyBottom"]/div/div[2]/ul/li[2]/label/input
                        for (String anwer : array) {
                            String redios = "//*[@id=\"ZyBottom\"]/div/div[2]/ul/li[" +anwersMap.get(anwer)+ "]/label/input";
                            driver.findElement(By.xpath(redios)).click();
                        }
                    }

                }else{
                    flag = false;
                }
                i++;
            }
            //driver.close();
        }

    }

    public static Boolean check(WebDriver driver,By seletor) {
        try {
            driver.findElement(seletor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String dataDeal(String text){
        String newText = "";
        newText = text.replaceAll("\\(","");
        newText = newText.replaceAll("\\)","");
        newText = newText.replaceAll("【单选题】","");
        newText = newText.replaceAll("【多选题】","");
        newText = newText.replaceAll("【判断题】","");
        newText = newText.replaceAll(" ","");
        newText = newText.replaceAll("  ","");
        newText = newText.replaceAll("    ","");
        newText = newText.replaceAll("\t","");
        newText = newText.replaceAll("\n","");
        newText = newText.replaceAll("“","");
        newText = newText.replaceAll("”","");
        newText = newText.replaceAll("\"","");
        if(text.endsWith(",")){
            newText = newText.substring(0,newText.length() -1);
        }
        if(text.endsWith("。")){
            newText = newText.substring(0,newText.length() -1);
        }
        if(text.endsWith("!")){
            newText = newText.substring(0,newText.length() -1);
        }
        if(text.endsWith(";")){
            newText = newText.substring(0,newText.length() -1);
        }
        return newText;
    }

    private static void switchWindowByHandle(WebDriver dr, String currentHandle, Set<String> handles) {
        Iterator<String> it = handles.iterator();
        while (it.hasNext()) {
            String newHandles = it.next();
            System.out.println("New newHandles URL is:" + newHandles);
            if(currentHandle.equals(newHandles)) {
                continue;
            }
            try {
                String newHanlde = it.next();
                WebDriver window = dr.switchTo().window(newHanlde);// 切换到新窗口
                System.out.println("New page Title is:" + window.getTitle());
                System.out.println("New page URL is:" + window.getCurrentUrl());
            } catch (Exception e) {
                System.out.println("无法切换至新打开的窗口");
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean switchToWindowByTitle(WebDriver driver,String windowTitle){
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle))
                    continue;
                else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(windowTitle)) {
                        flag = true;
                        System.out.println("Switch to window: "
                                + windowTitle + " successfully!");
                        break;
                    } else
                        continue;
                }
            }
        } catch (NoSuchWindowException e) {
            System.out.printf("Window: " + windowTitle  + " cound not found!", e.fillInStackTrace());
            flag = false;
        }
        return flag;
    }


    // 修改初始密码
    private static void updateNewPassword(WebDriver driver,UserInfo userInfo) throws InterruptedException{
        Thread.sleep(1000);

        if(StringUtils.pathEquals(newPasswordPath,driver.getCurrentUrl())){
            //修改密码
           /* driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(userInfo.getPassword());

            driver.findElement(By.xpath("//*[@id=\"newPassword\"]")).sendKeys(newPassword);
            driver.findElement(By.xpath("//*[@id=\"passwordAgain\"]")).sendKeys(newPassword);
            driver.findElement(By.xpath("//*[@id=\"passwordChangeForm\"]/div/input")).click();
            Thread.sleep(5000);*/
            //跳转到首页
            driver.get(forwardingPath);
        }
    }

    private static void readSaveList(List<UserInfo> userInfoList) throws Exception{
        File file = ResourceUtils.getFile("classpath:userInfo.txt");
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        UserInfo userInfo = new UserInfo();
        String text = null;
        while ((text = bufferedReader.readLine()) != null) {
            String [] array = text.split(" ");
            userInfo.setUserId(array[0]);
            userInfo.setPassword(array[1]);
            userInfoList.add(userInfo);
            // 转成char加到StringBuffer对象中
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
    }

    private static void answerMap( Map<String,String> answerMap) throws Exception{
        File file = ResourceUtils.getFile("classpath:answer.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr) ;
        String text,newTitle = null,newAnswer = null;
        while( (text = br.readLine()) != null){
            if(text.contains("单选题")||text.contains("判断题")||text.contains("多选题")) {
                newTitle = dataDeal(text);
            }
            if(text.contains("正确答案")) {
                newAnswer = dataDeal(text);
                newAnswer = newAnswer.replaceAll("正确答案：","");
                if("√".equals(newAnswer) || "×".equals(newAnswer)){
                    newAnswer = newAnswer.replaceAll("√","true");
                    newAnswer = newAnswer.replaceAll("×","false");
                }else{
                    StringBuilder sBuilder = new StringBuilder();
                    char [] answerArray = newAnswer.toCharArray();
                    for(int i = 0; i < answerArray.length; i++){
                        if(i != 0){
                            sBuilder.append(",");
                        }
                        sBuilder.append(answerArray[i]);
                    }
                    newAnswer = String.valueOf(sBuilder);
                }

            }
            if(StringUtils.hasText(newTitle) && StringUtils.hasText(newAnswer)){
                answerMap.put(newTitle,newAnswer);
            }
        }
        System.out.println(answerMap);
        br.close();
        isr.close();
    }

}
