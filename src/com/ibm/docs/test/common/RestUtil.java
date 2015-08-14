package com.ibm.docs.test.common;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.apache.log4j.Logger;

import com.ibm.docs.test.gui.pages.FilesPage;
//import com.ibm.test.common.FileUtil;
//import com.ibm.test.common.Logger;

/**
 * util to delete, upload file via IBM Lotus Connections3 Files api
 * @author xxt
 *
 */
public class RestUtil {
	private static final Logger log = Logger.getLogger(FilesPage.class);
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private static final String feedpathLibrary = "/files/basic/api/myuserlibrary/feed?pageSize=100";
	private static final String feedpathSingleFile = "/files/basic/api/myuserlibrary/document/{document-id}/entry";	
	private static final String feedpathTrash = "/files/basic/api/myuserlibrary/view/recyclebin/feed";
	private static final String feedpathUpload = "/files/basic/api/myuserlibrary/feed";
	
	public static void main(String[] args){
		//String uuid = "51a16867-f5b3-473b-b177-38f22af805a7";
		//deleteSingleFileViaUUID("http://docs10.cn.ibm.com","docs04","passw0rd",uuid);
		for(int i=0;i<=99;i++){
			if(i< 10){
				deleteAllFiles("https://docs10.cn.ibm.com","docs0" + i,"passw0rd");
			}else{
				deleteAllFiles("https://docs10.cn.ibm.com","docs" + i,"passw0rd");
			}			
		}
		
		/*for(int i=10;i<=79;i++){
			deleteAllFiles("https://docs10.cn.ibm.com","docs" + i,"passw0rd");
		}*/
		
		//deleteAllFiles("https://docs10.cn.ibm.com","docs00","passw0rd");
		/*String file = "G:/CL-BVT/BVT_Cell background&Text wrap - Copy.xlsx";
		String fileUploaded = "justForUploadViaRestAPI.xlsx";
		RestUtil.uploadFile("https://docs10.cn.ibm.com", "docs00", "passw0rd", file, fileUploaded);*/
	}	
	
	/**
	 * delete single file via the file's uuid, empty trash
	 * @param server
	 * @param userName
	 * @param password
	 * @param uuid
	 */
	public static void deleteSingleFileViaUUID(String server, String userName, String password, String uuid){
		deleteSingleFileInLibrary(server, userName, password, uuid);	
		deleteFilesInTrash(server, userName, password);
	}
	
	/**
	 * delete all files in specific user's library and trash.
	 * @param server, such as "https://concordvm06.cn.ibm.com"
	 * @param userName
	 * @param password
	 */
	public static void deleteAllFiles(String server, String userName, String password){
		deleteFilesInLibrary(server,userName,password);
		System.out.println("delete myFiles.");
		
		deleteFilesInTrash(server,userName,password);
		System.out.println("delete trash.");
	}
	
	/**
	 * delete all files in specific user's library
	 * @param server
	 * @param userName
	 * @param password
	 */
	public static void deleteFilesInLibrary(String server, String userName, String password){
		log.info("******" + DATEFORMAT.format(new Date()) + ", Start to delete all files in Library. Server: " +server + ", User: " + userName);
    	String feedQueryUrl = server + feedpathLibrary;	
    	String feedDeleteUrl = server + feedpathSingleFile;
    	deleteFiles(server,userName,password,feedQueryUrl, feedDeleteUrl); 
    	log.info("******" + DATEFORMAT.format(new Date()) + ", End to delete all files in Library. Server: " +server + ", User: " + userName);
	}
	
	/**
	 * delete single file via it's uuid in library.
	 * @param server
	 * @param userName
	 * @param password
	 * @param uuid
	 */
	public static void deleteSingleFileInLibrary(String server, String userName, String password, String uuid){
		String feedDeleteUrl = server + feedpathSingleFile;
		deleteSingleFile(server, userName, password, feedDeleteUrl, uuid);		
	}
	

	/**
	 * delete all files in specific user's trash
	 * @param server
	 * @param userName
	 * @param password
	 */
	public static void deleteFilesInTrash(String server, String userName, String password){
		log.info("******" + DATEFORMAT.format(new Date()) + ", Start to delete all files in Trash. Server: " +server + ", User: " + userName);
    	String feedUrl = server + feedpathTrash;     	
		ClientResponse response = null;
		try{
			AbderaClient client = new AbderaClient();	   
			Credentials creds = new UsernamePasswordCredentials(userName, password);
	        client.addCredentials(server, null, null, creds);	        
	        client.usePreemptiveAuthentication(true);
	        client.setConnectionManagerTimeout(1000);	               
	        AbderaClient.registerTrustManager();	        
	        RequestOptions options = client.getDefaultRequestOptions();	        
	        options.setNoCache(true); 
	        response = client.delete(feedUrl);
	        log.info("******" + DATEFORMAT.format(new Date()) + ", End to delete all files in Library. Server: " +server + ", User: " + userName);
		}catch(Exception e){
			log.error("Failed to delete all files under " + feedUrl + " Exception: " + e.getMessage());
		}finally{
			if(response != null){
				response.release();
			}
		}
	}
	
	private static void deleteSingleFile(String server, String userName, String password, String feedDeleteUrl, String uuid){
		ClientResponse response = null;
		String url = feedDeleteUrl.replace("{document-id}", uuid);
		try{
			AbderaClient client = new AbderaClient();	   
			Credentials creds = new UsernamePasswordCredentials(userName, password);
	        client.addCredentials(server, null, null, creds);	        
	        client.usePreemptiveAuthentication(true);
	        client.setConnectionManagerTimeout(1000);	               
	        AbderaClient.registerTrustManager();	        
	        RequestOptions options = client.getDefaultRequestOptions();	        
	        options.setNoCache(true); 	        
    		client.delete(url);
    		log.info("****delete single file: " + url);		               
		}catch(Exception e){
			log.error("Failed to delete single file " + url + " Exception: " + e.getMessage());
		}finally{
			if(response != null){
				response.release();
			}
		}
	}
	
	/**
	 * delete all files in specific feed url.
	 * first retrieve all files under the fee url, and get every file's href, 
	 * then delete the file via its href.
	 * @param server
	 * @param userName
	 * @param password
	 * @param feedUrl
	 */
	private static void deleteFiles(String server, String userName, String password, String feedQueryUrl, String feedDeleteUrl){
		ClientResponse response = null;
		try{
			AbderaClient client = new AbderaClient();	   
			Credentials creds = new UsernamePasswordCredentials(userName, password);
	        client.addCredentials(server, null, null, creds);	        
	        client.usePreemptiveAuthentication(true);
	        client.setConnectionManagerTimeout(1000);	               
	        AbderaClient.registerTrustManager();	        
	        RequestOptions options = client.getDefaultRequestOptions();	        
	        options.setNoCache(true); 
	        
	        int cycle = 1;
	        while(true){
	        	response = client.get(feedQueryUrl);
	        	log.info("***Cycle: " + cycle + ", retrive files status: " + response.getStatusText());
		        cycle++;
		        Document doc = parseXML(response.getInputStream());
		        NodeList entryList = doc.getElementsByTagName("entry");
		        if(entryList.getLength() <=0 ){
		        	return;
		        }else{
		        	for(int i=0;i<entryList.getLength();i++){
		        		Element element = (Element)entryList.item(i);		        		
		        		NodeList ids = element.getElementsByTagName("td:uuid");
		        		if(ids.getLength() <= 0){
		        			ids = element.getElementsByTagName("td:label");
		        		}
		        		String uuid = ids.item(0).getTextContent();
		        		String url = feedDeleteUrl.replace("{document-id}", uuid);
		        		client.delete(url);
		        		log.info("****delete single file: " + url);		        		
		        	}
		        }
	        }
		}catch(Exception e){
			log.error("Failed to delete all files under " + feedQueryUrl + " Exception: " + e.getMessage());
		}finally{
			if(response != null){
				response.release();
			}
		}
	}
	
	/**
	 * upload file to specific server via specific user
	 * @param server, such as "https://docs10.cn.ibm.com"
	 * @param userName
	 * @param password
	 * @param fileFullName
	 * @param uploadFileName
	 */
	public static void uploadFile(String server, String userName, String password, String fileFullName, String uploadFileName){
		File file = new File(fileFullName);
		ClientResponse response = null;
		try{
			AbderaClient.registerTrustManager();
	        AbderaClient client = new AbderaClient();
	        Credentials creds = new UsernamePasswordCredentials(userName, password);
	        client.addCredentials(server, null, null, creds);
	        client.usePreemptiveAuthentication(true);
	        client.setConnectionManagerTimeout(1000);

	        String feedUrl = server + feedpathUpload;	        
	        RequestOptions options = new RequestOptions();	      
	        options.setSlug(uploadFileName); // Slug is required for POST binary content in request body

	        response = client.post(feedUrl, new FileInputStream(file), options);
            //log.log(Level.INFO, "upload file : " + uploadFileName + ", status: " + response.getStatusText());	        
		}catch(Exception e){
	    	throw new RuntimeException("Failed to upload file: " + fileFullName, e);
	    }finally{
	    	if(response != null){
	    		response.release();
	    	}
	    }
	}
	
	/**
	 * Parse XML file to Document model
	 * @param path
	 * @return
	 */
	private static Document parseXML(InputStream inputStream) throws Exception{		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		dbfac.setNamespaceAware(true);
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		return docBuilder.parse(inputStream);		
	}
	
}
