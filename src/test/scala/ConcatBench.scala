
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive

trait ConcatBench extends CollectionBenchmarkSupport {

  private def concatMap       (c: Seq[scala.collection.Map[_, _]])  = c.reduce(_ ++ _)
  private def concatIterable  (c: Seq[Iterable[Int]])               = c.reduce(_ ++ _)
  private def concatArray     (c: Seq[Array[Int]])                  = c.reduce(_ ++ _)

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by concatenating C.++ (total C.size equals to other benchmarks)" config(cfg:_*) in {
      performance of "mutable.HashMap"        in using(gen.map(_.map(size => buildMutableHashMap((0 to size).zipWithIndex))))                                 .in(concatMap)
      performance of "mutable.ListMap"        in using(gen.map(_.map(size => buildMutableListMap((0 to size).zipWithIndex))))                                 .in(concatMap)
      performance of "mutable.TreeMap"        in using(gen.map(_.map(size => buildMutableTreeMap((0 to size).zipWithIndex))))                                 .in(concatMap)
      performance of "mutable.ListMap"        in using(gen.map(_.map(size => buildMutableListMap((0 to size).zipWithIndex))))                                 .in(concatMap)
      performance of "mutable.LinkedHashMap"  in using(gen.map(_.map(size => buildMutableLinkedHashMap((0 to size).zipWithIndex))))                           .in(concatMap)
      performance of "mutable.LongMap"        in using(gen.map(_.map(size => buildMutableLongMap((0 to size).zipWithIndex.map(t => t._1.toLong -> t._2)))))   .in(concatMap)
      performance of "mutable.WeakHashMap"    in using(gen.map(_.map(size => buildMutableWeakHashMap((0 to size).zipWithIndex))))                             .in(concatMap)
      performance of "mutable.OpenHashMap"    in using(gen.map(_.map(size => buildMutableOpenHashMap((0 to size).zipWithIndex))))                             .in(concatMap)

      performance of "concurrent.TrieMap"     in using(gen.map(_.map(size => buildConcurrentTrieMap((0 to size).zipWithIndex))))                              .in(concatMap)

      performance of "mutable.LinkedHashSet"  in using(gen.map(_.map(size => buildMutableLinkedHashSet(0 to size))))                                          .in(concatIterable)
      performance of "mutable.HashSet"        in using(gen.map(_.map(size => buildMutableHashSet(0 to size))))                                                .in(concatIterable)
      performance of "mutable.TreeSet"        in using(gen.map(_.map(size => buildMutableTreeSet(0 to size))))                                                .in(concatIterable)

      performance of "HashMap"                in using(gen.map(_.map(size => buildHashMap((0 to size).zipWithIndex))))                                        .in(concatMap)
      performance of "ListMap"                in using(gen.map(_.map(size => buildListMap((0 to size).zipWithIndex))))                                        .in(concatMap)
      performance of "TreeMap"                in using(gen.map(_.map(size => buildTreeMap((0 to size).zipWithIndex))))                                        .in(concatMap)
      performance of "LongMap"                in using(gen.map(_.map(size => buildLongMap((0 to size).zipWithIndex.map(t => t._1.toLong -> t._2)))))          .in(concatMap)

      performance of "Array"                  in using(gen.map(_.map(size => buildArray(0 to size))))                                                         .in(concatArray)
      performance of "List"                   in using(gen.map(_.map(size => buildList(0 to size))))                                                          .in(concatIterable)
      performance of "Vector"                 in using(gen.map(_.map(size => buildVector(0 to size))))                                                        .in(concatIterable)
      performance of "ListBuffer"             in using(gen.map(_.map(size => buildListBuffer(0 to size))))                                                    .in(concatIterable)
      performance of "ArrayBuffer"            in using(gen.map(_.map(size => buildArrayBuffer(0 to size))))                                                   .in(concatIterable)
      performance of "ArraySeq"               in using(gen.map(_.map(size => buildArraySeq(0 to size))))                                                      .in(concatIterable)
      performance of "ArrayStack"             in using(gen.map(_.map(size => buildArrayStack(0 to size))))                                                    .in(concatIterable)

      performance of "HashSet"                in using(gen.map(_.map(size => buildHashSet(0 to size))))                                                       .in(concatIterable)
      performance of "TreeSet"                in using(gen.map(_.map(size => buildTreeSet(0 to size))))                                                       .in(concatIterable)
      performance of "ListSet"                in using(gen.map(_.map(size => buildListSet(0 to size))))                                                       .in(concatIterable)
    }
  }
  bench(sqrtGen)

}
