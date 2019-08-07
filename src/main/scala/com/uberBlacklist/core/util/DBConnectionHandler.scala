package com.uberBlacklist.core
package util

import java.sql.{ Connection, DriverManager }

object DBConnectionHandler {

  val dbDriver    = "com.mysql.jdbc.Driver"
  val dbUrl       = "jdbc:mysql://localhost:3306/Uber?useSSL=false"
  val dbUsername  = "root"
  val dbPassword  = "root"
  var connection: Connection = null

  Class.forName(dbDriver)
  connection    = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)
}
