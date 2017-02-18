import org.scalameter.Bench.OnlineRegressionReport
import org.scalameter.api._
import org.scalameter.api.Gen
import org.scalameter.{Bench => _, Executor => _, _}
import scala.collection.immutable.Range.Inclusive
import scala.collection.immutable.{HashMap, TreeMap, ListMap, HashSet, TreeSet, Vector, List, IndexedSeq, LongMap}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object ScalaCollectionsBenchmark extends OnlineRegressionReport {
  override def measurer = new Executor.Measurer.Default

  config(
    exec.benchRuns -> 2,
    exec.maxWarmupRuns -> 1,
    exec.independentSamples -> 1,
    exec.requireGC -> false,
    exec.jvmflags -> List("-server", "-Xms1024m", "-Xmx8192m", "-XX:+UseG1GC")
  )

  val bigGen    = Gen.range("size")(16, 9016,  3000).map(size => 0 to size )
  val sqrtGen   = bigGen.map(_.map(math.sqrt(_).toInt))

  def buildHashMap      (c: IndexedSeq[(Int,Int)])    = HashMap.newBuilder[Int,Int] .++=(c).result()
  def buildListMap      (c: IndexedSeq[(Int,Int)])    = ListMap.newBuilder[Int,Int] .++=(c).result()
  def buildTreeMap      (c: IndexedSeq[(Int,Int)])    = TreeMap.newBuilder[Int,Int] .++=(c).result()
  def buildLongMap      (c: IndexedSeq[(Long,Int)])   = LongMap(c:_*)
  def buildArray        (c: IndexedSeq[Int])          = Array.newBuilder[Int]       .++=(c).result()
  def buildHashSet      (c: IndexedSeq[Int])          = HashSet.newBuilder[Int]     .++=(c).result()
  def buildTreeSet      (c: IndexedSeq[Int])          = TreeSet.newBuilder[Int]     .++=(c).result()
  def buildList         (c: IndexedSeq[Int])          = List.newBuilder[Int]        .++=(c).result()
  def buildVector       (c: IndexedSeq[Int])          = Vector.newBuilder[Int]      .++=(c).result()
  def buildListBuffer   (c: IndexedSeq[Int])          = ListBuffer.newBuilder[Int]  .++=(c).result()
  def buildArrayBuffer  (c: IndexedSeq[Int])          = ArrayBuffer.newBuilder[Int] .++=(c).result()

  def addMap[K,V] (empty: scala.collection.Map[K,V],  c: IndexedSeq[(K,V)])     = c.foldLeft(empty)  { case (acc, (k,v))  => acc.updated(k,v) }
  def addSet      (empty: Set[Int],                   c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => acc + e }
  def appendSeq   (empty: Seq[Int],                   c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => acc :+ e }
  def prependSeq  (empty: Seq[Int],                   c: IndexedSeq[Int])       = c.foldLeft(empty)  { case (acc, e)      => e +: acc }

  def concatMap       (c: Seq[scala.collection.Map[_, _]]) = c.reduce(_ ++ _)
  def concatIterable  (c: Seq[Iterable[Int]]) = c.reduce(_ ++ _)
  def concatList      (c: Seq[List[Int]]) = c.reduce(_ ::: _)
  def concatArray     (c: Seq[Array[Int]]) = c.reduce(_ ++ _)

  def addHashMap        (c: IndexedSeq[(Int,Int)])    = addMap(HashMap.empty[Int,Int], c)
  def addListMap        (c: IndexedSeq[(Int,Int)])    = addMap(ListMap.empty[Int,Int], c)
  def addTreeMap        (c: IndexedSeq[(Int,Int)])    = addMap(TreeMap.empty[Int,Int], c)
  def addLongMap        (c: IndexedSeq[(Int,Int)])    = addMap(LongMap.empty[Int], c.map(t => t._1.toLong -> t._2))
  def addHashSet        (c: IndexedSeq[Int])          = addSet(HashSet.empty, c)
  def addTreeSet        (c: IndexedSeq[Int])          = addSet(TreeSet.empty, c)

  def appendList        (c: IndexedSeq[Int])          = appendSeq(List.empty[Int], c)
  def appendVector      (c: IndexedSeq[Int])          = appendSeq(Vector.empty[Int], c)
  def appendListBuffer  (c: IndexedSeq[Int])          = appendSeq(ListBuffer.empty[Int], c)
  def appendArrayBuffer (c: IndexedSeq[Int])          = appendSeq(ArrayBuffer.empty[Int], c)
  def appendArray       (c: IndexedSeq[Int])          = c.foldLeft(Array.empty[Int]) { case (acc, e) => acc :+ e }

  def prependList       (c: IndexedSeq[Int])          = prependSeq(List.empty[Int], c)
  def prependVector     (c: IndexedSeq[Int])          = prependSeq(Vector.empty[Int], c)
  def prependListBuffer (c: IndexedSeq[Int])          = prependSeq(ListBuffer.empty[Int], c)
  def prependArrayBuffer(c: IndexedSeq[Int])          = prependSeq(ArrayBuffer.empty[Int], c)
  def prependArray      (c: IndexedSeq[Int])          = c.foldLeft(Array.empty[Int]) { case (acc, e) => e +: acc }

  def buildBench(gen: Gen[Inclusive]) = {
    performance of "Build by C.newBuilder" in {
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

  def addBench(gen: Gen[Inclusive]) = {
    performance of "Build by adding elements from C.empty" in {
      performance of "HashMap"          in using(gen.map(_.zipWithIndex)) .in(addHashMap)
      performance of "ListMap"          in using(gen.map(_.zipWithIndex)) .in(addListMap)
      performance of "TreeMap"          in using(gen.map(_.zipWithIndex)) .in(addTreeMap)
      performance of "LongMap"          in using(gen.map(_.zipWithIndex)) .in(addLongMap)
      performance of "HashSet"          in using(gen)                     .in(addHashSet)
      performance of "TreeSet"          in using(gen)                     .in(addTreeSet)
    }
  }

  def concatBench(gen: Gen[IndexedSeq[Int]]) = {
    performance of "Build by concatenating C.++ (total C.size equals to other benchmarks)" in {
      performance of "HashMap"          in using(gen.map(_.map(size => buildHashMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "ListMap"          in using(gen.map(_.map(size => buildListMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "TreeMap"          in using(gen.map(_.map(size => buildTreeMap((0 to size).zipWithIndex))))                                .in(concatMap)
      performance of "LongMap"          in using(gen.map(_.map(size => buildLongMap((0 to size).zipWithIndex.map(t => t._1.toLong -> t._2)))))  .in(concatMap)
      performance of "HashSet"          in using(gen.map(_.map(size => buildHashSet(0 to size))))                                               .in(concatIterable)
      performance of "TreeSet"          in using(gen.map(_.map(size => buildTreeSet(0 to size))))                                               .in(concatIterable)

      performance of "Array"            in using(gen.map(_.map(size => buildArray(0 to size))))         .in(concatArray)
      performance of "List-append"      in using(gen.map(_.map(size => buildList(0 to size))))          .in(concatIterable)
      performance of "List-prepend"     in using(gen.map(_.map(size => buildList(0 to size))))          .in(concatList)
      performance of "Vector"           in using(gen.map(_.map(size => buildVector(0 to size))))        .in(concatIterable)
      performance of "ListBuffer"       in using(gen.map(_.map(size => buildListBuffer(0 to size))))    .in(concatIterable)
      performance of "ArrayBuffer"      in using(gen.map(_.map(size => buildArrayBuffer(0 to size))))   .in(concatIterable)
    }
  }

  def appendBench(gen: Gen[Inclusive]) = {
    performance of "Build by appending to C.empty" in {
      performance of "Array"            in using(gen)                     .in(appendArray)
      performance of "List"             in using(gen)                     .in(appendList)
      performance of "Vector"           in using(gen)                     .in(appendVector)
      performance of "ListBuffer"       in using(gen)                     .in(appendListBuffer)
      performance of "ArrayBuffer"      in using(gen)                     .in(appendArrayBuffer)
    }
  }

  def prependBench(gen: Gen[Inclusive]) = {
    performance of "Build by prepending to C.empty" in {
      performance of "Array"            in using(gen)                     .in(prependArray)
      performance of "List"             in using(gen)                     .in(prependList)
      performance of "Vector"           in using(gen)                     .in(prependVector)
      performance of "ListBuffer"       in using(gen)                     .in(prependListBuffer)
      performance of "ArrayBuffer"      in using(gen)                     .in(prependArrayBuffer)
    }
  }

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

  buildBench(bigGen)
  addBench(bigGen)
  appendBench(bigGen)
  prependBench(bigGen)
  concatBench(sqrtGen)

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
