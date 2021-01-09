import 'package:dio/dio.dart';
import 'package:logger/logger.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseController {
  var logger = Logger(
    printer: PrettyPrinter(methodCount: 0),
  );

  Future<void> abrirBanco() async {
    var databasesPath = await getDatabasesPath();
    var path = join(databasesPath, "teste_carga.db");
    logger.d(path);

// Check if the database exists
    var exists = await databaseExists(path);

    if (!exists) {
      logger.i("Banco de dados inexistente, fazendo o download");

      try {
        Response response;
        Dio dio = new Dio();
        response =
            await dio.download("http://192.168.1.146:8080/poc/banco", path);
      } catch (e, s) {
        logger.e(e);
      }
    } else {
      logger.i("Abrindo o banco de dados...");
    }
// open the database
    final db = await openDatabase(path, readOnly: true);
    List<Map> list = await db.rawQuery("SELECT COUNT(*) FROM pessoa");
    logger.i(list);
  }

  DatabaseController();
}
