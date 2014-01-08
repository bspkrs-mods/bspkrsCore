package bspkrs.fml.util;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import bspkrs.helpers.client.settings.KeyBindingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;

public abstract class InputEventListener
{
    protected KeyBinding keyBinding;
    protected boolean    isKeyDown;
    protected boolean    allowRepeats;
    
    public InputEventListener(KeyBinding keyBinding, boolean allowRepeats)
    {
        this.keyBinding = keyBinding;
        this.allowRepeats = allowRepeats;
        this.isKeyDown = false;
    }
    
    public KeyBinding getKeyBinding()
    {
        return this.keyBinding;
    }
    
    @SubscribeEvent
    public void onKeyInputEvent(KeyInputEvent event)
    {
        onInputEvent(event);
    }
    
    @SubscribeEvent
    public void onMouseInputEvent(MouseInputEvent event)
    {
        onInputEvent(event);
    }
    
    private void onInputEvent(InputEvent event)
    {
        int keyCode = KeyBindingHelper.getKeyCode(keyBinding);
        boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
        if (state != isKeyDown || (state && allowRepeats))
        {
            if (state)
                keyDown(keyBinding, state == isKeyDown);
            else
                keyUp(keyBinding);
            
            isKeyDown = state;
        }
    }
    
    public abstract void keyDown(KeyBinding kb, boolean isRepeat);
    
    public abstract void keyUp(KeyBinding kb);
    
}
