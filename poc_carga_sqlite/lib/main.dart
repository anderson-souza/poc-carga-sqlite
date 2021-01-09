import 'package:flutter/material.dart';
import 'package:poc_carga_sqlite/DatabaseController.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Column(
          children: [
            RaisedButton(
              onPressed: () => DatabaseController().abrirBanco(),
              child: Text("Abrir Banco de Dados"),
            )
          ],
        ),
      ),
      appBar: AppBar(
        title: Text("Teste POC Carga Sqlite"),
      ),
    );
  }
}
