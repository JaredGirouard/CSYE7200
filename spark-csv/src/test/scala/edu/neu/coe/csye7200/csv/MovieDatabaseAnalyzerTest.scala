package edu.neu.coe.csye7200.csv

import org.apache.spark.sql.Dataset
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.Try

class MovieDatabaseAnalyzerTest extends AnyFlatSpec with Matchers {

  behavior of "parseResource"
  it should "get movie_metadata.csv" in {

    val mdy: Try[Dataset[Movie]] = MovieDatabaseAnalyzer("/movie_metadata.csv").dy
    mdy.isSuccess shouldBe true
    mdy foreach {
      d =>
        d.count() shouldBe 1567
        d.show(10)
    }
  }

  behavior of "movieRatingAnalyzer"
  it should "get movie_metadata.csv" in {
    val path = getClass.getResource("/movie_metadata.csv").getPath
    val df = MovieRatingAnalyzer(path).df
    df.count() shouldBe 1609
    df.show(10)
  }

  it should "get mean and std dev" in {
    val path = getClass.getResource("/movie_metadata.csv").getPath
    val processedDF = MovieRatingAnalyzer(path).processedDF
    processedDF.select("mean").first().getDouble(0) shouldBe 6.453200745804848
    processedDF.select("stddev").first().getDouble(0) shouldBe 0.9984966998015917
  }
}
