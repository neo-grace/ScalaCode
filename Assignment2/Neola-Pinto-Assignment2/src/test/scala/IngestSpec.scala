
import scala.util._
import org.scalatest.{FlatSpec,Matchers}
import scala.io.Source

/**
  * Created by scalaprof on 9/13/16.
  */
class IngestSpec extends FlatSpec with Matchers {

  behavior of "ingest"

  it should "work for Int" in {
    trait IngestibleInt extends Ingestible[Int] {
      def fromString(w: String): Try[Int] = Try(w.toInt)
    }
    implicit object IngestibleInt extends IngestibleInt
    val source = Source.fromChars(Array('x', '\n', '4', '2'))
    val ingester: Ingest[Int] = new Ingest[Int]()
    val xys: Seq[Try[Int]] = ingester(source).toSeq
    // TODO check that xys has one element, consisting of Success(42) -- 10 points
    xys.size shouldBe 1
    xys.head should matchPattern {case Success(42) => }
  }

  it should "work for movie database" in {
    // NOTE that you expect to see a number of exceptions thrown. That's OK. We expect that some lines will not parse correctly.
    Try(Source.fromFile("movie_metadata.csv")) match {
      case Success(source) =>
        val ingester = new Ingest[Movie]()
        val mys: Seq[Try[Movie]] = (for (my <- ingester(source)) yield my.transform(
          { m => Success(m) }, { e => System.err.println(e); my }
        )).toSeq
        val mos: Seq[Option[Movie]] = for (my <- mys) yield for (m <- my.toOption; if m.production.country == "New Zealand") yield m
        val ms = mos.flatten
        ms.size shouldBe 4
        ms foreach { println(_) }
        source.close()
      case Failure(x) =>
        fail(x)
    }
  }

}