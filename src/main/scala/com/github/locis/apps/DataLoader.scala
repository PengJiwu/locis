package com.github.locis.apps

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.github.locis.utils.HDFSWriter
import com.github.locis.utils.Mysql

object DataLoader {

  private val logger: Logger = LoggerFactory.getLogger(getClass)
  def loadData(limit: Int = -1) = {
    //    Be careful with limit=-1. It would bring in a lot of data. (~500 mb)
    val mysql = new Mysql()
    val sqlQuery = {
      if (limit == -1) {
        "SELECT id, primary_type, longitude, latitude FROM dataset order by date asc"
      } else {
        "SELECT id, primary_type, longitude, latitude FROM dataset order by date asc limit " + limit.toString()
      }
    }
    mysql.runQuery(sqlQuery)
  }

  def main(args: Array[String]): Unit = {
    val limit = 10
    val result = loadData(limit)
    val writer = new HDFSWriter().getWriter("/user/locis/input/data")
    result.foreach { x => writer.write((x.mkString(",") + "\n").getBytes) }
    writer.close()
  }
}