package com.heima.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Appliccation {
    /**
     * 识别图片中的文字
     *
     * @param args
     */
    public static void main(String[] args) throws TesseractException {
//创建实列
        ITesseract tesseract = new Tesseract();
        //设置字体库路径
        tesseract.setDatapath("C:\\springcloud\\daili");
        //设置语言-->简体中文
        tesseract.setLanguage("chi_sim");
        //识别图片
        File file = new File("C:\\logs\\1.png");
        String result = tesseract.doOCR(file);
        System.out.println("识别结果为：" + result);
    }
}
