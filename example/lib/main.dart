import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:contacts/contacts.dart';
import 'package:flutter/widgets.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _havePermission = false;
  String _permissionStatus = '';
  Map<Object?, Object?> _contactMap = {};
  final _contactsPlugin = Contacts();

  @override
  void initState() {
    super.initState();
    checkPermission();
  }

  Future<void> checkPermission() async {
    String platformVersion;

    try {
      platformVersion = await _contactsPlugin.getPlatformVersion() ?? 'Unknown platform version';
      _havePermission = await _contactsPlugin.checkPermission() ?? false;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> requestPermission() async {
    var status = await _contactsPlugin.requestPermission();

    setState(() {
      _permissionStatus = status.toString();
    });

    if (status == 1) {
      debugPrint("contact request started.");
      requestContacts();
    }
  }

  Future<void> requestContacts() async {
    var status = await _contactsPlugin.requestContacts();

    debugPrint("contact request success");
    debugPrint("contact : ${status?.length}");

    setState(() {
      _contactMap = status ?? {};
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              const SizedBox(height: 12),
              Text('Have contact permission : $_havePermission'),
              const SizedBox(height: 12),
              Text('Permission Status : $_permissionStatus'),
              const SizedBox(height: 12),
              Expanded(
                child: ListView.builder(
                    itemCount: _contactMap.length,
                    itemBuilder: (context, index) {
                      String key = _contactMap.keys.elementAt(index).toString();
                      String value = _contactMap[key].toString();
                      return ListTile(
                        title: Text(key),
                        subtitle: Text(value),
                      );
                    }),
              )
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            requestPermission().then((value) => checkPermission());
          },
        ),
      ),
    );
  }
}
