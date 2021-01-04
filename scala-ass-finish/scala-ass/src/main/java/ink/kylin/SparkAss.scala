package ink.kylin

import java.util.Properties

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import scala.io.StdIn

object SparkAss {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("engine$").master("local[*]").getOrCreate
    import spark.implicits._

    val sc = spark.sparkContext



    val mysql_user="root"
    val mysql_pwd ="root"
    val mysql_driver="com.mysql.jdbc.Driver"
    val mysql_url = "jdbc:mysql://127.0.0.1:3306/airports?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"


    val connectionProperties = new Properties
    //connectionProperties.setProperty("dbtable", "data")
    connectionProperties.setProperty("user", mysql_user)
    connectionProperties.setProperty("password", mysql_pwd)
    connectionProperties.setProperty("driver", mysql_driver)
    connectionProperties.setProperty("truncate","true")


    val airports_df = spark.read.format("csv").option("header","true").csv("data/airports.csv")
    airports_df.show()

    airports_df.createOrReplaceTempView("airports_df")

    val airports_clean =spark.sql(" select regexp_replace(`\"\"iso_country\"\"`,'\"','') as iso_country," +
      "regexp_replace(`\"id,\"\"ident\"\"`,'\"','') as id_and_ident," +
      "regexp_replace(`\"\"type\"\"`,'\"','') as type," +
      "regexp_replace(`\"\"name\"\"`,'\"','') as name," +
      "regexp_replace(`\"\"continent\"\"`,'\"','') as continent," +
      "regexp_replace(`\"\"iso_region\"\"`,'\"','') as iso_region," +
      "regexp_replace(`\"\"municipality\"\"`,'\"','') as municipality," +
      "regexp_replace(`\"\"scheduled_service\"\"`,'\"','') as scheduled_service," +
      "regexp_replace(`\"\"gps_code\"\"`,'\"','') as gps_code," +
      "regexp_replace(`\"\"iata_code\"\"`,'\"','') as iata_code," +
      "regexp_replace(`\"\"local_code\"\"`,'\"','') as local_code," +
      "regexp_replace(`\"\"home_link\"\"`,'\"','') as home_link," +
      "regexp_replace(`\"\"wikipedia_link\"\"`,'\"','') as wikipedia_link," +
      "`\"\"latitude_deg\"\"` as latitude_deg,"+
      "`\"\"longitude_deg\"\"` as longitude_deg,"+
      "`\"\"elevation_ft\"\"` as elevation_ft "+
      "from airports_df").withColumn("splitCols",split($"id_and_ident",","))
      .withColumn("id",$"splitCols".getItem(0))
      .withColumn("ident",$"splitCols".getItem(1)).drop("id_and_ident","splitCols","`\"\"keywords\"\"\";`").cache()

    airports_clean.createOrReplaceTempView("airports")
    airports_clean.printSchema()
    airports_clean.write.mode(SaveMode.Overwrite).jdbc(mysql_url, "airports", connectionProperties)
    val countries_df = spark.read.format("csv").option("header","true").csv("data/countries.csv")

    val runways_df = spark.read.format("csv").option("header","true").csv("data/runways.csv")
    val airports_df_sql = spark.sql("select iso_country ,1 as count from airports ")
    val airports_top10 =  airports_df_sql.groupBy("iso_country").sum("count").orderBy(desc("sum(count)")).cache()
    //top10
    airports_top10.withColumnRenamed("sum(count)","sum_count").write.mode(SaveMode.Overwrite).jdbc(mysql_url, "airports_top", connectionProperties)

    //low10
    val airports_low10 = airports_df_sql.groupBy("iso_country").sum("count").orderBy("sum(count)").cache()

    //runways top10
    runways_df.createOrReplaceTempView("runways")
    val  runways_df_sql = spark.sql("select le_ident ,1 as count from runways")
    val runways_top10 = runways_df_sql.groupBy("le_ident").sum("count").orderBy(desc("sum(count)")).cache()

    runways_top10.withColumnRenamed("sum(count)","sum_count").write.mode(SaveMode.Overwrite).jdbc(mysql_url, "runways_top", connectionProperties)

    val runways_inner_join =  spark.sql("select a.iso_country,r.* from runways r inner join airports a on r.airport_ident=a.ident")

    runways_inner_join.createOrReplaceTempView("runways_inner")
    runways_inner_join.write.mode(SaveMode.Overwrite).jdbc(mysql_url, "runways_inner", connectionProperties)

    val country_surface =spark.sql("select distinct concat_ws('-',iso_country,surface) as country_surface from runways_inner order by country_surface ").cache()

    country_surface.write.mode(SaveMode.Overwrite).jdbc(mysql_url, "country_surface", connectionProperties)

    while (true){
      println("input 0:query ,1:report")
      val flag = StdIn.readLine()
      if("1".equals(flag)){
        println("1: report airports_top10 ")
        println("2: airports_low10")
        println("3: report runways_surface_type ")
        println("4: report runways_top10")

        val flag1 = StdIn.readLine()
        if("1".equals(flag1)){
          airports_top10.show(10)
          println("↑ airports_top10 ↑")

        }else if ("2".equals(flag1)){
          airports_low10.show(10)
          println("↑ airports_low10 ↑")
        }else if("3".equals(flag1)){
          country_surface.show()
          println("↑ runways_surface_type ↑")

        }else if("4".equals(flag1)){
          runways_top10.show(10)
          println("↑ runways_top10 ↑")

        }
      }else if("0".equals(flag)){
        println("1： query airports ")
        println("2： query runways ")
        val query = StdIn.readLine()

        if("1".equals(query)){
          println("input iso_country ：")
          val name = StdIn.readLine()

          spark.sql("select * from airports where iso_country ='"+name+"'").show()
          println(" up ↑ is airports  ↑")
        }else if("2".equals(query)){
          println("input iso_country：")
          val name = StdIn.readLine()

          spark.sql("select * from runways_inner where iso_country ='"+name+"'").show()
          println(" up ↑ is runways  ↑")
        }

      }
    }
  }
}
