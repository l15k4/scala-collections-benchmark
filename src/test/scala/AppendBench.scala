
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable.{IndexedSeq, List, Vector}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object AppendBench extends ScalaCollectionsBenchmark {
  def appendSeq(empty: Seq[Int], c: IndexedSeq[Int]) = c.foldLeft(empty) { case (acc, e) => acc :+ e }

  def appendList        (c: IndexedSeq[Int]) = appendSeq(List.empty[Int], c)
  def appendVector      (c: IndexedSeq[Int]) = appendSeq(Vector.empty[Int], c)
  def appendListBuffer  (c: IndexedSeq[Int]) = appendSeq(ListBuffer.empty[Int], c)
  def appendArrayBuffer (c: IndexedSeq[Int]) = appendSeq(ArrayBuffer.empty[Int], c)
  def appendArray       (c: IndexedSeq[Int]) = c.foldLeft(Array.empty[Int]) { case (acc, e) => acc :+ e }

  def appendBench(gen: Gen[Inclusive]) = {
    performance of "Build by appending to C.empty" in {
      performance of "Array"        in using(gen)  .in(appendArray)
      performance of "List"         in using(gen)  .in(appendList)
      performance of "Vector"       in using(gen)  .in(appendVector)
      performance of "ListBuffer"   in using(gen)  .in(appendListBuffer)
      performance of "ArrayBuffer"  in using(gen)  .in(appendArrayBuffer)
    }
  }

  appendBench(bigGen)

}
