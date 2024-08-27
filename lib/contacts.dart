import 'contacts_platform_interface.dart';

class Contacts {
  Future<String?> getPlatformVersion() {
    return ContactsPlatform.instance.getPlatformVersion();
  }

  Future<bool?> checkPermission() {
    return ContactsPlatform.instance.checkPermission();
  }

  Future<int?> requestPermission() {
    return ContactsPlatform.instance.requestPermission();
  }

  Future<Map<Object?,Object?>?> requestContacts() {
    return ContactsPlatform.instance.requestContacts();
  }
}
