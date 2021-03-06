/**
 * 
 */
package com.workshop.web.controller;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.util.CalendarUtil;
import com.workshop.util.CommonUtil;
import com.workshop.web.BaseController;

/**
 * @author tushen
 * @date Nov 5, 2011
 */
@Controller
public class MyHomeController extends BaseController {
	

	@RequestMapping(value="/home",method=RequestMethod.GET)
	public String home(){
		
		return "home";
	}
	
	@RequestMapping(value="/goManage",method=RequestMethod.GET)
	public String goManage(){
		
		return "redirect:index.shtml";
	}
	
	
	@RequestMapping(value = "/user/add")
	public String goAddStudent() {
		return "addStudent";
	}
	
	@RequestMapping(value="/home/uploadPic", produces = "text/html;charset=UTF-8")
	@ResponseBody
    public String uploadHeadPic(@RequestParam("file")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
            String picUrl =  this.upload(file, this.path,request);
            return picUrl;
    }
	
	
	/**
     * 
     * <p class="detail">
     * 功能：文件上传
     * </p>
     * @author wangsheng
     * @date 2014年9月25日 
     * @param files
     * @param destDir
     * @throws Exception
     */
    public String uploads(MultipartFile[] files, String destDir,HttpServletRequest request) throws Exception {
        String path = request.getContextPath();
        String picUrl = "";
        try {
            fileNames = new String[files.length];
            StringBuilder sb =  new StringBuilder();
            Date date = new Date();
    		String dateString = CalendarUtil.getDateString(date, CalendarUtil.SHORT_DATE_FORMAT_NO_DASH);
    		dateString = "workshop"  + dateString;
            for (MultipartFile file : files) {
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                int length = getAllowSuffix().indexOf(suffix);
                if(length == -1){
                    throw new Exception("请上传允许格式的文件");
                }
                if(file.getSize() > getAllowSize()){
                    throw new Exception("您上传的文件大小已经超出范围");
                }
                File destFile = new File(destDir + dateString + "/");
                if(!destFile.exists()){
                    destFile.mkdirs();
                }
                String fileNameNew = getFileNameNew()+"."+suffix;//
                File f = new File(destFile.getAbsoluteFile()+"\\"+fileNameNew);
                file.transferTo(f);
                f.createNewFile();
                sb.append("/data/").append(dateString).append("/").append(fileNameNew).append(",");
            }
            if (sb.length() > 0) {
				picUrl = sb.substring(0, sb.length() -1);
			}
        } catch (Exception e) {
            throw e;
        }
        return picUrl;
    }
     
    /**
     * 
     * <p class="detail">
     * 功能：文件上传
     * </p>
     * @author wangsheng
     * @date 2014年9月25日 
     * @param files
     * @param destDir
     * @return 
     * @throws Exception
     */
    public String upload(MultipartFile file, String destDir,HttpServletRequest request) throws Exception {
        String picUrl = "";
        try {
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                String fileOriginalName = file.getOriginalFilename();
                int length = getAllowSuffix().indexOf(suffix);
                if(length == -1){
                    throw new Exception("请上传允许格式的文件");
                }
                if(file.getSize() > getAllowSize()){
                    throw new Exception("您上传的文件大小已经超出范围");
                }
                 
                File destFile = new File(destDir  + "/");
                if(!destFile.exists()){
                    destFile.mkdirs();
                }
                File f = new File(destFile.getAbsoluteFile()+"/"+fileOriginalName);
                if (f.exists()) {
                	Date date = new Date();
            		String dateString = CalendarUtil.getDateString(date, CalendarUtil.SIMPLE_DATE_FORMAT_NO_DASH);
                	f = new File(destFile.getAbsoluteFile()+"/" + dateString + fileOriginalName);
				}
                String fileNameNew = f.getName();
                file.transferTo(f);
                f.createNewFile();
                
                //转换成html
                CommonUtil.convertWord2Html(f.getAbsolutePath(), destDir + "html/");
                
               picUrl = "/data/workshop/" + fileNameNew;
        } catch (Exception e) {
            throw e;
        }
        return picUrl;
    }
        
        /**
         * 
         * <p class="detail">
         * 功能：重新命名文件
         * </p>
         * @author wangsheng
         * @date 2014年9月25日 
         * @return
         */
        private String getFileNameNew(){
        	String uuid = UUID.randomUUID().toString();
			return uuid;
        }
        
        
        private String allowSuffix = "jpg,png,gif,jpeg,doc,docx";
        String path = "/home/data/workshop/";
//        String path ="/Users/liunaikun/Documents/swift/data/workshop/";
        private long allowSize = 5000000L;//允许文件大小
        private String fileName;
        private String[] fileNames;

		public String getAllowSuffix() {
			return allowSuffix;
		}

		public void setAllowSuffix(String allowSuffix) {
			this.allowSuffix = allowSuffix;
		}

		public long getAllowSize() {
			return allowSize;
		}

		public void setAllowSize(long allowSize) {
			this.allowSize = allowSize;
		}
        
        
}
