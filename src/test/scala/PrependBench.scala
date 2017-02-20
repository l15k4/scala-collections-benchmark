
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable.{IndexedSeq, List, Vector}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

trait PrependBench extends CollectionBenchmarkSupport {

  private def prependSeq(empty: Seq[Int], c: IndexedSeq[Int]) = c.foldLeft(empty) { case (acc, e) => e +: acc }

  private def prependList       (c: IndexedSeq[Int]) = prependSeq(List.empty[Int], c)
  private def prependVector     (c: IndexedSeq[Int]) = prependSeq(Vector.empty[Int], c)
  private def prependListBuffer (c: IndexedSeq[Int]) = prependSeq(ListBuffer.empty[Int], c)
  private def prependArrayBuffer(c: IndexedSeq[Int]) = prependSeq(ArrayBuffer.empty[Int], c)
  private def prependArray      (c: IndexedSeq[Int]) = c.foldLeft(Array.empty[Int]) { case (acc, e) => e +: acc }

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by prepending to C.empty" config(cfg:_*) in {
      performance of "Array"        in using(gen)  .in(prependArray)
      performance of "List"         in using(gen)  .in(prependList)
      performance of "Vector"       in using(gen)  .in(prependVector)
      performance of "ListBuffer"   in using(gen)  .in(prependListBuffer)
      performance of "ArrayBuffer"  in using(gen)  .in(prependArrayBuffer)
    }
  }
  bench(bigGen)

}
