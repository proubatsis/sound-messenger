import org.jtransforms.fft.DoubleFFT_1D

trait JTransformSpectrumTransformer extends SpectrumTransformer {
  override def fft(bytes : Array[Byte]) : Array[Double] = {
    val doubles = bytes map (_.toDouble)
    val fft = new DoubleFFT_1D(doubles.length)
    val spectrum = Array.ofDim[Double](doubles.length * 2)
    Array.copy(doubles, 0, spectrum, 0, doubles.length)
    fft.realForwardFull(spectrum)
    val reals = spectrum.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
    val immaginaries = spectrum.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)
    reals.zip(immaginaries).map({ case (r, i) => Math.sqrt(r * r + i * i) })
  }
}
