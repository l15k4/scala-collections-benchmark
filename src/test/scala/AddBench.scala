
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable._
import scala.collection.mutable

trait AddBench extends CollectionBenchmarkSupport {

  private def addMap[K,V]               (empty: Map[K,V],         c: IndexedSeq[(K,V)])     = c.foldLeft(empty)  { case (acc, (k,v))  => acc.updated(k,v) }
  private def addMutableMap[K,V]        (empty: mutable.Map[K,V], c: IndexedSeq[(K,V)])     = c.foldLeft(empty)  { case (acc, (k,v))  => acc += (k -> v) }

  private def addSet                    (empty: Set[Int],         c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => acc + e }
  private def addMutableSet             (empty: mutable.Set[Int], c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => acc += e }

  private def addMutableHashMap         (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.HashMap.empty[Int,Int], c)
  private def addMutableListMap         (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.ListMap.empty[Int,Int], c)
  private def addMutableTreeMap         (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.TreeMap.empty[Int,Int], c)
  private def addMutableLongMap         (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.LongMap.empty[Int], c.map(t => t._1.toLong -> t._2))
  private def addMutableLinkedHashMap   (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.LinkedHashMap.empty[Int,Int], c)
  private def addMutableWeakHashMap     (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.WeakHashMap.empty[Int,Int], c)
  private def addMutableOpenHashMap     (c: IndexedSeq[(Int,Int)])    = addMutableMap(mutable.OpenHashMap.empty[Int,Int], c)

  private def addConcurrentTrieMap      (c: IndexedSeq[(Int,Int)])    = addMutableMap(TrieMap.empty[Int,Int], c)

  private def addMutableLinkedHashSet   (c: IndexedSeq[Int])          = addMutableSet(mutable.LinkedHashSet.empty[Int], c)
  private def addMutableHashSet         (c: IndexedSeq[Int])          = addMutableSet(mutable.HashSet.empty[Int], c)
  private def addMutableTreeSet         (c: IndexedSeq[Int])          = addMutableSet(mutable.TreeSet.empty[Int], c)

  private def addHashMap                (c: IndexedSeq[(Int,Int)])    = addMap(HashMap.empty[Int,Int], c)
  private def addListMap                (c: IndexedSeq[(Int,Int)])    = addMap(ListMap.empty[Int,Int], c)
  private def addTreeMap                (c: IndexedSeq[(Int,Int)])    = addMap(TreeMap.empty[Int,Int], c)
  private def addLongMap                (c: IndexedSeq[(Int,Int)])    = addMap(LongMap.empty[Int], c.map(t => t._1.toLong -> t._2))

  private def addHashSet                (c: IndexedSeq[Int])          = addSet(HashSet.empty[Int], c)
  private def addTreeSet                (c: IndexedSeq[Int])          = addSet(TreeSet.empty[Int], c)
  private def addListSet                (c: IndexedSeq[Int])          = addSet(ListSet.empty[Int], c)

  private def bench(gen: Gen[Inclusive]) = {
    performance of "Build by adding elements from C.empty" config(cfg:_*) in {
      performance of "mutable.HashMap"        in using(gen.map(_.zipWithIndex)) .in(addMutableHashMap)
      performance of "mutable.ListMap"        in using(gen.map(_.zipWithIndex)) .in(addMutableListMap)
      performance of "mutable.TreeMap"        in using(gen.map(_.zipWithIndex)) .in(addMutableTreeMap)
      performance of "mutable.LongMap"        in using(gen.map(_.zipWithIndex)) .in(addMutableLongMap)
      performance of "mutable.LinkedHashMap"  in using(gen.map(_.zipWithIndex)) .in(addMutableLinkedHashMap)
      performance of "mutable.WeakHashMap"    in using(gen.map(_.zipWithIndex)) .in(addMutableWeakHashMap)
      performance of "mutable.OpenHashMap"    in using(gen.map(_.zipWithIndex)) .in(addMutableOpenHashMap)

      performance of "concurrent.TrieMap"     in using(gen.map(_.zipWithIndex)) .in(addConcurrentTrieMap)

      performance of "mutable.LinkedHashSet"  in using(gen)                     .in(addMutableLinkedHashSet)
      performance of "mutable.HashSet"        in using(gen)                     .in(addMutableHashSet)
      performance of "mutable.TreeSet"        in using(gen)                     .in(addMutableTreeSet)

      performance of "HashMap"                in using(gen.map(_.zipWithIndex)) .in(addHashMap)
      performance of "ListMap"                in using(gen.map(_.zipWithIndex)) .in(addListMap)
      performance of "TreeMap"                in using(gen.map(_.zipWithIndex)) .in(addTreeMap)
      performance of "LongMap"                in using(gen.map(_.zipWithIndex)) .in(addLongMap)

      performance of "HashSet"                in using(gen)                     .in(addHashSet)
      performance of "TreeSet"                in using(gen)                     .in(addTreeSet)
      performance of "ListSet"                in using(gen)                     .in(addListSet)
    }
  }

  bench(bigGen)

}
