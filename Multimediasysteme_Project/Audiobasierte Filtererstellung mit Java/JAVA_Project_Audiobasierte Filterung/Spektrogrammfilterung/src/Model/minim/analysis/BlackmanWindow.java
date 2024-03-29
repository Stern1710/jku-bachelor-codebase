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

package Model.minim.analysis; 

/**
 * A Blackman window function.
 *
 * @author Damien Di Fede
 * @author Corban Brook
 * @see   <a href="http://en.wikipedia.org/wiki/Window_function#Blackman_windows">The Blackman Window</a>
 * 
 * @invisible
 */
public class BlackmanWindow extends WindowFunction
{
  protected double alpha;

  /** 
   * Constructs a Blackman window. 
   * 
   * @param alpha
   * 		double: the alpha value to use for this window
   * */
  public BlackmanWindow(double alpha)
  {
    this.alpha = alpha;
  }

  /** Constructs a Blackman window with a default alpha value of 0.16 */
  public BlackmanWindow() 
  {
    this(0.16);
  }

  protected double value(int length, int index) 
  {
      double a0 = (1 - this.alpha) / 2.0;
      double a1 = 0.5;
      double a2 = this.alpha / 2.0;

      return a0 - a1 *  Math.cos(TWO_PI * index / (length - 1)) + a2 * (float) Math.cos(4 * Math.PI * index / (length - 1));
  }
  
  public String toString()
  {
	  return "Blackman Window";
  }
}

