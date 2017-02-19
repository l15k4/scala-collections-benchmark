
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable.{HashMap, HashSet, IndexedSeq, ListMap, LongMap, TreeMap, TreeSet}

trait AddBench extends CollectionBenchmarkSupport {

  private def addMap[K,V] (empty: scala.collection.Map[K,V],  c: IndexedSeq[(K,V)])     = c.foldLeft(empty)  { case (acc, (k,v))  => acc.updated(k,v) }
  private def addSet      (empty: Set[Int],                   c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => acc + e }

  private def addHashMap        (c: IndexedSeq[(Int,Int)])    = addMap(HashMap.empty[Int,Int], c)
  private def addListMap        (c: IndexedSeq[(Int,Int)])    = addMap(ListMap.empty[Int,Int], c)
  private def addTreeMap        (c: IndexedSeq[(Int,Int)])    = addMap(TreeMap.empty[Int,Int], c)
  private def addLongMap        (c: IndexedSeq[(Int,Int)])    = addMap(LongMap.empty[Int], c.map(t => t._1.toLong -> t._2))
  private def addHashSet        (c: IndexedSeq[Int])          = addSet(HashSet.empty, c)
  private def addTreeSet        (c: IndexedSeq[Int])          = addSet(TreeSet.empty, c)

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by adding elements from C.empty" in {
      performance of "HashMap"  in using(gen.map(_.zipWithIndex)) .in(addHashMap)
      performance of "ListMap"  in using(gen.map(_.zipWithIndex)) .in(addListMap)
      performance of "TreeMap"  in using(gen.map(_.zipWithIndex)) .in(addTreeMap)
      performance of "LongMap"  in using(gen.map(_.zipWithIndex)) .in(addLongMap)
      performance of "HashSet"  in using(gen)                     .in(addHashSet)
      performance of "TreeSet"  in using(gen)                     .in(addTreeSet)
    }
  }

  bench(bigGen)

}
