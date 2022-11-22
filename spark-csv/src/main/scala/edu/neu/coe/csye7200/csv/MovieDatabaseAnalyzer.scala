package edu.neu.coe.csye7200.csv

import com.phasmidsoftware.table.Table
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import scala.util.{Try}
import org.apache.spark.sql.functions.{mean, stddev_pop}


/**
 * @author scalaprof
 */
case class MovieDatabaseAnalyzer(resource: String) {

  val spark: SparkSession = SparkSession
          .builder()
          .appName("AnalyzeRating")
          .master("local[*]")
          .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // We want to ignore all of the INFO and WARN messages.

  import MovieParser._
  import spark.implicits._

  private val mty: Try[Table[Movie]] = Table.parseResource(resource, getClass)
  val dy: Try[Dataset[Movie]] = mty map {
    mt =>
      println(s"Movie table has ${mt.size} rows")
      spark.createDataset(mt.rows.toSeq)
  }
}

case class MovieRatingAnalyzer(path: String)
{
  val spark: SparkSession = SparkSession
    .builder()
    .appName("MovieRatingAnalyzer")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // We want to ignore all of the INFO and WARN messages.

  val df = spark.read.option("header", "true").csv(path)
  val processedDF = scoreProcessor(df)
  processedDF.show()

  def scoreProcessor(df: DataFrame): DataFrame = {
    df.select(mean("imdb_score").alias("mean"), stddev_pop("imdb_score").alias("stddev"))
  }
}

object MovieDatabaseAnalyzer extends App {
  val path = getClass.getResource("/movie_metadata.csv").getPath
  MovieRatingAnalyzer(path)
}
