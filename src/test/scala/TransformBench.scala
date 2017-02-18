
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.IndexedSeq

object TransformBench extends ScalaCollectionsBenchmark {

  def transformBench[C <: IndexedSeq[Int]](gen: Gen[C], fn: Traversable[Int] => Any, fnI: Iterator[Int] => Any, fnArr: Array[Int] => Any, fnM: Traversable[(Int, Int)] => Any,fnMI: Iterator[(Int, Int)] => Any, name: String) = {
    performance of name in {
      performance of "HashMap"          in using(gen.map(c => buildHashMap(c.zipWithIndex)))                .in(fnM)
      performance of "ListMap"          in using(gen.map(c => buildListMap(c.zipWithIndex)))                .in(fnM)
      performance of "TreeMap"          in using(gen.map(c => buildTreeMap(c.zipWithIndex)))                .in(fnM)
      performance of "HashMap-iterator" in using(gen.map(c => buildHashMap(c.zipWithIndex).iterator))       .in(fnMI)
      performance of "Array"            in using(gen.map(buildArray))                                       .in(fnArr)
      performance of "HashSet"          in using(gen.map(buildHashSet))                                     .in(fn)
      performance of "TreeSet"          in using(gen.map(buildTreeSet))                                     .in(fn)
      performance of "List"             in using(gen.map(buildList))                                        .in(fn)
      performance of "Vector"           in using(gen.map(buildVector))                                      .in(fn)
      performance of "ListBuffer"       in using(gen.map(buildListBuffer))                                  .in(fn)
      performance of "ArrayBuffer"      in using(gen.map(buildArrayBuffer))                                 .in(fn)
      performance of "HashSet-iterator" in using(gen.map(buildHashSet(_).iterator))                         .in(fnI)
      performance of "List-iterator"    in using(gen.map(buildList(_).iterator))                            .in(fnI)
      performance of "Vector-iterator"  in using(gen.map(buildVector(_).iterator))                          .in(fnI)
      performance of "Array-iterator"   in using(gen.map(buildArray(_).iterator))                           .in(fnI)
    }
  }

  transformBench(bigGen,  _.size,                       _.size,                       _.length,                     _.size,                                 _.size,
    "size"
  )
  transformBench(bigGen,  _.map(identity),              _.map(identity),              _.map(identity),              _.map(identity),                        _.map(identity),
    "map identity"
  )
  transformBench(bigGen,  _.filter(_ => true),          _.filter(_ => true),          _.filter(_ => true),          _.filter(_ => true),                    _.filter(_ => true),
    "filter (all true)"
  )
  transformBench(bigGen,  _.partition(_ % 2 == 0),      _.partition(_ % 2 == 0),      _.partition(_ % 2 == 0),      _.partition(_._2 % 2 == 0),             _.partition(_._2 % 2 == 0),
    "partition (50/50)"
  )
  transformBench(bigGen,  _.foldLeft(0)(_ - _),         _.foldLeft(0)(_ - _),         _.foldLeft(0)(_ - _),         _.foldLeft(0)((x, y) => y._1 - y._2),   _.foldLeft(0)((x, y) => y._1 - y._2),
    "foldLeft from 0 with subtraction"
  )
  transformBench(sqrtGen,  c => c.flatMap(_ => c),       c => c.flatMap(_ => c),       c => c.flatMap(_ => c),       c => c.flatMap(_ => c),                 c => c.flatMap(_ => c),
    "flatMap - generate C[Int] of size=sqrt(total) and flatMap itself"
  )
  transformBench(sqrtGen,  c => c.map(_ => c).flatten,   c => c.map(_ => c).flatten,   c => c.map(_ => c).flatten,   c => c.map(_ => c).flatten,             c => c.map(_ => c).flatten,
    "flatten - generate C[Int] of size=sqrt(total) and map itself and flatten"
  )

}
