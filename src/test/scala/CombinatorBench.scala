
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.IndexedSeq

trait CombinatorBench extends CollectionBenchmarkSupport {
  import CollectionBenchmarkSupport._

  private def bench[C <: IndexedSeq[Int]](gen: Gen[C], fn: Traversable[Int] => Any, fnI: Iterator[Int] => Any, fnArr: Array[Int] => Any, fnM: Traversable[(Int, Int)] => Any, fnMI: Iterator[(Int, Int)] => Any, name: String) = {
    performance of name config(cfg:_*) in {
      performance of "mutable.HashMap"          in using(gen.map(c => buildMutableHashMap(c.zipWithIndex)))         .in(fnM)
      performance of "mutable.ListMap"          in using(gen.map(c => buildMutableListMap(c.zipWithIndex)))         .in(fnM)
      performance of "mutable.TreeMap"          in using(gen.map(c => buildMutableTreeMap(c.zipWithIndex)))         .in(fnM)
      performance of "mutable.LinkedHashMap"    in using(gen.map(c => buildMutableLinkedHashMap(c.zipWithIndex)))   .in(fnM)
      performance of "mutable.WeakHashMap"      in using(gen.map(c => buildMutableWeakHashMap(c.zipWithIndex)))     .in(fnM)
      performance of "mutable.OpenHashMap"      in using(gen.map(c => buildMutableOpenHashMap(c.zipWithIndex)))     .in(fnM)

      performance of "concurrent.TrieMap"       in using(gen.map(c => buildConcurrentTrieMap(c.zipWithIndex)))      .in(fnM)

      performance of "mutable.HashSet"          in using(gen.map(buildMutableHashSet))                              .in(fn)
      performance of "mutable.TreeSet"          in using(gen.map(buildMutableTreeSet))                              .in(fn)
      performance of "mutable.LinkedHashSet"    in using(gen.map(buildMutableLinkedHashSet))                        .in(fn)

      performance of "MutableList"              in using(gen.map(buildMutableList))                                 .in(fn)
      performance of "ListBuffer"               in using(gen.map(buildListBuffer))                                  .in(fn)
      performance of "ArrayBuffer"              in using(gen.map(buildArrayBuffer))                                 .in(fn)
      performance of "ArraySeq"                 in using(gen.map(buildArraySeq))                                    .in(fn)
      performance of "ArrayStack"               in using(gen.map(buildArrayStack))                                  .in(fn)

      performance of "HashMap"                  in using(gen.map(c => buildHashMap(c.zipWithIndex)))                .in(fnM)
      performance of "ListMap"                  in using(gen.map(c => buildListMap(c.zipWithIndex)))                .in(fnM)
      performance of "TreeMap"                  in using(gen.map(c => buildTreeMap(c.zipWithIndex)))                .in(fnM)
      performance of "HashMap-iterator"         in using(gen.map(c => buildHashMap(c.zipWithIndex).iterator))       .in(fnMI)

      performance of "HashSet"                  in using(gen.map(buildHashSet))                                     .in(fn)
      performance of "TreeSet"                  in using(gen.map(buildTreeSet))                                     .in(fn)
      performance of "ListSet"                  in using(gen.map(buildListSet))                                     .in(fn)

      performance of "Array"                    in using(gen.map(buildArray))                                       .in(fnArr)
      performance of "List"                     in using(gen.map(buildList))                                        .in(fn)
      performance of "Vector"                   in using(gen.map(buildVector))                                      .in(fn)

      performance of "HashSet-iterator"         in using(gen.map(buildHashSet(_).iterator))                         .in(fnI)
      performance of "List-iterator"            in using(gen.map(buildList(_).iterator))                            .in(fnI)
      performance of "Vector-iterator"          in using(gen.map(buildVector(_).iterator))                          .in(fnI)
      performance of "Array-iterator"           in using(gen.map(buildArray(_).iterator))                           .in(fnI)
    }
  }

  bench(bigGen,  _.size,                        _.size,                                 _.length,                     _.size,                                 _.size,
    "size"
  )
  bench(bigGen,  _.map(identity),               _.map(identity).emptyForce,             _.map(identity),              _.map(identity),                        _.map(identity).emptyForce,
    "map identity"
  )
  bench(bigGen,  _.filter(_ => true),           _.filter(_ => true).emptyForce,         _.filter(_ => true),          _.filter(_ => true),                    _.filter(_ => true).emptyForce,
    "filter (all true)"
  )
  bench(bigGen,  _.foldLeft(0)(_ - _),          _.foldLeft(0)(_ - _),                   _.foldLeft(0)(_ - _),         _.foldLeft(0)((x, y) => y._1 - y._2),   _.foldLeft(0)((x, y) => y._1 - y._2),
    "foldLeft from 0 with subtraction"
  )
  bench(sqrtGen, c => c.flatMap(_ => c),        c => c.flatMap(_ => c).emptyForce,      c => c.flatMap(_ => c),       c => c.flatMap(_ => c),                 c => c.flatMap(_ => c).emptyForce,
    "flatMap - generate C[Int] of size=sqrt(total) and flatMap itself"
  )
  bench(sqrtGen, c => c.map(_ => c).flatten,    c => c.map(_ => c).flatten.emptyForce,  c => c.map(_ => c).flatten,   c => c.map(_ => c).flatten,             c => c.map(_ => c).flatten.emptyForce,
    "flatten - generate C[Int] of size=sqrt(total) and map itself and flatten"
  )

}
