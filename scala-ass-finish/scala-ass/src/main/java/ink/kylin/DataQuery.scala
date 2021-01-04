package ink.kylin

import java.sql
import java.sql.DriverManager



object DataQuery {


  val mysql_url = "jdbc:mysql://127.0.0.1:3306/airports?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"

  val driver = "com.mysql.jdbc.Driver"

  val username = "root"
  val password = "root"

  def getAirPortTop10(): String = {

    val query = "select iso_country,sum_count from airports_top order by sum_count desc limit 10"
    println("query sql = %s ".format(query))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query)
    val res = new StringBuilder()
    while (rs.next) {
      val iso_country = rs.getString("iso_country")
      val sum_count = rs.getInt("sum_count")
      res.append("iso_country = %s, sum_count = %s".format(iso_country, sum_count)).append("\n")
    }
    res.toString()
  }

  def getConnection(url:String,username:String,pwd:String,driverName: String): sql.Connection={
    //注册Driver
    Class.forName(driverName)
    //得到连接
    val connection = DriverManager.getConnection(url, username, pwd)
    connection
  }


  def getRunwaysSurfaceType(): String = {

    val query = "select * from country_surface"
    println("query sql = %s ".format(query))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query)
    val res = new StringBuilder()
    while (rs.next) {
      val country_surface = rs.getString("country_surface")
      res.append("country_surface = %s".format(country_surface)).append("\n")
    }
    res.toString()
  }

  def getRunwaysTop10(): String = {

    val query = "select le_ident,sum_count from runways_top order by sum_count desc limit 10"
    println("query sql = %s ".format(query))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query)
    val res = new StringBuilder()
    while (rs.next) {
      val le_ident = rs.getString("le_ident")
      val sum_count = rs.getInt("sum_count")
      res.append("le_ident = %s, sum_count = %s".format(le_ident, sum_count)).append("\n")
    }
    res.toString()
  }

  def getAirPortLow10(): String = {

    val query = "select iso_country,sum_count from airports_top order by sum_count limit 10"
    println("query sql = %s ".format(query))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query)
    val res = new StringBuilder()
    while (rs.next) {
      val iso_country = rs.getString("iso_country")
      val sum_count = rs.getInt("sum_count")
      res.append("iso_country = %s, sum_count = %s".format(iso_country, sum_count)).append("\n")
    }
    res.toString()
  }


  def airPortsQuery(query: String): String = {
    val query_sql = "select iso_country,name,type,municipality from airports where iso_country like '%"+query+"%'"
    println("query sql = %s ".format(query_sql))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query_sql)
    val res = new StringBuilder()
    while (rs.next) {
      val iso_country = rs.getString("iso_country")
      val name = rs.getString("name")
      val type_query = rs.getString("type")
      val municipality = rs.getString("municipality")
      res.append("%s, %s, %s, %s".format(iso_country, name,type_query,municipality)).append("\n")
    }
    res.toString()
  }

  def runwaysQuery(query: String): String = {
    val query_sql = "select iso_country,id,surface,airport_ref,airport_ident from runways_inner where iso_country like '%"+query+"%'"
    println("query sql = %s ".format(query_sql))
    val statement = getConnection(mysql_url,username,password,driver).createStatement
    val rs = statement.executeQuery(query_sql)
    val res = new StringBuilder()
    while (rs.next) {
      val iso_country = rs.getString("iso_country")
      val id = rs.getString("id")
      val surface = rs.getString("surface")
      val airport_ref = rs.getString("airport_ref")
      val airport_ident = rs.getString("airport_ident")
      res.append("%s, %s, %s, %s,%s".format(iso_country,id, surface,airport_ref,airport_ident)).append("\n")
    }
    res.toString()
  }

}
