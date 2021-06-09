package com.lz.files;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lz.files.utils.SignUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.lz.files.utils.SignUtil.*;

public class SignTest {

    @Test
    public void test1() {
        // 模板内容填充
        try {
			File directory = new File("");//
			String courseFile = directory.getCanonicalPath() + "\\src\\main\\resources\\templates\\";
			System.out.println("开始签章。。。");
			long start = System.currentTimeMillis();
			editPdfTemplate(courseFile + "subscribeTemplate_act.pdf", "D:\\signpdf\\result.pdf");
            long end = System.currentTimeMillis();
            System.out.println("签章完成！耗时：" + (end - start));
		} catch (Exception e) {
		}
    }

	@Test
	public void test2() {
		// 盖章添加图片
		FileOutputStream outputStream = null;
		try {
			File directory = new File("");//
			String courseFile = directory.getCanonicalPath() + "\\src\\main\\resources\\templates\\";

			System.out.println("开始合成。。。");
			long start = System.currentTimeMillis();
			ByteArrayOutputStream result = SignUtil.signature(courseFile+"mytest.pdf",courseFile+"timg.jpg",courseFile+"client1.p12");

			outputStream = new FileOutputStream(new File("D:\\signpdf\\result2.pdf"));
			outputStream.write(result.toByteArray());
			outputStream.flush();
			long end = System.currentTimeMillis();
			System.out.println("成功合成。耗时：" + (end - start) + "ms");
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

	@Test
	public void test3() throws Exception{
		File directory = new File("");//
		String courseFile = directory.getCanonicalPath() + "\\src\\main\\resources\\templates\\";
		InputStream is = new FileInputStream(courseFile + "timg.jpg");

		byte[] bbs = new byte[is.available()];

		is.read(bbs);
		is.close();

		long start = System.currentTimeMillis();
		// 模板路径
		String templatePath = courseFile + "subscribeTemplate_act.pdf";
		// 生成的新文件路径
		String newPDFPath = "D:\\signpdf\\result.pdf";
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

		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, out);

		SignUtil.creatPdf(o, stamper);
		stamper.close();
	}

}
