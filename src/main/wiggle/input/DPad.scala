//
// $Id$

package wiggle.input

import wiggle.app.Entity
import wiggle.util.Task

/**
 * Maintains groups of key bindings where only one key can be active at a time in a group, like a
 * directional pad. A traditional dpad has horizontal bindings where only one of left or right can
 * be active and vertical bindings where only one of up or down can be active. Multiple key bindings
 * are supported for each group to allow left and right handed variations (WASD and arrow keys, for
 * example).
 */
class DPad (kdb :Keyboard, target :Entity)
{
  /** A group of mappings from keys to tasks. */
  class Group ()
  {
    /** Binds a key code to a particular task. */
    def bind (code :Int, task :Task) :Group = {
      val key = kdb.key(code)
      _keymap += (key -> task)
      key.addOnPress(onPress)
      key.addOnRelease(onRelease)
      this
    }

    /** Binds a list of key codes to a particular task. Each key will be mapped to the same task. */
    def bind (keys :List[Int], task :Task) :Group = {
      keys.foreach(bind(_, task))
      this
    }

    /** Convenience function for binding left arrow key and a (for wasd controls). */
    def bindLeft (task :Task) = bind(List(Keyboard.KEY_LEFT, Keyboard.KEY_A), task)

    /** Convenience function for binding right arrow key and d (for wasd controls). */
    def bindRight (task :Task) = bind(List(Keyboard.KEY_RIGHT, Keyboard.KEY_D), task)

    /** Convenience function for binding up arrow key and w (for wasd controls). */
    def bindUp (task :Task) = bind(List(Keyboard.KEY_UP, Keyboard.KEY_W), task)

    /** Convenience function for binding down arrow key and s (for wasd controls). */
    def bindDown (task :Task) = bind(List(Keyboard.KEY_DOWN, Keyboard.KEY_S), task)

    /** Activates or deactivates first-wins mode. In first-wins mode, the first key pressed will
     * remain active until it is released. No other key in the group will take effect. In last-wins
     * mode (the default) any key pressed will override any already pressed key. If the original key
     * remains down when the overriding key is released, the original task will be resumed. */
    def setFirstWins (firstWins :Boolean) = {
      _firstWins = true
      this
    }

    protected def onPress (key :Keyboard#Key) = {
      if (!_firstWins || _pressed.isEmpty) {
        _pressed = key :: _pressed
        activate(key)
      }
      true
    }

    protected def onRelease (key :Keyboard#Key) = {
      if (!_pressed.isEmpty) {
        if (_pressed.head == key) {
          if (_pressed.tail.isEmpty) deactivate()
          else activate(_pressed.tail.head)
        }
        _pressed = _pressed - key
      }
      true
    }

    protected def activate (key :Keyboard#Key) = _keymap.get(key) match {
      case None => // nada
      case Some(task) => {
        deactivate()
        target.add(task)
        _active = Some(task)
      }
    }

    protected def deactivate () = _active match {
      case None => // nada
      case Some(task) => {
        target.remove(task)
        _active = None
      }
    }

    private[this] var _firstWins = false
    private[this] var _keymap = Map[Keyboard#Key, Task]()
    private[this] var _pressed :List[Keyboard#Key] = Nil
    private[this] var _active :Option[Task] = None
  }

  /** Createse a mutually exclusive group. */
  def group () = new Group
}
