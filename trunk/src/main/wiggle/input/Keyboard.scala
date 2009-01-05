//
// $Id$

package wiggle.input

import org.lwjgl.input.{Keyboard => GLKeyboard}

/**
 * Represents a keyboard configuration. Generally there is one active keyboard at a time and all of
 * its input mappings are in effect.
 */
class Keyboard (val repeatEnabled :Boolean)
{
  /** Define a type for our key event handlers. If the handler function returns true, it will
   * remain registered, if it returns false, its registration will be removed. */
  type Handler = (Key) => Boolean

  /** Represents a particular key and contains event handler mappings. */
  class Key (val code :Int)
  {
    /** Returns a reference to the keyboard with which this key is associated. */
    def keyboard = Keyboard.this

    /** Returns true if this key is currently depressed, false if not. */
    def isDown = GLKeyboard.isKeyDown(code)

    /** Adds a key press handler. */
    def addOnPress (onPress :Handler) {
      _onPress = onPress :: _onPress
    }

    /** Adds a key release handler. */
    def addOnRelease (onRelease :Handler) {
      _onRelease = onRelease :: _onRelease
    }

    /** Called by our Keyboard when this key is pressed. */
    private[Keyboard] def triggerPress () {
      _onPress = _onPress.filter(!_(this))
    }

    /** Called by our Keyboard when this key is released. */
    private[Keyboard] def triggerRelease () {
      _onRelease = _onRelease.filter(!_(this))
    }

    private[this] var _onPress :List[Handler] = Nil
    private[this] var _onRelease :List[Handler] = Nil
  }

  /** Returns the key for the specified key code. */
  def key (code :Int) :Key = _keys.get(code) match {
    case Some(key) => key
    case None => {
      val key = new Key(code)
      _keys += (code -> key)
      key
    }
  }

  /** Processes pending keyboard events and dispatches them to listeners. Called once per frame by
   * the application when this keyboard is active. */
  def poll () {
    GLKeyboard.poll
    while (GLKeyboard.next) {
      _keys.get(GLKeyboard.getEventKey) match {
        case Some(key) => 
          if (GLKeyboard.getEventKeyState) key.triggerPress() else key.triggerRelease()
        case None => /* no registered handler, ignore it */
      }
    }
  }

  /** A mapping of all keys that have been requested. */
  private[this] var _keys = Map[Int, Key]()
}

object Keyboard
{
  // keycode mappings
  val KEY_0 = GLKeyboard.KEY_0
  val KEY_1 = GLKeyboard.KEY_1
  val KEY_2 = GLKeyboard.KEY_2
  val KEY_3 = GLKeyboard.KEY_3
  val KEY_4 = GLKeyboard.KEY_4
  val KEY_5 = GLKeyboard.KEY_5
  val KEY_6 = GLKeyboard.KEY_6
  val KEY_7 = GLKeyboard.KEY_7
  val KEY_8 = GLKeyboard.KEY_8
  val KEY_9 = GLKeyboard.KEY_9
  val KEY_A = GLKeyboard.KEY_A
  val KEY_ADD = GLKeyboard.KEY_ADD
  val KEY_APOSTROPHE = GLKeyboard.KEY_APOSTROPHE
  val KEY_APPS = GLKeyboard.KEY_APPS
  val KEY_AT = GLKeyboard.KEY_AT
  val KEY_AX = GLKeyboard.KEY_AX
  val KEY_B = GLKeyboard.KEY_B
  val KEY_BACK = GLKeyboard.KEY_BACK
  val KEY_BACKSLASH = GLKeyboard.KEY_BACKSLASH
  val KEY_C = GLKeyboard.KEY_C
  val KEY_CAPITAL = GLKeyboard.KEY_CAPITAL
  val KEY_CIRCUMFLEX = GLKeyboard.KEY_CIRCUMFLEX
  val KEY_COLON = GLKeyboard.KEY_COLON
  val KEY_COMMA = GLKeyboard.KEY_COMMA
  val KEY_CONVERT = GLKeyboard.KEY_CONVERT
  val KEY_D = GLKeyboard.KEY_D
  val KEY_DECIMAL = GLKeyboard.KEY_DECIMAL
  val KEY_DELETE = GLKeyboard.KEY_DELETE
  val KEY_DIVIDE = GLKeyboard.KEY_DIVIDE
  val KEY_DOWN = GLKeyboard.KEY_DOWN
  val KEY_E = GLKeyboard.KEY_E
  val KEY_END = GLKeyboard.KEY_END
  val KEY_EQUALS = GLKeyboard.KEY_EQUALS
  val KEY_ESCAPE = GLKeyboard.KEY_ESCAPE
  val KEY_F = GLKeyboard.KEY_F
  val KEY_F1 = GLKeyboard.KEY_F1
  val KEY_F10 = GLKeyboard.KEY_F10
  val KEY_F11 = GLKeyboard.KEY_F11
  val KEY_F12 = GLKeyboard.KEY_F12
  val KEY_F13 = GLKeyboard.KEY_F13
  val KEY_F14 = GLKeyboard.KEY_F14
  val KEY_F15 = GLKeyboard.KEY_F15
  val KEY_F2 = GLKeyboard.KEY_F2
  val KEY_F3 = GLKeyboard.KEY_F3
  val KEY_F4 = GLKeyboard.KEY_F4
  val KEY_F5 = GLKeyboard.KEY_F5
  val KEY_F6 = GLKeyboard.KEY_F6
  val KEY_F7 = GLKeyboard.KEY_F7
  val KEY_F8 = GLKeyboard.KEY_F8
  val KEY_F9 = GLKeyboard.KEY_F9
  val KEY_G = GLKeyboard.KEY_G
  val KEY_GRAVE = GLKeyboard.KEY_GRAVE
  val KEY_H = GLKeyboard.KEY_H
  val KEY_HOME = GLKeyboard.KEY_HOME
  val KEY_I = GLKeyboard.KEY_I
  val KEY_INSERT = GLKeyboard.KEY_INSERT
  val KEY_J = GLKeyboard.KEY_J
  val KEY_K = GLKeyboard.KEY_K
  val KEY_KANA = GLKeyboard.KEY_KANA
  val KEY_KANJI = GLKeyboard.KEY_KANJI
  val KEY_L = GLKeyboard.KEY_L
  val KEY_LBRACKET = GLKeyboard.KEY_LBRACKET
  val KEY_LCONTROL = GLKeyboard.KEY_LCONTROL
  val KEY_LEFT = GLKeyboard.KEY_LEFT
  val KEY_LMENU = GLKeyboard.KEY_LMENU
  val KEY_LMETA = GLKeyboard.KEY_LMETA
  val KEY_LSHIFT = GLKeyboard.KEY_LSHIFT
  val KEY_M = GLKeyboard.KEY_M
  val KEY_MINUS = GLKeyboard.KEY_MINUS
  val KEY_MULTIPLY = GLKeyboard.KEY_MULTIPLY
  val KEY_N = GLKeyboard.KEY_N
  val KEY_NEXT = GLKeyboard.KEY_NEXT
  val KEY_NOCONVERT = GLKeyboard.KEY_NOCONVERT
  val KEY_NUMLOCK = GLKeyboard.KEY_NUMLOCK
  val KEY_NUMPAD0 = GLKeyboard.KEY_NUMPAD0
  val KEY_NUMPAD1 = GLKeyboard.KEY_NUMPAD1
  val KEY_NUMPAD2 = GLKeyboard.KEY_NUMPAD2
  val KEY_NUMPAD3 = GLKeyboard.KEY_NUMPAD3
  val KEY_NUMPAD4 = GLKeyboard.KEY_NUMPAD4
  val KEY_NUMPAD5 = GLKeyboard.KEY_NUMPAD5
  val KEY_NUMPAD6 = GLKeyboard.KEY_NUMPAD6
  val KEY_NUMPAD7 = GLKeyboard.KEY_NUMPAD7
  val KEY_NUMPAD8 = GLKeyboard.KEY_NUMPAD8
  val KEY_NUMPAD9 = GLKeyboard.KEY_NUMPAD9
  val KEY_NUMPADCOMMA = GLKeyboard.KEY_NUMPADCOMMA
  val KEY_NUMPADENTER = GLKeyboard.KEY_NUMPADENTER
  val KEY_NUMPADEQUALS = GLKeyboard.KEY_NUMPADEQUALS
  val KEY_O = GLKeyboard.KEY_O
  val KEY_P = GLKeyboard.KEY_P
  val KEY_PAUSE = GLKeyboard.KEY_PAUSE
  val KEY_PERIOD = GLKeyboard.KEY_PERIOD
  val KEY_POWER = GLKeyboard.KEY_POWER
  val KEY_PRIOR = GLKeyboard.KEY_PRIOR
  val KEY_Q = GLKeyboard.KEY_Q
  val KEY_R = GLKeyboard.KEY_R
  val KEY_RBRACKET = GLKeyboard.KEY_RBRACKET
  val KEY_RCONTROL = GLKeyboard.KEY_RCONTROL
  val KEY_RETURN = GLKeyboard.KEY_RETURN
  val KEY_RIGHT = GLKeyboard.KEY_RIGHT
  val KEY_RMENU = GLKeyboard.KEY_RMENU
  val KEY_RMETA = GLKeyboard.KEY_RMETA
  val KEY_RSHIFT = GLKeyboard.KEY_RSHIFT
  val KEY_S = GLKeyboard.KEY_S
  val KEY_SCROLL = GLKeyboard.KEY_SCROLL
  val KEY_SEMICOLON = GLKeyboard.KEY_SEMICOLON
  val KEY_SLASH = GLKeyboard.KEY_SLASH
  val KEY_SLEEP = GLKeyboard.KEY_SLEEP
  val KEY_SPACE = GLKeyboard.KEY_SPACE
  val KEY_STOP = GLKeyboard.KEY_STOP
  val KEY_SUBTRACT = GLKeyboard.KEY_SUBTRACT
  val KEY_SYSRQ = GLKeyboard.KEY_SYSRQ
  val KEY_T = GLKeyboard.KEY_T
  val KEY_TAB = GLKeyboard.KEY_TAB
  val KEY_U = GLKeyboard.KEY_U
  val KEY_UNDERLINE = GLKeyboard.KEY_UNDERLINE
  val KEY_UNLABELED = GLKeyboard.KEY_UNLABELED
  val KEY_UP = GLKeyboard.KEY_UP
  val KEY_V = GLKeyboard.KEY_V
  val KEY_W = GLKeyboard.KEY_W
  val KEY_X = GLKeyboard.KEY_X
  val KEY_Y = GLKeyboard.KEY_Y
  val KEY_YEN = GLKeyboard.KEY_YEN
  val KEY_Z = GLKeyboard.KEY_Z
}

/**
 * Unit tests for the Keyboard.
 */
package tests {
  import org.scalatest.Suite

  class KeyboardSuite extends Suite {
    def testKeyboard () {
      val kbd = new Keyboard(false)

      kbd.key(Keyboard.KEY_F1).addOnPress((key) => { println("F1! " + key); false })

      println(kbd.key(Keyboard.KEY_F1))
      println(kbd.key(Keyboard.KEY_F1))
      println(kbd.key(Keyboard.KEY_F1))
    }
  }
}
