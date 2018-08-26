object Main {
  def main(args : Array[String]) : Unit = {
    //    val transmitter = new Transmitter with JavaSoundPlayer
    //    transmitter.transmit(List(0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x77, 0x6f, 0x72, 0x6c, 0x64, 0x21), secondsPerByte = 0.1f, step = 1000, low = 1000)

    val receiver = new Receiver with JavaSoundRecorder with JTransformSpectrumTransformer with SamplingRate
    val ctx = BucketContext()

    receiver.start()

    var maxFreq = -1.0
    var frequencies = List.empty[Double]
    while (maxFreq < 9000) {
      maxFreq = receiver.receiveFrequency()
      frequencies ++= List(maxFreq)
    }

    receiver.close()
    val recording = frequencies map (f => ctx.buckets find (_.isFreqInBucket(f.toInt))) collect {
      case Some(bucket) => bucket
    } sliding 2 collect { case Seq(a, b) if a != b => b } toList

    print(FrequencyDecoder.decode(recording, FrequencyBucket.createWithAbsVariation(1000, 50), FrequencyBucket.createWithAbsVariation(10000, 50), ctx.freqToNibble))
  }
}
