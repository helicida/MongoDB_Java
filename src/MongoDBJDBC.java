import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress;
import java.util.*;

public class MongoDBJDBC {

	private static String ip = "192.168.1.109";				// IP a la que queremos conectar
	private static String nombreDatabase = "SergiDB";		// Nombre de la BBDD sobre la que queremos trabajar
	private static String nombreColeccion = "SergiDB";		// Nombre de la coleccion sobre la que queremos trabajar
	private static MongoClient clienteMongo;				// Cliente
	private static MongoCollection<Document> colleccion;	// Coleccion sobre la que trabajaremos

	public static void main(String args[]) {

		Scanner teclat = new Scanner(System.in);

		int opcio = 1;

		while (opcio != 0) {

			System.out.println("MENU:");
			System.out.println("--------------OPCIONES--------------");
			System.out.println("1 - Alta de Grupo Armado");
			System.out.println("2 - Alta de Conflicto");
			System.out.println("3 - Conflictos con más de 300 heridos");
			System.out.println("4 - Imprimir informacion de un conflitcto");
			System.out.println("0 - Salir ");

			opcio = teclat.nextInt();

			switch (opcio) {

				case 0:
					teclat.close();
				break;

				case 1:
					crearGrupoArmado();
				break;

				case 2:
					crearConflicto();
				break;

				case 3:
					conflictos300heridos();
				break;

				case 4:
					imprimirConflicto();
				break;
			}
		}
	}


	/**
	 * Con este metodo estableceremos conexión con mongo
	 */
	public static void conectarMongo() {

		clienteMongo = new MongoClient(new ServerAddress(ip));
		MongoDatabase database = clienteMongo.getDatabase(nombreDatabase);
		colleccion = database.getCollection(nombreColeccion);

		System.out.println("Se ha establecido conexión correctamente");
	}

	/**
	 * Permite crear un grupo armado y lo inserta en la BBDD
	 */
	public static void crearGrupoArmado() {

		// Hacemos la conexión a mongo
		conectarMongo();

		// Pedimos la usuario introducir los datos

		Scanner teclat = new Scanner(System.in);

		System.out.println("Introduce el codigo del grupo armado (int)");
			int codigo = teclat.nextInt();

		System.out.println("Introduce el nombre del grupo armado (String)");
			String nombre = teclat.next();

		System.out.println("Introduce las bajas del grupo armado (String)");
			String bajas = teclat.next();

		// Consideramos grupoArmado como un hash con claves predefinidas y valores dados por el usuario
		Map<String, Object> grupoArmado = new HashMap<>();

		grupoArmado.put("_id", codigo);
		grupoArmado.put("nombre", nombre);
		grupoArmado.put("bajas", bajas);

		// Insertamos en la BBDD
		colleccion.insertOne(new Document(grupoArmado));
	}

	/**
	 * Permite crear un conflicto y lo inserta en la BBDD
	 */
	public static void crearConflicto() {

		// Hacemos la conexión a mongo
		conectarMongo();

		// Pedimos la usuario introducir los datos

		Scanner teclat = new Scanner(System.in);

		System.out.println("Introduce el codigo del conflicto (int)");
			int codigo = teclat.nextInt();

		System.out.println("Introduce el nombre del conflicto (String)");
			String nombre = teclat.next();

		System.out.println("Introduce las zona del conflicto (String)");
			String zona = teclat.next();

		System.out.println("Introduce el numero de heridos en el conflicto (int)");
			int heridos = teclat.nextInt();

		System.out.println("Introduce el nombre del grupo armado que le quieras insertar (int)");
			String grupoArmado = teclat.next();

		// Buscamos en la colección por el nombre introducido por el usuario
		FindIterable<Document> cursor = colleccion.find(new BasicDBObject("nombre", grupoArmado));

		// Consideramos conflicto como un hash con claves predefinidas y valores dados por el usuario
		Map<String, Object> conflicto = new HashMap<>();

		conflicto.put("_id", codigo);
		conflicto.put("nombre", nombre);
		conflicto.put("zona", zona);
		conflicto.put("heridos", heridos);

		// Buscamos el grupoArmado que queremos vincular y lo insertamos
		for (Document document : cursor) {
			conflicto.put("gArmados", Arrays.asList(document.toJson().toString()));
		}

		// Insertamos en la BBDD
		colleccion.insertOne(new Document(conflicto));
	}

	/**
	 * Consulta los conflictos con mas de 300 heridos
	 */
	public static void conflictos300heridos() {

		/*
		NOTA: Es importante pedir al usuario los valores numericos como int's ya que a la hora de hacer consultas
		sobre estos, si los insertas como string, la consulta no va a devolver los resultados deseados
		 */

		// Hacemos la conexión a mongo
		conectarMongo();

		// Buscamos por la palabra clave heridos y que estos sean mayores a trescientos con "gt"
		FindIterable<Document> cursor = colleccion.find(new BasicDBObject("heridos", new BasicDBObject("$gt", 300)));

		// Lo mostramos por pantalla

		for (Document document : cursor) {
			System.out.println(document.toJson());
		}
	}

	/**
	 * Imprime los datos de un conflicto
	 */
	public static void imprimirConflicto() {

		// Hacemos la conexión a mongo
		conectarMongo();

		// Pedimos la usuario introducir los datos

		Scanner teclat = new Scanner(System.in);

		System.out.println("Introduce el nombre del conflicto (String)");
			String nombre = teclat.next();

		// Buscamos en la colección por el nombre introducido por el usuario
		FindIterable<Document> cursor = colleccion.find(new BasicDBObject("nombre", nombre));

		// Lo mostramos por pantalla

		for (Document document : cursor) {
			System.out.println(document.toJson());
		}
	}
}