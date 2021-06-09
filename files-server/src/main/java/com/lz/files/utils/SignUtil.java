package com.lz.files.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;

public class SignUtil {

    public static final char[] PASSWORD = "123456".toCharArray();//keystory密码

    /**
     * 单多次签章通用
     * @param src
     * @param target
     * @param signatureInfos
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void sign(String src, String target, SignatureInfo... signatureInfos){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            inputStream = new FileInputStream(src);
            for (SignatureInfo signatureInfo : signatureInfos) {
                ByteArrayOutputStream tempArrayOutputStream = new ByteArrayOutputStream();
                PdfReader reader = new PdfReader(inputStream);
                //创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
                PdfStamper stamper = PdfStamper.createSignature(reader, tempArrayOutputStream, '\0', null, true);
                // 获取数字签章属性对象
                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setReason(signatureInfo.getReason());
                appearance.setLocation(signatureInfo.getLocation());
                //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
                appearance.setVisibleSignature(new Rectangle(500, 10, 550, 50), 1, signatureInfo.getFieldName());
                //读取图章图片
                Image image = Image.getInstance(signatureInfo.getImagePath());
                appearance.setSignatureGraphic(image);
                appearance.setCertificationLevel(signatureInfo.getCertificationLevel());
                //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
                appearance.setRenderingMode(signatureInfo.getRenderingMode());
                // 摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                // 签名算法
                ExternalSignature signature = new PrivateKeySignature(signatureInfo.getPk(), signatureInfo.getDigestAlgorithm(), null);
                // 调用itext签名方法完成pdf签章
                MakeSignature.signDetached(appearance, digest, signature, signatureInfo.getChain(), null, null, null, 0, signatureInfo.getSubfilter());
                //定义输入流为生成的输出流内容，以完成多次签章的过程
                inputStream = new ByteArrayInputStream(tempArrayOutputStream.toByteArray());
                result = tempArrayOutputStream;
            }
            outputStream = new FileOutputStream(new File(target));
            outputStream.write(result.toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=outputStream){
                    outputStream.close();
                }
                if(null!=inputStream){
                    inputStream.close();
                }
                if(null!=result){
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 单多次签章通用
     * @param srcFile
     * @param imagePath
     * @param p12Path
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static ByteArrayOutputStream signature(String srcFile, String imagePath,String p12Path){
        InputStream inputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {

            //将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            //String pkPath = app.getClass().getResource("D:\\client1.p12").getPath();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(p12Path), PASSWORD);
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
            Certificate[] chain = ks.getCertificateChain(alias);


            inputStream = new FileInputStream(srcFile);
            PdfReader reader = new PdfReader(inputStream);
            //创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
            PdfStamper stamper = PdfStamper.createSignature(reader, result, '\0', null, true);
            // 获取数字签章属性对象
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setReason("");
            appearance.setLocation("");
            //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
            appearance.setVisibleSignature(new Rectangle(400, 10, 450, 300), 1, "sig1");
            //读取图章图片
            Image image = Image.getInstance(imagePath);
            appearance.setSignatureGraphic(image);
            appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
            //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            // 摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            // 签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, null);
            // 调用itext签名方法完成pdf签章
            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, null);
            //定义输入流为生成的输出流内容，以完成多次签章的过程
            inputStream = new ByteArrayInputStream(result.toByteArray());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=inputStream){
                    inputStream.close();
                }
                if(null!=result){
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * pdf设置域的值     操作pdf可以用迅捷pdf编辑器
     * @param templateFile
     * @param outFile
     * @throws IOException
     * @throws DocumentException
     */
    public static void editPdfTemplate(String templateFile, String outFile)
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(templateFile); // 模版文件目录
        PdfStamper ps = new PdfStamper(reader, new FileOutputStream(outFile)); // 生成的输出流
        BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        AcroFields s =  ps.getAcroFields();
        //设置文本域表单的字体
        // 对于模板要显中文的，在此处设置字体比在pdf模板中设置表单字体的好处：
        //1.模板文件的大小不变；2.字体格式满足中文要求
        s.setFieldProperty("company","textfont",bf,null);
        s.setFieldProperty("buyerName","textfont",bf,null);
        s.setFieldProperty("buildingName","textfont",bf,null);
        s.setFieldProperty("unit","textfont",bf,null);
        s.setFieldProperty("companyMobile","textfont",bf,null);
        //s.setFieldProperty("userSign","textfont",bf,null);
        //s.setFieldProperty("companySignature","textfont",bf,null);
        s.setFieldProperty("idCard","textfont",bf,null);
        s.setFieldProperty("userMobile","textfont",bf,null);
        s.setFieldProperty("year","textfont",bf,null);
        s.setFieldProperty("month","textfont",bf,null);
        s.setFieldProperty("day","textfont",bf,null);
        //编辑文本域表单的内容
        s.setField("company", "房车宝集团");
        s.setField("buyerName", "孙悟空");
        s.setField("buildingName", "花果山水帘洞景");
        s.setField("unit",  "02");
        s.setField("companyMobile",  "18888888888");
        s.setField("idCard",  "441828198812287952");
        s.setField("userMobile",  "18218089328");
        s.setField("year",  "2021");
        s.setField("month",  "06");
        s.setField("day",  "08");
        //s.setFieldProperty("Text1","readonly",TextField.READ_ONLY,null);
        ps.setFormFlattening(true); // 这句不能少
        ps.close();
        reader.close();
    }

    /**
     * 根据模板生成pdf
     * @param map Map(String,Object)
     * @return
     */
    public static void creatPdf(Map<String, Object> map, PdfStamper stamper) {
        try {
            BaseFont font = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            stamper.setFormFlattening(true);
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
            Map<String, String> dataMap = (Map<String, String>) map.get("dataMap");
            form.addSubstitutionFont(font);
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key);
                if (value != null) {
                    form.setField(key, new String(value.getBytes(), "utf8"));
                }
            }
            // 图片类的内容处理
            if (map.containsKey("imgMap")) {
                Map<String, Object> imgMap = (Map<String, Object>) map.get("imgMap");
                for (String key : imgMap.keySet()) {
                    byte[] value = (byte[]) imgMap.get(key);
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 根据字节数组读取图片
                    Image image = Image.getInstance(value);
                    // 获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    // 图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }
            // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.setFormFlattening(true);
            stamper.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }
    }
}
