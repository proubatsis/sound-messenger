trait Receiver {
  this: SoundRecorder with SpectrumTransformer with SamplingRate =>
  def receiveFrequency(): Double = {
    val bytes = record()
    val spectrum = fft(bytes)
    val maxIndex = spectrum.zipWithIndex.maxBy(_._1)._2
    (maxIndex / spectrum.length.toDouble) * samplingRate
  }
}
