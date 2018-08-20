import javax.sound.sampled.{AudioFormat, AudioSystem, DataLine, TargetDataLine}

trait JavaSoundRecorder extends SoundRecorder {
  this: SamplingRate =>
  private val audioFormat = new AudioFormat(samplingRate, 8, 1, true, true)
  private val targetInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
  private val microphone = AudioSystem.getLine(targetInfo) match {
    case tdl: TargetDataLine => tdl
    case _ => throw new Exception("Failed to cast to target data line")
  }

  def start(): Unit = {
    microphone.open(audioFormat)
    microphone.start()
  }

  def close(): Unit = {
    microphone.close()
  }

  override def record(): Array[Byte] = {
    val current = Array.ofDim[Byte](microphone.getBufferSize / 5)
    val bytesRead = microphone.read(current, 0, 1024)

    if (bytesRead < 0) {
      Array()
    } else {
      current.take(bytesRead)
    }
  }
}
