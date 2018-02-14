package edu.nuist.osbank.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuUpload{

	private String ACCESS_KEY;
	private String SECRET_KEY; 
	private String myBucket;
	private String uploadFileName;
	private String fileName;
	private boolean state = true;
	
	public QiniuUpload(String accessKey, String secretKey, String myBucket, String uploadFile, String uploadFileName){
		this.ACCESS_KEY = accessKey;
		this.SECRET_KEY = secretKey;
		this.myBucket = myBucket;
		this.uploadFileName = uploadFile;
		this.fileName = uploadFileName;
	}
	
	class MyRet {
	    public long fsize;
	    public String key;
	    public String hash;
	    public int width;
	    public int height;
	}


	public boolean upload(UploadManager manager, Auth auth, String myBucket) {
	    try {
	        Response res = manager.put( uploadFileName, this.fileName, getUpToken(auth, myBucket));
	       
	       // manager.put()
	        MyRet ret = res.jsonToObject(MyRet.class);
	       // System.out.println(res);
			return true;
	    } catch (QiniuException e) {
	    	e.printStackTrace();
	        Response r = e.response;
	        // 请求失败时简单状态信息
	      //  System.out.println("failed: " + r.toString());
	        try {
	        	System.out.println("failed: " + r.bodyString());
				if( r.bodyString().indexOf("file exists") != -1)
					return true;
	        } catch (QiniuException e1) {
	        	
	        }
	    }

		return false;
	}

	public String getUpToken(Auth auth, String bucketname){
		return auth.uploadToken(bucketname);
	}
	
	public void run() {
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		UploadManager manager = new UploadManager();
		this.upload(manager, auth, this.myBucket);
		this.state = false;
	}
	
	public boolean getState(){
		return this.state;
	}
	
	public QiniuUpload(){;}
	
	public QiniuUpload(String ak, String sk, String bucket){
		this.ACCESS_KEY = ak;
		this.SECRET_KEY = sk;
		this.myBucket = bucket;
	}
	
	
	public static void main(String[] args){
		//String ak = "Rw4s0wsGyiRvL4NGKbiI0Eo3CcHhJUvrosSKZOm-";
		//String sk = "_9CzeLl4V1UiLTPAXzHBHi_mP85-1GGSCzp57j7z";
		//String bucket = "taobaotrans";
       
		/*String ak="Mkgyd4z1eSAOWccUnSrzB5mL1r9Ih9DQAd5wsRGu";
		String sk="Mt98lWJ3hKGaLIv5_Lh-GRdIezOp6HkIRqUsOmIA";
		String bucket="lnbucket";
		Auth auth = Auth.create(ak, sk);
		UploadManager manager = new UploadManager();
		new QiniuUpload(ak, sk, bucket, "E:\\burst.jpg", "burst.jpg")
				.upload(manager, auth, bucket);*/
		//String URL="http://taobaotrans/burst.jpg";
		//String downloadRUL = auth.privateDownloadUrl(URL,3600);
	     //System.out.println(downloadRUL);
		
		long timestamp=System.currentTimeMillis();
		System.out.println(timestamp);
	}

	public String getACCESS_KEY() {
		return ACCESS_KEY;
	}

	public void setACCESS_KEY(String aCCESS_KEY) {
		ACCESS_KEY = aCCESS_KEY;
	}

	public String getSECRET_KEY() {
		return SECRET_KEY;
	}

	public void setSECRET_KEY(String sECRET_KEY) {
		SECRET_KEY = sECRET_KEY;
	}

	public String getMyBucket() {
		return myBucket;
	}

	public void setMyBucket(String myBucket) {
		this.myBucket = myBucket;
	}


	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
