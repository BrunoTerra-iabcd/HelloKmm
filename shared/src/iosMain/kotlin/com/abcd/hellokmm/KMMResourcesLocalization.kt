package com.abcd.hellokmm


import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.localizedStringWithFormat

var localizationBundle = NSBundle.mainBundle()

actual fun L.General.Home.title(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.general.home.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}
