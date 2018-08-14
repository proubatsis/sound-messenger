import javax.sound.sampled._

object Main {
  def main(args : Array[String]) : Unit = {
//    val transmitter = new Transmitter with JavaSoundPlayer
//    transmitter.transmit(List(0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x77, 0x6f, 0x72, 0x6c, 0x64, 0x21), secondsPerByte = 0.1f, step = 1000, low = 1000)
    val audioFormat = new AudioFormat(44100, 8, 1, true, true)
    val targetInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
    val microphone = AudioSystem.getLine(targetInfo) match {
      case tdl: TargetDataLine => tdl
      case _ => throw new Exception("Failed to cast to target data line")
    }

    val endTime = System.currentTimeMillis() + 5000
    microphone.open(audioFormat)
    microphone.start()
    val bytes = record(microphone, endTime)
    microphone.close()

    val player = new JavaSoundPlayer {}
    player.play(bytes, 44100)
  }

  private def record(microphone : TargetDataLine, endTime : Long): Array[Byte] = {
    val current = Array.ofDim[Byte](microphone.getBufferSize / 5)
    val bytesRead = microphone.read(current, 0, 1024)

    if (bytesRead < 0) {
      Array()
    } else if (System.currentTimeMillis < endTime) {
      current.take(bytesRead) ++ record(microphone, endTime)
    } else {
      current.take(bytesRead)
    }
  }
}
