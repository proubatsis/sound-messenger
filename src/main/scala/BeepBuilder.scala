case class BeepBuilder(frequency : Int, seconds : Float, samplingRate : Float = 44100, amplitude : Int = 128, bytes : Array[Byte] = Array()) {
  def addBeep(frequency : Int, seconds : Float) : BeepBuilder =
    BeepBuilder(frequency, seconds, samplingRate, amplitude, build)

  lazy val build : Array[Byte] =
    bytes ++ (1 to (samplingRate * seconds).toInt toArray)
      .map(n => Math.sin(2 * Math.PI * frequency * (n / samplingRate.toFloat)))
      .map(d => (d * amplitude).toByte)
}
