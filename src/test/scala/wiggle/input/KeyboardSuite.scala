//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.input

import org.scalatest.Suite

/**
 * Unit tests for the Keyboard.
 */
class KeyboardSuite extends Suite
{
  def testKeyboard () {
    val kbd = new Keyboard(false)

    kbd.key(Keyboard.KEY_F1).addOnPress((key) => { println("F1! " + key); false })

    println(kbd.key(Keyboard.KEY_F1))
    println(kbd.key(Keyboard.KEY_F1))
    println(kbd.key(Keyboard.KEY_F1))
  }
}
