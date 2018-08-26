trait Transmitter {
  this: SoundPlayer =>
  def transmit(bytes : List[Byte], secondsPerByte : Float = 1.0f, low : Int = 1000, step : Int = 250, endValue : Int = 15000): Unit = {
    val beepBuilder =
      bytes
        .flatMap(frequenciesFromByte(_, low, step))
        .foldLeft(BeepBuilder(low, secondsPerByte * 2))((a, b) => a.addBeep(b, secondsPerByte / 4))

    val samplingRate = beepBuilder.samplingRate
    val soundBytes = beepBuilder.addBeep(15000, secondsPerByte * 2).build
    play(soundBytes, samplingRate)
  }

  private def frequenciesFromByte(byte: Byte, low : Int, step : Int) : List[Int] = {
    val firstNibble = (byte & 0xF0) >> 4
    val secondNibble = byte & 0x0F
    List((firstNibble + 1) * step + low, low, (secondNibble + 1) * step + low, low)
  }
}
