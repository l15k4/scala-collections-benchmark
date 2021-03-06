
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable.{IndexedSeq, List, Vector}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

trait AppendBench extends CollectionBenchmarkSupport {
  def appendSeq         (empty: Seq[Int],             c: IndexedSeq[Int]) = c.foldLeft(empty) { case (acc, e) => acc :+ e }
  def appendBuffer      (empty: mutable.Buffer[Int],  c: IndexedSeq[Int]) = c.foldLeft(empty) { case (acc, e) => acc += e }

  def appendList        (c: IndexedSeq[Int]) = appendSeq(List.empty[Int], c)
  def appendVector      (c: IndexedSeq[Int]) = appendSeq(Vector.empty[Int], c)
  def appendListBuffer  (c: IndexedSeq[Int]) = appendBuffer(ListBuffer.empty[Int], c)
  def appendArrayBuffer (c: IndexedSeq[Int]) = appendBuffer(ArrayBuffer.empty[Int], c)
  def appendArray       (c: IndexedSeq[Int]) = c.foldLeft(Array.empty[Int]) { case (acc, e) => acc :+ e }
  def appendArraySeq    (c: IndexedSeq[Int]) = appendSeq(mutable.ArraySeq.empty[Int], c)
  def appendArrayStack  (c: IndexedSeq[Int]) = appendSeq(mutable.ArrayStack.empty, c)

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by appending to C.empty" config(cfg:_*) in {
      performance of "Array"        in using(gen)   .in(appendArray)
      performance of "List"         in using(gen)   .in(appendList)
      performance of "Vector"       in using(gen)   .in(appendVector)
      performance of "ListBuffer"   in using(gen)   .in(appendListBuffer)
      performance of "ArrayBuffer"  in using(gen)   .in(appendArrayBuffer)
      performance of "ArraySeq"     in using(gen)   .in(appendArraySeq)
      performance of "ArrayStack"   in using(gen)   .in(appendArrayStack)
    }
  }

  bench(bigGen)

}
