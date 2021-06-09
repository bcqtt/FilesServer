package com.lz.files.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import com.lz.files.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SignServiceImpl implements ISignService {

    public static final char[] PASSWORD = "123456".toCharArray();//keystory密码

    @Async("taskExecutor")
    @Override
    public void executeAsyncSign(int i) {

        FileOutputStream outputStream = null;
        long start = System.currentTimeMillis();
        try {
            File directory = new File("");//
            String courseFile = directory.getCanonicalPath() + "\\src\\main\\resources\\templates\\";
            ByteArrayOutputStream result = SignUtil.signature(courseFile+"mytest.pdf",courseFile+"timg.jpg",courseFile+"client1.p12");
            outputStream = new FileOutputStream(new File("D:\\signpdf\\result_" + i +".pdf"));
            //outputStream = new FileOutputStream(new File("/data/signpdf/result_" + (i+1) +".pdf"));
            outputStream.write(result.toByteArray());
            outputStream.flush();
            long end = System.currentTimeMillis();
            log.info("签署result_" + (i+1) +".pdf耗时：" + (end - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Async("taskExecutor")
    @Override
    public void executeAsyncSign2( int i ) throws IOException, DocumentException {
        File directory = new File("");//
        String courseFile = directory.getCanonicalPath() + "\\src\\main\\resources\\templates\\";
        InputStream is = new FileInputStream(courseFile + "timg.jpg");

        byte[] bbs = new byte[is.available()];

        is.read(bbs);
        is.close();

        long start = System.currentTimeMillis();
        // 模板路径
        String templatePath = courseFile + "subscribeTemplate_act.pdf";
        templatePath = "http://172.25.104.16:81/download/subscribeTemplate_act.pdf";
        // 生成的新文件路径
        String newPDFPath = "D:\\signpdf\\result_" + i + ".pdf";
        //文字类
        Map<String, String> data = new HashMap<String, String>();
        data.put("company", "房车宝集团");
        data.put("buyerName", "孙悟空");
        data.put("buildingName", "花果山水帘洞景");
        data.put("unit",  "02");
        data.put("companyMobile",  "18888888888");
        data.put("idCard",  "441828198812287952");
        data.put("userMobile",  "18218089328");
        data.put("year",  "2021");
        data.put("month",  "06");
        data.put("day",  "08");

        //图片
        Map<String, Object> imgMap = new HashMap<String, Object>();
        imgMap.put("userSign", bbs);
        //根据模板生成pdf
        Map<String, Object> o = new HashMap<String, Object>();
        o.put("dataMap", data);
        o.put("imgMap", imgMap);

        PdfReader reader = new PdfReader(templatePath);// 读取pdf模板
        FileOutputStream out = new FileOutputStream(newPDFPath);// 输出流

        PdfStamper stamper = new PdfStamper(reader, out);

        SignUtil.creatPdf(o, stamper);
        stamper.close();

        long end = System.currentTimeMillis();
        log.info("成功合成。耗时：" + (end - start) + "ms");
    }
}
