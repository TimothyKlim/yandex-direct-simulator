package dao.squerylorm

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import java.sql.Timestamp
import org.joda.time._
import scala.reflect._

@BeanInfo
case class NetAdvisedBidHistory(
  val bannerphrase_id: Long = 0, //fk
  val date: Timestamp = new Timestamp(0),
  val a: Double = 0,
  val b: Double = 0,
  val c: Double = 0,
  val d: Double = 0,
  val e: Double = 0,
  val f: Double = 0) extends domain.NetAdvisedBids with KeyedEntity[Long] with History {
  val id: Long = 0
  def dateTime = date

  /**
   * default put - save to db
   */
  def put(): NetAdvisedBidHistory = inTransaction { AppSchema.netadvisedbidhistory insert this }

}

object NetAdvisedBidHistory {
  /**
   * get NetAdvisedBidHistory from DB
   */
  def get_by_id(id: Long): NetAdvisedBidHistory = inTransaction {
    AppSchema.netadvisedbidhistory.where(a => a.id === id).single
  }

  /**
   * construct NetAdvisedBid from domain.NetAdvisedBidHistory and BannerPhrase
   */
  def apply(bp: domain.BannerPhrase, nb: domain.NetAdvisedBids): NetAdvisedBidHistory =
    NetAdvisedBidHistory(
      bannerphrase_id = bp.id,
      date = nb.dateTime,
      a = nb.a,
      b = nb.b,
      c = nb.c,
      d = nb.d,
      e = nb.e,
      f = nb.f)

  import domain.Position._
  import domain.PositionValue._
  def generate(bp: domain.BannerPhrase, mu: PositionValue, sigma: PositionValue, ts: Timestamp): NetAdvisedBidHistory = {
    import org.apache.commons.math3.random.RandomDataGenerator
    val r = new RandomDataGenerator()
    NetAdvisedBidHistory(
      bannerphrase_id = bp.id,
      date = ts,
      a = r.nextGaussian(mu(_min), sigma(_min)),
      b = r.nextGaussian(mu(_max), sigma(_max)),
      c = r.nextGaussian(mu(_pMin), sigma(_pMin)),
      d = r.nextGaussian(mu(_pMax), sigma(_pMax)),
      e = r.nextGaussian(0, 1),
      f = r.nextGaussian(0, 1))
  }
}
