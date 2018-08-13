object Main {
  def main(args : Array[String]) : Unit = {
    val high = 1500
    val low = 1000
    val seconds = 0.25f
    val iterations = 8
    val beepBuilder = List.fill(iterations)(List(high, low))
      .flatten
      .foldLeft(BeepBuilder(low, seconds))((a, b) => a.addBeep(b, seconds))

    val samplingRate = beepBuilder.samplingRate
    val bytes = beepBuilder.build
    JavaSoundPlayer.play(bytes, samplingRate)
  }
}
