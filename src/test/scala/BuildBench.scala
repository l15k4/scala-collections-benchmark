
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive

trait BuildBench extends CollectionBenchmarkSupport {

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by C.newBuilder" config(cfg:_*) in {
      performance of "mutable.HashMap"        in using(gen.map(_.zipWithIndex))                                 .in(buildMutableHashMap)
      performance of "mutable.ListMap"        in using(gen.map(_.zipWithIndex))                                 .in(buildMutableListMap)
      performance of "mutable.TreeMap"        in using(gen.map(_.zipWithIndex))                                 .in(buildMutableTreeMap)
      performance of "mutable.LongMap"        in using(gen.map(_.zipWithIndex.map(t => t._1.toLong -> t._2)))   .in(buildMutableLongMap)
      performance of "mutable.LinkedHashMap"  in using(gen.map(_.zipWithIndex))                                 .in(buildMutableLinkedHashMap)
      performance of "mutable.WeakHashMap"    in using(gen.map(_.zipWithIndex))                                 .in(buildMutableWeakHashMap)
      performance of "mutable.OpenHashMap"    in using(gen.map(_.zipWithIndex))                                 .in(buildMutableOpenHashMap)

      performance of "concurrent.TrieMap"     in using(gen.map(_.zipWithIndex))                                 .in(buildConcurrentTrieMap)

      performance of "mutable.LinkedHashSet"  in using(gen)                                                     .in(buildMutableLinkedHashSet)
      performance of "mutable.HashSet"        in using(gen)                                                     .in(buildMutableHashSet)
      performance of "mutable.TreeSet"        in using(gen)                                                     .in(buildMutableTreeSet)

      performance of "mutable.MutableList"    in using(gen)                                                     .in(buildMutableList)
      performance of "ListBuffer"             in using(gen)                                                     .in(buildListBuffer)
      performance of "ArrayBuffer"            in using(gen)                                                     .in(buildArrayBuffer)

      performance of "HashMap"                in using(gen.map(_.zipWithIndex))                                 .in(buildHashMap)
      performance of "ListMap"                in using(gen.map(_.zipWithIndex))                                 .in(buildListMap)
      performance of "TreeMap"                in using(gen.map(_.zipWithIndex))                                 .in(buildTreeMap)
      performance of "LongMap"                in using(gen.map(_.zipWithIndex.map(t => t._1.toLong -> t._2)))   .in(buildLongMap)

      performance of "HashSet"                in using(gen)                                                     .in(buildHashSet)
      performance of "TreeSet"                in using(gen)                                                     .in(buildTreeSet)
      performance of "ListSet"                in using(gen)                                                     .in(buildListSet)

      performance of "Array"                  in using(gen)                                                     .in(buildArray)
      performance of "List"                   in using(gen)                                                     .in(buildList)
      performance of "Vector"                 in using(gen)                                                     .in(buildVector)
    }
  }

  bench(bigGen)

}
