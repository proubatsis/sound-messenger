import javax.sound.sampled.{AudioFormat, AudioSystem}

trait JavaSoundPlayer extends SoundPlayer {
  override def play(bytes: Array[Byte], samplingRate: Float): Unit = {
    val audioFormat = new AudioFormat(samplingRate.toFloat, 8, 1, true, false)
    val sourceDataLine = AudioSystem.getSourceDataLine(audioFormat)
    sourceDataLine.open()
    sourceDataLine.start()
    sourceDataLine.write(bytes, 0, bytes.length)
    sourceDataLine.drain()
    sourceDataLine.stop()
  }
}
