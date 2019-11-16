package gensokyo.hakurei.chitlist

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context

class ChitlistDeviceAdminReceiver : DeviceAdminReceiver() {
    companion object {
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, ChitlistDeviceAdminReceiver::class.java)
        }
    }
}
