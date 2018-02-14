package edu.nuist.osbank.abenablebean.util;

import java.awt.image.BufferedImage;
import java.io.*;

import org.apache.commons.logging.Log;

//import java.net.URLDecoder;
import java.nio.charset.Charset;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

//import java.text.MessageFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

public class FileUtil {
	public static final int FIL = 1;
	public static final int IMG = 2;
	public static final int TXT = 0;
	//private static Log logger = Logger.getLogger();
	
	public static void convertFileToUTF(File src, String outputFile, String encodeing){  
        char[] buf = new char[1024];  
        StringBuilder strb = new StringBuilder();  
          
        InputStreamReader isr = null;  
          
        try {  
            isr = new InputStreamReader(new FileInputStream(src), Charset.forName(encodeing));  
            int readCount = 0;  
            while ( -1 != (readCount =isr.read(buf))) {  
                strb.append(buf, 0, readCount-1);  
            }               
        }catch(FileNotFoundException e){
        	String msg = "convertFileToUTF - the file: "+ src.getName() +" doesn't exist error!";
        	//logger.error(msg);
        }catch(IOException e){
        	String msg = "convertFileToUTF - the file: "+ src.getName() +" open error!";    
        	//logger.error(msg);
        } 
        finally {  
            if (isr!=null) {  
                try {  
                    isr.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
        
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(outputFile);  
            fos.write(strb.toString().getBytes("utf-8"));  
            fos.flush();  
        }catch(IOException e){
        	//logger.error("convertFileToUTF - the converted file create error!");          
        }finally {  
            if (fos!=null) {  
                try {  
                    fos.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
    } 
	   
	
	
	//if subDirectory is null and directory isn't exist, just create directory, else create subDirectory under the directory
	 public static void createDirectory(String directory, String subDirectory) throws Exception {
	  String dir[];
	  File fl = new File(directory);
	  
	  if ("".equals(subDirectory) && fl.exists() != true)
 		fl.mkdir();
	  else if ( !"".equals(subDirectory) ) {
		dir = subDirectory.replace('\\', '/').split("/");
		 for (int i = 0; i < dir.length; i++) {
			 File subFile = new File(directory + File.separator + dir[i]);
			 if (subFile.exists() == false)
			   subFile.mkdir();
			   directory += File.separator + dir[i];
			 }
	  }
	  	
	}
	
	
	public static String getSplitedFileContent(InputStream is){
		byte b[] = new byte[Configuration.FILE_TXTRECODE_LENGTH];  
		
        int len = 0;
        int temp = 0; 
        try{
        	 while((temp=is.read())!=-1 && len< Configuration.FILE_TXTRECODE_LENGTH){  
                 b[len]=(byte)temp;  
                 len++;  
             }         	
        }catch(Exception e){
        	//logger.error(" FileSplitedException getSplitedFileContent - the file SplitedException"); 
        }       
        return new String(b,0,len); 	
	}
	
	public static void copyFile(String src, String dst){
		
		try { 
			   int byteread = 0; 
	           File oldfile = new File(src); 
	           if (oldfile.exists()) {
	               InputStream inStream = new FileInputStream(src);
	               FileOutputStream fs = new FileOutputStream(dst); 
	               byte[] buffer = new byte[1444]; 
	               while ( (byteread = inStream.read(buffer)) != -1) { 
	                  fs.write(buffer, 0, byteread); 
	               } 
	               fs.close();
	               inStream.close(); 
	           } 
	       } 
	       catch (Exception e) {
	    	   //logger.error(" FileCopyException copyFile - the file: " + src + " to dest file: "+dst +" IO Exception!");	          
	       } 		
	}
	//waiting test
	public static boolean deleteFile(String fileName) {
	   File file = new File(fileName);
	   int deleteTryCount = 5;
	   if (file.isFile() && file.exists())  file.getAbsoluteFile().delete();	    
	   while(!file.renameTo(file) && deleteTryCount >= 0)  
	    {
	    	try{
	    		file = null;
	    		System.gc();
	    		file = new File(fileName);
	    		Thread.sleep(1000);	    	
	    	}catch(Exception e){
	    		;
	    	}    		      
	    	deleteTryCount --;
	    } 	   
	    if(deleteTryCount == 0) {
	    	//logger.error(" FileDeleteException deleteFile - the file: "+ fileName + " deleted Exception");
	    	return false;
	    }
	    return  true;	    
	}
	
	public static String getFileNameFromFullPath(String path){
		return path.substring(path.lastIndexOf("/")+1);
	}
	
	public static String getFileStorePath(String fullPath){
		return fullPath.substring(0, fullPath.lastIndexOf("/"));
	}

	public static String getFileExtendName(String mimetype){
		Properties props = new Properties();
		InputStream in = null;
		String ext = null; 
		try{
			String configFilePath = Configuration.class.getClassLoader().getResource("").getFile(); //Configuration.class.getResource("/").getFile().toString();
			configFilePath = configFilePath.substring(0, configFilePath.indexOf("bin/"));
			String filePath = configFilePath + Configuration.FILEEXTENDNAMETABLE;
			props = new Properties();	
			in = new BufferedInputStream(new FileInputStream(filePath + "config.properties")); 
			ext = props.getProperty(mimetype);
		}catch(Exception e){
			//logger.error(" FileExtendNameGetException getFileExtendName - the file-mime: " + mimetype + " get Extend Name Exception");
		}finally{
			try{
				in.close();
			}catch(Exception e){
				
			}		
		}
		if(ext == null){
			if(mimetype.indexOf("htm") != -1)
				ext = ".html";			
		}
		return ext;		
	}
	
	protected static String getExceptionString(StringWriter sw, Exception e){
		e.printStackTrace(new PrintWriter(sw,true));  
    	return sw.toString();   
	}
	
	public static void unRar(String rarFileName, String dstDir) {
		StringWriter sw = new StringWriter();
		
		Archive archive = null;  
        File out = null;  
        File file = null;  
        File dir = null;  
        FileOutputStream os = null;  
        FileHeader fh = null;  
        String path, dirPath = "";  
        try {  
            file = new File(rarFileName);  
            archive = new Archive(file);  
        } catch (RarException e1) {  
        	//logger.error(" FileUnRARException " + getExceptionString(sw, e1));
        } catch (IOException e1) {  
        	//logger.error(" FileUnRARException " + getExceptionString(sw, e1));
        } finally {  
            if (file != null) {  
                file = null;  
            }  
        }  
        if (archive != null) {  
            try {  
                fh = archive.nextFileHeader();  
                while (fh != null) {  
                    String fileName = fh.getFileNameW().trim();  
                    if(!existZH(fileName)){  
                        fileName = fh.getFileNameString().trim();  
                    }  
                    path = (dstDir +"/"+ fileName).replaceAll("\\\\", "/");  
                    int end = path.lastIndexOf("/");  
                    if (end != -1) {  
                        dirPath = path.substring(0, end);  
                    }  
                    try {  
                        dir = new File(dirPath);  
                        if (!dir.exists()) {  
                            dir.mkdirs();  
                        }  
                    } catch (RuntimeException e1) {  
                    	//logger.error(" FileUnRARException " + getExceptionString(sw, e1));
                    } finally {  
                        if (dir != null) {  
                            dir = null;  
                        }  
                    }  
                    if (fh.isDirectory()) {  
                        fh = archive.nextFileHeader();  
                        continue;  
                    }  
                    out = new File(dstDir + "/" + fileName);  
                    try {  
                        os = new FileOutputStream(out);  
                        archive.extractFile(fh, os);  
                    } catch (FileNotFoundException e) {  
                    	//logger.error(" FileUnRARException " + getExceptionString(sw, e));
                    } catch (RarException e) {  
                    	//logger.error(" FileUnRARException " + getExceptionString(sw, e));
                    } finally {  
                        if (os != null) {  
                            try {  
                                os.close();  
                            } catch (IOException e) {  
                            	//logger.error(" FileUnRARException " + getExceptionString(sw, e));
                            }  
                        }  
                        if (out != null) {  
                            out = null;  
                        }  
                    }  
                    fh = archive.nextFileHeader();  
                }  
            } catch (RuntimeException e) {  
            	//logger.error(" FileUnRARException " + getExceptionString(sw, e));
            } finally {  
                fh = null;  
                if (archive != null) {  
                    try {  
                        archive.close();  
                    } catch (IOException e) {  
                    	//logger.error(" FileUnRARException " + getExceptionString(sw, e));
                    }  
                }  
            }  
        }  
        
	} 

    public static boolean existZH(String str) {  
        String regEx = "[\\u4e00-\\u9fa5]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        while (m.find()) {  
            return true;  
        }  
        return false;  
    }  
    
	/*
	public static JSONObject scanDirectory(String dir){
		
		int fileCount = 0;
		File dirFile = new File(dir);
		try{
			if(!dirFile.isDirectory()){
				fileCount = 1;
				int dataType = FileUtil.judgeFileType(dir);
				String mimeType = FileUtil.detectFileType(dir);
				String jsonTmp = MessageFormat.format(Configuration.DIRECTORY_SCAN_JSON_TEMP, 0, dataType, mimeType, dir, fileCount);
				JSONObject json = new JSONObject("{" + jsonTmp + "}");		
				return json;
			}else{
				String jsonTmp = MessageFormat.format(Configuration.DIRECTORY_SCAN_JSON_TEMP, 1, "fileDir", "fileDir",dir, 0);
				JSONObject json = new JSONObject("{" + jsonTmp + "}");
				for(String f : dirFile.list()){
					f = dir + "/" + f;
					JSONObject jo = scanDirectory(f);
					fileCount += jo.getInt("fileNums");
					json.put("fileNums", fileCount);
					JSONArray dirFiles = (JSONArray)json.get("dirFiles");
					dirFiles.put(jo);
				}
				return json;
			}		
		}catch(JSONException e){
			logger.error(" JSONParseException scanDirectory - the file directory: "+ dir + " scan json parse Exception");	
		}catch(Exception e){
			logger.error(" FileIOException scanDirectory - the file directory: "+ dir + " scan Exception");			
		}
		
	}*/
	
	public static void zipFile(String srcPathName, String dstFileName){
		/*
		 File zipFile = new File(dstFileName);      
		 File srcdir = new File(srcPathName); 
		 if (!srcdir.exists())
			 //logger.error("zipFile - the file directory: "+ srcPathName + "  Exception doesn't exist!");
		 Project prj = new Project(); 
		 Zip zip = new Zip();
		 zip.setProject(prj);
		 zip.setDestFile(zipFile);
		 FileSet fileSet = new FileSet(); 
		 fileSet.setProject(prj);
		 if(srcdir.isDirectory())
			 fileSet.setDir(srcdir);
		 else 
			 fileSet.setFile(srcdir);
		 zip.addFileset(fileSet);
		 zip.execute();   */
	}  

	public static void copyDir(String srcDir, String dstDir){
		File dst = new File(dstDir);
		File src = new File(srcDir);
		if(!src.exists()){
			//logger.error(" FileDirCopyException copyDir - the src file directory: "+ srcDir + "  doesn't exist!");
		}
		if(dst.exists()){
			//logger.error(" FileDirCopyException copyDir - the dst file directory: "+ dstDir + "  already exist!");
		}		
		if(src.isDirectory()){
			 String[] subFiles = src.list();
			 dst.mkdirs();
			 for(int i=0; i<subFiles.length; i++){
				 String newFileName = dstDir.endsWith("/") ? dstDir + subFiles[i] : dstDir + "/" +subFiles[i];
				 subFiles[i] = srcDir.endsWith("/") ? srcDir + subFiles[i] : srcDir + "/" +subFiles[i];
				 if(new File(subFiles[i]).isDirectory()){
					 copyDir(subFiles[i], newFileName);
				 }else{
					 try{
						 copyFile(subFiles[i], newFileName);
					 }catch(Exception e){
						 //logger.error(" FileCopyException copyDir:copyFile - the file: " + subFiles[i] + " to dest file: "+ newFileName +" IO Exception!");
					 }					 
				 }
			}
		}else{
			try{
				 copyFile(srcDir, dstDir);
			 }catch(Exception e){
				 //logger.error(" FileCopyException copyDir:copyFile - the file: " + srcDir + " to dest file: "+ dstDir +" IO Exception!");
			 }	
		}
	}
	
	

	public static void writeStringToFile(String str, String fileName){
		File f = new File(fileName);
		try {
		    if(!f.exists()){
		    	f.createNewFile();
		    }
		    FileWriter fw = new FileWriter(f);
		    BufferedWriter out = new BufferedWriter(fw);
		    out.write(str, 0, str.length()-1);
		    out.close();
		}catch (IOException e) {
			//logger.error(" FileIOException writeStringToFile - the file: " + fileName + " IO Exception!");
		}
	}
	
	public static String getFileContent(String targetFile){	
		BufferedReader br = null;
	    try{	    	
		    String data = "";
		    String jsonString = "";
		    InputStreamReader isr = new InputStreamReader(new FileInputStream(targetFile), "UTF-8");
	    	br = new BufferedReader(isr);  
		    data = br.readLine();
		    jsonString += data;
		    while( data != null){ 
		        data = br.readLine();
		        if(data != null) jsonString += data;
		    }  		
		    if("".equals(jsonString))
		    	return null;
		    else
		    	return jsonString.trim();
	   }catch(IOException e){
			//logger.error(" FileIOException getJSONFromFile - get JSON from the file : "+ targetFile + " IOException");
		}finally{
			if(br != null)
				try{
					br.close();
				}catch(IOException e){;}
		}	    
	    return null;
	} 

	public static boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    boolean flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}  
	
	public static void fileAppend(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            writer = new FileWriter(fileName, true);     
            writer.write(content);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }     
	
	public static void renameFile(String oldFileName, String newFileName){
		File f = new File( oldFileName );
        File f1 = new File( newFileName );
        
        f.renameTo(f1);
	}
	
}
