import 'dart:io';

import 'package:flutter/material.dart';
import 'package:starter/network/Repository.dart';

import 'package:flutter/services.dart' show ByteData, rootBundle;
import 'package:path_provider/path_provider.dart';

Future<void> main() async {
  runApp(const MyApp());
  Stopwatch stopwatch = Stopwatch()..start();
  await runProgram(await start());
  await logdata();
  done();
}

Future<void> runProgram(ProgramExecution program) async {
  print('${program.program} : ${program.parameter}');
  sleep(Duration(seconds:5));
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Example',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Ebserver Example'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      )
    );
  }
}
