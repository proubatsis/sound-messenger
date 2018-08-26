import Ordering.Implicits._

case class FrequencyBucket(low : Int, hi : Int) {
  def isFreqInBucket(freq : Int) : Boolean = low < freq && freq < hi
}

object FrequencyBucket {
  def createWithAbsVariation(value : Int, variation : Int) : FrequencyBucket = FrequencyBucket(value - variation, value + variation)
  def createBuckets(startingValue : Int, step : Int, variation : Int, repetitions : Int) : List[FrequencyBucket] =
    if (repetitions > 0) List(createWithAbsVariation(startingValue, variation)) ++ createBuckets(startingValue + step, step, variation, repetitions - 1) else List()
}
