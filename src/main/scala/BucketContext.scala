case class BucketContext(startingValue : Int = 1000, step : Int = 250, variation : Int = 50, endValue : Int = 10000) {
  lazy val buckets : List[FrequencyBucket] = FrequencyBucket.createBuckets(startingValue, step, variation, 1000)
  lazy val freqToNibble : Map[FrequencyBucket, Byte] =
    FrequencyBucket.createBuckets(startingValue + step, step, variation, 16)
    .zip(0 to 15 map (_.toByte))
    .toMap
  lazy val lowBucket : FrequencyBucket = FrequencyBucket.createWithAbsVariation(startingValue, variation)
  lazy val endBucket : FrequencyBucket = FrequencyBucket.createWithAbsVariation(endValue, variation)
}
