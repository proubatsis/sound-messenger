object Main {
  def main(args : Array[String]) : Unit = {
    val transmitter = new Transmitter with JavaSoundPlayer
    transmitter.transmit(List(0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x77, 0x6f, 0x72, 0x6c, 0x64, 0x21), secondsPerByte = 0.1f, step = 1000, low = 1000)
  }
}
