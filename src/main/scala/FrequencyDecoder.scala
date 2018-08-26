object FrequencyDecoder {
  def decode(frequencies : List[FrequencyBucket], low : FrequencyBucket, hi : FrequencyBucket, freqToNibble : Map[FrequencyBucket, Byte]) : List[Byte] = {
    frequencies match {
      case a::FrequencyBucket(low.low, low.hi)::b::FrequencyBucket(low.low, low.hi)::xs =>
        (freqToNibble.get(a), freqToNibble.get(b)) match {
          case (Some(upper), Some(lower)) => List[Byte](((upper << 4) | lower).toByte) ++ decode(xs, low, hi, freqToNibble)
          case _ => decode(xs, low, hi, freqToNibble)
        }
      case FrequencyBucket(hi.low, hi.hi)::_ => List.empty[Byte]
      case _ => List.empty[Byte]
    }
  }
}
