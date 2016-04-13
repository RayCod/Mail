package cn.it.test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.junit.Test;

import cn.it.utils.BaseMongoDB;
import cn.it.utils.DBUtils;

import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

public class DBUtilsTest {

	
	
	private static Log log = LogFactory.getLog(DBUtilsTest.class); 
	private static MongoDatabase db = DBUtils.getInstance().getDb();
	private static MongoClient client = DBUtils.getInstance().getClient();

	@Test
	public void getDB() {

		//log.info(db);
		 DBUtils.getInstance().getDb();
		//DBUtils.getInstance().getClient();
		 

	}
	
	

	@Test
	public void create() {

		db.createCollection("Email");
		log.info("Collection create successfully!");
		
		
	}
	@Test
	public void del(){
		db.drop();
	}

	@Test
	public void insert() {
		MongoCollection<Document> collection = db.getCollection("Email");
//		Document document = new Document("title", "MongoDB")
//				.append("description", "database")
//				.append("likes", 100)
//				.append("by", "Fly");	
		
		Document document = new Document("title", "MongoDB")
		.append("description", "database")
		.append("likes", 100)
		.append("by", "Fly")
		.append("message",
				new Document("ywid","12345")
						.append("message", "hehe")
				);
		
		List<Document> documents = new ArrayList<Document>();
		documents.add(document);
		collection.insertMany(documents);
		log.info("insert Documents successfully");
		
	}
	@Test
	public void insert1(){
		
		String str="{\"total\":6,\"rows\":[{\"account\":{\"id\":2,\"login\":\"user\",\"name\":\"客服A\",\"pass\":\"user\"},\"hot\":false,\"id\":1,\"type\":\"男士休闲\"},{\"account\":{\"id\":1,\"login\":\"admin\",\"name\":\"管理员\",\"pass\":\"admin\"},\"hot\":false,\"id\":2,\"type\":\"儿童休闲\"},{\"account\":{\"id\":1,\"login\":\"admin\",\"name\":\"管理员\",\"pass\":\"admin\"},\"hot\":true,\"id\":3,\"type\":\"儿童休闲\"},{\"account\":{\"id\":2,\"login\":\"user\",\"name\":\"客服A\",\"pass\":\"user\"},\"hot\":true,\"id\":4,\"type\":\"女士流行\"},{\"account\":{\"id\":2,\"login\":\"user\",\"name\":\"客服A\",\"pass\":\"user\"},\"hot\":true,\"id\":8,\"type\":\"测试一下\"},{\"account\":{\"id\":1,\"login\":\"admin\",\"name\":\"管理员\",\"pass\":\"admin\"},\"hot\":true,\"id\":9,\"type\":\"女士流行\"}]}";
		Object  json =JSON.parse(str);
	
		DBUtils.getInstance().getCollection().insert((DBObject[]) json);
		
		
	}

	@Test
	public void find() {
		MongoCollection<Document> collection = db.getCollection("Email");
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			log.info(mongoCursor.next());
		}
	}
	
	@Test
	public void find1(){
		FindIterable<Document> iterable = db.getCollection("Email").find();
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		    }
		});
	}

	@Test
	public void update() {
		MongoCollection<Document> collection = db.getCollection("Email");
		// 更新文档 将文档中likes=100的文档修改为likes=200
		collection.updateMany(Filters.eq("likes", 100), new Document("$set",
				new Document("likes", 200)));
		// 检索查看结果
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			log.info(mongoCursor.next());
		}
	}

	@Test
	public void delete() {
		MongoCollection<Document> collection = db.getCollection("Email");
		// 删除符合条件的第一个文档
		collection.deleteOne(Filters.eq("title", "MongoDB"));
		// 删除所有符合条件的文档
		collection.deleteMany(Filters.eq("likes", 200));
		// 检索查看结果
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			log.info(mongoCursor.next());
		}
	}

	@Test
	public void insertFile() throws Exception {

		File file = new File("D:\\temp\\3\\SendMail.txt");
		String filename = file.getName();
		@SuppressWarnings("deprecation")
		DB test = client.getDB("test");
		GridFS gfstxt = new GridFS(test, "TXT");
		GridFSInputFile gfsFile = gfstxt.createFile(file);
		String ext = file.getName().substring(filename.lastIndexOf('.'),
				filename.length());
		String newFileName = UUID.randomUUID().toString() + ext;
		log.info(newFileName);
		gfsFile.setFilename(newFileName);
		gfsFile.save();
	}

	@Test
	public void readAll() {
		// 输出已保存的所有 输出所有保存在TXT命名空间下的文件信息
		DB test = client.getDB("test");
		GridFS gfstxt = new GridFS(test, "TXT");
		DBCursor cursor = gfstxt.getFileList();
		while (cursor.hasNext()) {
			
			DBObject  obj=cursor.next();			
			log.info(obj);
			log.info(obj.get("filename"));
		}
	}

	@Test
	public void readFile() throws Exception {
		String newFileName = "a5ee352b-7946-4095-b404-1910b4bd704a.txt";
		DB test = client.getDB("test");
		GridFS gfstxt = new GridFS(test, "TXT");
		GridFSDBFile txtForOutput = gfstxt.findOne(newFileName);
		log.info(txtForOutput);
		String outputPath = "D:\\temp\\" + newFileName;
		txtForOutput.writeTo(new File(outputPath));
		log.info("文件已经保存在" + outputPath);
	}

	@Test
	public void deleteFile() {
		String newFileName = "3bf2b02b-8f1c-460b-8ab5-6a7c2adbb906.txt";
		DB test = client.getDB("test");
		GridFS gfstxt = new GridFS(test, "TXT");
		GridFSDBFile obj=gfstxt.findOne(newFileName);
		log.info(JSON.serialize(obj));
		//gfstxt.findOne("bece8a62-eafa-4910-9f1b-9644add66e70.txt")
		gfstxt.remove(obj);		
	}
	


}
