package cn.it.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

import cn.it.ReciveMail;
import cn.it.SendMail;
import cn.it.utils.MongoFileUtil;

public class MailTest {

	@Test
	public void testSendMail() {
		/*String smtp = "smtp.163.com";
		String from = "testmail@163.com";*/
		String smtp="127.0.0.1";
		String from="test1@test.it.com";
		String to = "test2@test.it.com";
		String copyto = "test1@test.it.com";
		String subject = "JAVA邮件测试";
		String content = "I think it’s six of one, half a dozen of the other.";
//		String username = "testmail@163.com";
//		String password = "password";
		String username="test1@test.it.com";
		String password = "123321";
		//附件地址
		String filename = "D:\\THINK\\J2EE\\workspace\\Mail\\src\\com\\sanfu\\SendMail.java";
//		StringBuffer sb = new StringBuffer();
//		try {
//			File file = new File(filename);
//			if (file.exists()) {
//				FileReader fr = new FileReader(file);
//				BufferedReader bReader = new BufferedReader(fr);
//				while (bReader.readLine() != null) {
//					sb.append(bReader.readLine() + "\n");
//				}
//				bReader.close();
//
//			} else {
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		content = sb.toString();
		
		SendMail.sendAndCc(smtp, from, to, copyto, subject, content, username,
				password, filename);
	
		//将文件保存到MongoDB 中
		try {
			MongoFileUtil.getInstance().insertFile(filename, "tes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	@Test
	public void testRecive() throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "127.0.0.1");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, null);		
		String protocol="pop3";
	    String host="127.0.0.1";
	    int port=110;
	    String file=null;
	    String username="test2@test.it.com";
	    String password="123321";
        URLName urlname=new URLName(protocol, host, port, file, username, password);
		Store store = session.getStore(urlname);
		store.connect();
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message msgs[] = folder.getMessages();
		int count = msgs.length;
		System.out.println("Message Count:" + count);
		ReciveMail rm =null;		
		for (int i = 0; i < count; i++) {
			rm = new ReciveMail((MimeMessage) msgs[i]);
			rm.recive(msgs[i], i);		
		}
	}	
	
	
	

}
