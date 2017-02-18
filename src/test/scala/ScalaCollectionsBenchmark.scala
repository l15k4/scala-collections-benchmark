import org.scalameter.Bench.OnlineRegressionReport
import org.scalameter.api._
import org.scalameter.{Executor => _, Gen => _, _}

import scala.collection.immutable.{HashSet, LongMap, TreeSet, Vector, _}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

trait ScalaCollectionsBenchmark extends OnlineRegressionReport {
  override def measurer = new Executor.Measurer.Default

  config(
    exec.benchRuns -> 2,
    exec.maxWarmupRuns -> 1,
    exec.independentSamples -> 1,
    exec.requireGC -> false,
    exec.jvmflags -> List("-server", "-Xms1024m", "-Xmx2048m", "-XX:+UseG1GC")
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

}
