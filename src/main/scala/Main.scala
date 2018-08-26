import java.nio.charset.StandardCharsets

object Main {
  def main(args : Array[String]) : Unit = {
    //    val transmitter = new Transmitter with JavaSoundPlayer
    //    transmitter.transmit(List(0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x77, 0x6f, 0x72, 0x6c, 0x64, 0x21), secondsPerByte = 0.1f, step = 1000, low = 1000)

    val receiver = new Receiver with JavaSoundRecorder with JTransformSpectrumTransformer with SamplingRate
    val ctx = BucketContext(startingValue = 3000)

    receiver.start()

    var maxFreq = -1.0
    var frequencies = List.empty[Double]
    while (maxFreq < (ctx.endValue - 1000)) {
      maxFreq = receiver.receiveFrequency()
      frequencies ++= List(maxFreq)
    }

    receiver.close()
    val recording = frequencies map (f => ctx.buckets find (_.isFreqInBucket(f.toInt))) collect {
      case Some(bucket) => bucket
    } sliding 2 collect { case Seq(a, b) if a != b => b } toList


    val decoded = FrequencyDecoder.decode(recording, FrequencyBucket.createWithAbsVariation(ctx.startingValue, ctx.variation), FrequencyBucket.createWithAbsVariation(ctx.endValue, ctx.variation), ctx.freqToNibble)
    println(new String(decoded.toArray, StandardCharsets.UTF_8))
  }
}
