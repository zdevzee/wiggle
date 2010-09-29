//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.gfx

import org.scalatest.Suite

class ColorSuite extends Suite
{
  def testColor () {
    expect(0xFFFFFF) { Color.from(255, 255, 255, 0).toARGB }
    expect(0xFFFFFFFF) { Color.from(255, 255, 255, 255).toARGB }
    expect(0xFFFFCC99) { Color.fromRGB(0xFFCC99).toARGB }
    expect(0xCC9933FF) { Color.fromARGB(0xFFCC9933).toRGBA }
    expect(0xFFCC9933) { Color.fromARGB(0xFFCC9933).toARGB }
    expect(0xFFFFCC99) { Color.fromARGB(0xFFFFCC99).toARGB }
    expect(0xFFCC99FF) { Color.fromARGB(0xFFFFCC99).toRGBA }
    expect("0191919") { Color.from(25, 25, 25, 0).toHexString }
    expect("(255,204,153,255)") { Color.fromRGB(0xFFCC99).toString }
    expect("33FFCC99") { Color.fromARGB(0x33FFCC99).toHexString }
    expect("FFFFCC99") { Color.fromARGB(0xFFFFCC99).toHexString }
  }
}
