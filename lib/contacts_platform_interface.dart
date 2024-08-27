import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'contacts_method_channel.dart';

abstract class ContactsPlatform extends PlatformInterface {
  /// Constructs a ContactsPlatform.
  ContactsPlatform() : super(token: _token);

  static final Object _token = Object();

  static ContactsPlatform _instance = MethodChannelContacts();

  /// The default instance of [ContactsPlatform] to use.
  ///
  /// Defaults to [MethodChannelContacts].
  static ContactsPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ContactsPlatform] when
  /// they register themselves.
  static set instance(ContactsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<bool?> checkPermission() {
    throw UnimplementedError('checkPermission() has not been implemented.');
  }

  Future<int?> requestPermission() {
    throw UnimplementedError('requestPermission() has not been implemented.');
  }

  Future<Map<Object?,Object?>?> requestContacts() {
    throw UnimplementedError('requestContacts() has not been implemented.');
  }
}
