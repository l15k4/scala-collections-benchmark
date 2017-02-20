
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive

trait BuildBench extends CollectionBenchmarkSupport {

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by C.newBuilder" config(cfg:_*) in {
      performance of "HashMap"          in using(gen.map(_.zipWithIndex))                                 .in(buildHashMap)
      performance of "ListMap"          in using(gen.map(_.zipWithIndex))                                 .in(buildListMap)
      performance of "TreeMap"          in using(gen.map(_.zipWithIndex))                                 .in(buildTreeMap)
      performance of "LongMap"          in using(gen.map(_.zipWithIndex.map(t => t._1.toLong -> t._2)))   .in(buildLongMap)
      performance of "Array"            in using(gen)                                                     .in(buildArray)
      performance of "HashSet"          in using(gen)                                                     .in(buildHashSet)
      performance of "TreeSet"          in using(gen)                                                     .in(buildTreeSet)
      performance of "List"             in using(gen)                                                     .in(buildList)
      performance of "Vector"           in using(gen)                                                     .in(buildVector)
      performance of "ListBuffer"       in using(gen)                                                     .in(buildListBuffer)
      performance of "ArrayBuffer"      in using(gen)                                                     .in(buildArrayBuffer)
    }
  }

  bench(bigGen)

}
