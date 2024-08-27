import 'package:flutter_test/flutter_test.dart';
import 'package:contacts/contacts.dart';
import 'package:contacts/contacts_platform_interface.dart';
import 'package:contacts/contacts_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockContactsPlatform with MockPlatformInterfaceMixin implements ContactsPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<bool?> checkPermission() {
    // TODO: implement checkPermission
    throw UnimplementedError();
  }

  @override
  Future<int?> requestPermission() {
    return 0;
  }
}

void main() {
  final ContactsPlatform initialPlatform = ContactsPlatform.instance;

  test('$MethodChannelContacts is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelContacts>());
  });

  test('getPlatformVersion', () async {
    Contacts contactsPlugin = Contacts();
    MockContactsPlatform fakePlatform = MockContactsPlatform();
    ContactsPlatform.instance = fakePlatform;

    expect(await contactsPlugin.getPlatformVersion(), '42');
  });
}
