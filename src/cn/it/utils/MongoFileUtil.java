package cn.it.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

public class MongoFileUtil {

	
	private static Log log = LogFactory.getLog(MongoFileUtil.class); 
	
	private static MongoFileUtil instance=null;
	

	private MongoFileUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static MongoFileUtil getInstance() {
		if(instance==null){
			instance=new MongoFileUtil();
		}
		return instance;
	}
	
	
	@SuppressWarnings("deprecation")
	public void insertFile(String pathname ,String dbName ) throws Exception{	
		File file=new File(pathname);
		String filename = file.getName();
		DB test = DBUtils.getInstance().getClient().getDB("test");
		GridFS gfstxt = new GridFS(test, "ALLFILE");
		GridFSInputFile gfsFile = gfstxt.createFile(file);		
		String ext = file.getName().substring(filename.lastIndexOf('.'),
				filename.length());
		String newFileName = UUID.randomUUID().toString();
		log.info(newFileName);
		gfsFile.setFilename(filename);
		gfsFile.setId(newFileName);
		gfsFile.save();
	}
	/**
	 * 
	 * @param fileName UUID唯一文件名
	 * @param dbName 数据库名
	 */
	
	public void deleteFile(String fileName,String dbName){
		
		DB test = DBUtils.getInstance().getClient().getDB(dbName);
		GridFS gfstxt = new GridFS(test, "TXT");
		GridFSDBFile obj=gfstxt.findOne(fileName);
		log.info(JSON.serialize(obj));
		//gfstxt.findOne("bece8a62-eafa-4910-9f1b-9644add66e70.txt")
		gfstxt.remove(obj);	
	}
	
	
	
}
