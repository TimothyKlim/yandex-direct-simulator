package dao.squerylorm.test

import dao.squerylorm.AppSchema

import org.specs2.mutable._
import org.specs2.specification._
import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm.test.helpers._
import domain.PositionValue
import dao.squerylorm.SquerylDao

class SquerylDaoSpec extends Specification with AllExpectations {

  /**
   * data simulator
   */
  val dao = new SquerylDao
  val dt_fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
  val date = dt_fmt.parseDateTime("2013-01-01")
  val plusMinutes = date.plusMinutes _

  //work with postgreSQL DB
  "fill postgreSQL DB with generating data" should {
    sequential

    "just drop and recreate schema and fill DB" in {
      TestDB_0.creating_and_filling_DB() {
        inTransaction {
          val bp = AppSchema.bannerphrases.toList.head
          val mu = PositionValue(0.01, 2.00, 2.50, 3.00, 0.20)
          val sigma = PositionValue(0.001, 0.1, 0.1, 0.1)
          val nb = 0 until 5 map (i => dao.generateNetAdvisedBids(bp, mu, sigma, plusMinutes(i)))

          AppSchema.netadvisedbidhistory.toList.length must_== (5)
        }
      }
    }
  }

  "generateNetAdvisedBids(bp, mu, sigma, dt)" should {
    sequential
    "put 5 NetAdvisedBids" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          val bp = AppSchema.bannerphrases.toList.head
          val mu = PositionValue(0.01, 2.00, 2.50, 3.00, 0.20)
          val sigma = PositionValue(0.001, 0.1, 0.1, 0.1)
          val nb = 0 until 5 map (i => dao.generateNetAdvisedBids(bp, mu, sigma, plusMinutes(i)))

          AppSchema.netadvisedbidhistory.toList.length must_== (5)
        }
      }
    }
  }
}