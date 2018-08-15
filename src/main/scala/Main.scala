import javax.sound.sampled._
import org.jtransforms.fft.DoubleFFT_1D

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

//    val player = new JavaSoundPlayer {}
//    player.play(bytes, 44100)
    val maxIndexes = bytes map fft map maxIndex
    print(maxIndexes.mkString(", "))
  }

  private def record(microphone : TargetDataLine, endTime : Long): Array[Array[Byte]] = {
    val current = Array.ofDim[Byte](microphone.getBufferSize / 5)
    val bytesRead = microphone.read(current, 0, 1024)

    if (bytesRead < 0) {
      Array(Array())
    } else if (System.currentTimeMillis < endTime) {
      Array(current.take(bytesRead)) ++ record(microphone, endTime)
    } else {
      Array(current.take(bytesRead))
    }
  }

  private def fft(bytes : Array[Byte]) : Array[Double] = {
    val doubles = bytes map (_.toDouble)
    val fft = new DoubleFFT_1D(doubles.length)
    val spectrum = Array.ofDim[Double](doubles.length * 2)
    Array.copy(doubles, 0, spectrum, 0, doubles.length)
    fft.realForwardFull(spectrum)
    val reals = spectrum.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
    val immaginaries = spectrum.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)
    reals.zip(immaginaries).map({ case (r, i) => Math.sqrt(r * r + i * i) })
  }

  private def maxIndex(spectrum : Array[Double]) : Int = {
    spectrum.zipWithIndex.maxBy(_._1)._2
  }
}
