import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'contacts_platform_interface.dart';

/// An implementation of [ContactsPlatform] that uses method channels.
class MethodChannelContacts extends ContactsPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('contacts');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
    @override
  Future<bool?> checkPermission() async {
    final result = await methodChannel.invokeMethod<bool>('checkPermission');
    return result;
  }
    @override
  Future<int?> requestPermission() async {
    final result = await methodChannel.invokeMethod<int>('requestPermission');
    return result;
  }

  @override
  Future<Map<Object?,Object?>?> requestContacts() async{
    final result = await methodChannel.invokeMethod<Map<Object?,Object?>?>('requestContacts');
    return result;
  }
}
