// Generated by KMM Resources
package com.abcd.hellokmm

class L {
  companion object {
    val general: General = General()
  }
  data class General(
    val home: Home = Home()) {
    class Home {
      operator fun get(key: String): String? = when(key) {
        "title" -> title()
        else -> null
      }
    }
  }
}


expect fun L.General.Home.title(): String
