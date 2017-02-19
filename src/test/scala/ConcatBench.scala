
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive

trait ConcatBench extends CollectionBenchmarkSupport {

  private def concatMap       (c: Seq[scala.collection.Map[_, _]])  = c.reduce(_ ++ _)
  private def concatIterable  (c: Seq[Iterable[Int]])               = c.reduce(_ ++ _)
  private def concatArray     (c: Seq[Array[Int]])                  = c.reduce(_ ++ _)

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by concatenating C.++ (total C.size equals to other benchmarks)" in {
      performance of "HashMap"          in using(gen.map(_.map(size => buildHashMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "ListMap"          in using(gen.map(_.map(size => buildListMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "TreeMap"          in using(gen.map(_.map(size => buildTreeMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "LongMap"          in using(gen.map(_.map(size => buildLongMap((0 to size).zipWithIndex.map(t => t._1.toLong -> t._2)))))  .in(concatMap)
      performance of "HashSet"          in using(gen.map(_.map(size => buildHashSet(0 to size))))                                               .in(concatIterable)
      performance of "TreeSet"          in using(gen.map(_.map(size => buildTreeSet(0 to size))))                                               .in(concatIterable)
      performance of "Array"            in using(gen.map(_.map(size => buildArray(0 to size))))                                                 .in(concatArray)
      performance of "List"             in using(gen.map(_.map(size => buildList(0 to size))))                                                  .in(concatIterable)
      performance of "Vector"           in using(gen.map(_.map(size => buildVector(0 to size))))                                                .in(concatIterable)
      performance of "ListBuffer"       in using(gen.map(_.map(size => buildListBuffer(0 to size))))                                            .in(concatIterable)
      performance of "ArrayBuffer"      in using(gen.map(_.map(size => buildArrayBuffer(0 to size))))                                           .in(concatIterable)
    }
  }
  bench(sqrtGen)

}
