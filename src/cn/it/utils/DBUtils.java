package cn.it.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class DBUtils {
	private static Log logger = LogFactory.getLog(DBUtils.class); 
	private static DBUtils instance = null;

	private DBUtils() {
		// TODO Auto-generated constructor stub
	}
	

	public static DBUtils getInstance() {
		if (instance == null) {
			instance = new DBUtils();
		}
		return instance;
	}
	
	private static MongoDatabase db;

	private static MongoClient client;

	
	static {		
		String ip="127.0.0.1";
		int port=27017;
		client = new MongoClient(ip,port);
		String dbname="test";
		db=client.getDatabase(dbname);
		logger.info("Connect to "+ ip+":"+port+" database ["+dbname+" ] successfully");
	}
	
	public  MongoClient getClient() {
		return client;
	}

	public MongoDatabase getDb() {
		return db;
	}
	
	public  DBCollection getCollection(){
		 MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
		 DB db = mongoClient.getDB("mydb");
		 DBCollection collection = db.getCollection("test"); 
		 return collection;
	}
	
	
}
