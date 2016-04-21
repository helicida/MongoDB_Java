import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress;

import java.util.Arrays;

public class MongoDBJDBC {

   public static void main( String args[] ) {
	
	   MongoClient mongoClient = null;
	     
	   try {
		//  MongoClientURI uri = new MongoClientURI("mongodb://alberto:verano731@192.168.1.37:27017");
		 //  mongoClient = new MongoClient (uri);
		   mongoClient = new MongoClient(new ServerAddress("192.168.1.41"));
		   System.out.println("conexion");

	     // New way to get database
	     MongoDatabase db = mongoClient.getDatabase("prova");

	     // New way to get collection
	     MongoCollection<Document> collection = db.getCollection("exercici");
	//     System.out.println("collection: " + collection);
	     for (Document doc: collection.find()) {
	    	  System.out.println(doc.toJson());
	    	}
	    
	 
	   } catch (Exception e) {
	     e.printStackTrace();
	   } finally{
		   
	     mongoClient.close();
	     
	   }
   }
}