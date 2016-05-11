package com.workshop.web.controller;


	import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import com.workshop.util.CommonUtil;  
	  
	/** 
	 * Created by Carey on 15-2-2. 
	 */  
	public class Test {  
	  
	  
	    public static void main(String argv[]) {  
	        try {  
	        	System.out.println(CommonUtil.getExtensionName("/Users/liunaikun/黄瑶dropbox/Dropbox/咨询/沈天一 veronica941016/基本信息表(varonica941016).doc"));
//	            convert2Html("/Users/liunaikun/黄瑶dropbox/Dropbox/咨询/沈天一 veronica941016/基本信息表(varonica941016).doc","/Users/liunaikun/黄瑶dropbox/Dropbox/咨询/沈天一 veronica941016/");  
//	            convetDocx2Html("/Users/liunaikun/黄瑶dropbox/Dropbox/咨询/沈天一 veronica941016/基本信息表(varonica941016).docx","/Users/liunaikun/Desktop/");
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        System.out.println("done");
	    }  
	  
	    //输出html文件   
	    public static void writeFile(String content, String path) {  
	        FileOutputStream fos = null;   
	        BufferedWriter bw = null;  
	        org.jsoup.nodes.Document doc = Jsoup.parse(content);  
	         content=doc.html();  
	        try {  
	            File file = new File(path);  
	            fos = new FileOutputStream(file);  
	            bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));  
	            bw.write(content);  
	        } catch (FileNotFoundException fnfe) {  
	            fnfe.printStackTrace();  
	        } catch (IOException ioe) {  
	            ioe.printStackTrace();  
	        } finally {  
	            try {  
	                if (bw != null)  
	                    bw.close();  
	                if (fos != null)  
	                    fos.close();  
	            } catch (IOException ie) {  
	            }  
	        }  
	    }  
	  
	    //word 转 html   
	    public static void convert2Html(String filePath, String outputFolder)  
	            throws TransformerException, IOException,  
	            ParserConfigurationException {  
	  
	        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(filePath));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));  
	        File file = new File(filePath);
	        String fileName  = file.getName();
	        fileName = fileName.replace(".doc", ".html");
	         //兼容2007 以上版本  
//	        XSSFWorkbook  xssfwork=new XSSFWorkbook(new FileInputStream(fileName));  
	        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(  
	                DocumentBuilderFactory.newInstance().newDocumentBuilder()  
	                        .newDocument());  
	        wordToHtmlConverter.setPicturesManager( new PicturesManager()  
	        {  
	            public String savePicture( byte[] content,  
	                                       PictureType pictureType, String suggestedName,  
	                                       float widthInches, float heightInches )  
	            {  
	                return "pic/"+suggestedName;  
	            }  
	        } );  
	        wordToHtmlConverter.processDocument(wordDocument);  
	        //save pictures  
	        List pics=wordDocument.getPicturesTable().getAllPictures();  
	        if(pics!=null){  
	            for(int i=0;i<pics.size();i++){  
	                Picture pic = (Picture)pics.get(i);  
	                System.out.println();  
	                try {  
	                	File picFolder = new File(outputFolder + "pic/");
	                	if (!picFolder.exists()) {
							picFolder.mkdirs();
						}
	                    pic.writeImageContent(new FileOutputStream(outputFolder + "pic/"  
	                            + pic.suggestFullFileName()));  
	                } catch (FileNotFoundException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	        Document htmlDocument = wordToHtmlConverter.getDocument();  
	  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        DOMSource domSource = new DOMSource(htmlDocument);  
	        StreamResult streamResult = new StreamResult(out);  
	  
	  
	        TransformerFactory tf = TransformerFactory.newInstance();  
	        Transformer serializer = tf.newTransformer();  
	        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
	        serializer.setOutputProperty(OutputKeys.INDENT, "yes");  
	        serializer.setOutputProperty(OutputKeys.METHOD, "HTML");  
	        serializer.transform(domSource, streamResult);  
	        out.close();  
	        writeFile(new String(out.toByteArray()), outputFolder + fileName);  
	    }  
	    
	    public static void convetDocx2Html(String filePath, String outputFolder) throws Exception {
	    	InputStream in = new FileInputStream(filePath);
			XWPFDocument document = new XWPFDocument(in);
			
			outputFolder = CommonUtil.converterToSpell(outputFolder);
			File outputFolderFile = new File(outputFolder);
			if (!outputFolderFile.exists()) {
				outputFolderFile.mkdirs();
			}
			System.out.println(outputFolder);

			XHTMLOptions options = XHTMLOptions.create().indent(4);
			//img的src属性 后面会自动添加/word/media
			//这里就是images/word/media/ + 图片名字
			
			options.URIResolver(new BasicURIResolver( outputFolder));
			//>> 文件的保存路径 之后自动会添加 word\media子路径
			FileImageExtractor extractor = new FileImageExtractor(new File(outputFolder));
			options.setExtractor(extractor);

			// 3) Convert XWPFDocument to XHTML
			File file = new File(filePath);
	        String fileName  = file.getName();
	        fileName = CommonUtil.converterToSpell(fileName);
	        fileName = fileName.replace(".docx", ".html");
			OutputStream out = new FileOutputStream(new File(
					outputFolder + fileName));
			XHTMLConverter.getInstance().convert(document, out, options);
		}
	}  

