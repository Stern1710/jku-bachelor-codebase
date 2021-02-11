/*
 *  Copyright (c) 2007 - 2008 by Damien Di Fede <ddf@compartmental.net>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package Model.minim; 

/**
 * <code>MAudioBuffer</code> encapsulates a sample buffer of doubles. All Minim
 * classes that give you access to audio samples do so with an
 * <code>MAudioBuffer</code>. The underlying array is not immutable and this
 * class has a number of methods for reading and writing to that array. It is
 * even possible to be given a direct handle on the array to process it as you
 * wish.
 * 
 * @author Damien Di Fede
 * 
 */

public final class MAudioBuffer implements AudioBuffer
{
  private double[] samples;

  /**
   * Constructs and MAudioBuffer that is <code>bufferSize</code> samples long.
   * 
   * @param bufferSize
   *          the size of the buffer
   */
  public MAudioBuffer(int bufferSize)
  {
    samples = new double[bufferSize];
  }

  public synchronized int size()
  {
    return samples.length;
  }

  public synchronized double get(int i)
  {
    return samples[i];
  }
  
  public synchronized double get(double i)
  {
	  int lowSamp = (int)i;
	  int hiSamp = lowSamp + 1;
	  if ( hiSamp == samples.length )
	  {
		  return samples[lowSamp];
	  }
	  double lerp = i - lowSamp;
	  return samples[lowSamp] + lerp*(samples[hiSamp] - samples[lowSamp]);
  }

  public synchronized void set(double[] buffer)
  {
    if (buffer.length != samples.length)
      Minim
          .error("MAudioBuffer.set: passed array (" + buffer.length + ") " + 
              "must be the same length (" + samples.length + ") as this MAudioBuffer.");
    else
      samples = buffer;
  }

  /**
   * Mixes the two double arrays and puts the result in this buffer. The
   * passed arrays must be the same length as this buffer. If they are not, an
   * error will be reported and nothing will be done. The mixing function is:
   * <p>
   * <code>samples[i] = (b1[i] + b2[i]) / 2</code>
   * 
   * @param b1
   *          the first buffer
   * @param b2
   *          the second buffer
   */
  public synchronized void mix(double[] b1, double[] b2)
  {
    if ((b1.length != b2.length)
        || (b1.length != samples.length || b2.length != samples.length))
    {
      Minim.error("MAudioBuffer.mix: The two passed buffers must be the same size as this MAudioBuffer.");
    }
    else
    {
      for (int i = 0; i < samples.length; i++)
      {
        samples[i] = (b1[i] + b2[i]) / 2;
      }
    }
  }

  /**
   * Sets all of the values in this buffer to zero.
   */
  public synchronized void clear()
  {
    samples = new double[samples.length];
  }
  
  public synchronized double level()
  {
    double level = 0;
    for (int i = 0; i < samples.length; i++)
    {
      level += (samples[i] * samples[i]);
    }
    level /= samples.length;
    level =  Math.sqrt(level);
    return level;
  }

  public synchronized double[] toArray()
  {
    double[] ret = new double[samples.length];
    System.arraycopy(samples, 0, ret, 0, samples.length);
    return ret;
  }
}
