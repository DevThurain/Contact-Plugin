package com.thurainx.contacts

import android.app.Activity
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** ContactsPlugin */
class ContactsPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private var permissionManager: PermissionManager? = null
  private var activityPluginBinding: ActivityPluginBinding? = null
  private var contactsManager: ContactsManager? = null

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "contacts")
    channel.setMethodCallHandler(this)

    permissionManager = PermissionManager(context = flutterPluginBinding.applicationContext)
    contactsManager = ContactsManager()
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    permissionManager?.initResult(result)
    when(call.method){
      "getPlatformVersion" -> {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }
      "checkPermission" -> {
        result.success(permissionManager?.determineContactPermission());
      }
      "requestPermission" -> {
        permissionManager?.requestContactPermission();
      }
      "openSettings" -> {

      }
      "requestContacts" -> {
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        scope.launch {
          val contactMap = contactsManager?.fetchContacts()

          withContext(Dispatchers.Main){
            print(contactMap)
            result.success(contactMap)
          }
        }
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    listenToActivity(binding.activity)
    activityPluginBinding = binding
    registerBindingMethods()
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    stopListenToActivity()
    unregisterBindingMethods()
    activityPluginBinding = null
  }

  private fun registerBindingMethods(){
    permissionManager?.let { manager ->
      activityPluginBinding?.addActivityResultListener(manager)
      activityPluginBinding?.addRequestPermissionsResultListener(manager)
    }
  }

  private fun unregisterBindingMethods(){
    permissionManager?.let { manager ->
      activityPluginBinding?.removeActivityResultListener(manager)
      activityPluginBinding?.removeRequestPermissionsResultListener(manager)
    }
  }

  private fun listenToActivity(activity: Activity){
    permissionManager?.let { manager ->
      manager.initActivity(activity)
    }
    contactsManager?.let { manager ->
      manager.initActivity(activity)
    }
  }

  private fun stopListenToActivity(){
    permissionManager?.let { manager ->
      manager.initActivity(null)
    }
    contactsManager?.let { manager ->
      manager.initActivity(null)
    }
  }

}
