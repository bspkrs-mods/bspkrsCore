package bspkrs.fml.util;

import java.util.HashMap;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import bspkrs.helpers.client.settings.KeyBindingHelper;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class InputEventListener
{
    private static HashMap<KeyBinding, InputEventListener> instances = new HashMap();
    
    protected KeyBinding                                   keyBinding;
    protected boolean                                      isKeyDown;
    protected boolean                                      allowRepeats;
    
    public InputEventListener(KeyBinding keyBinding, boolean allowRepeats)
    {
        this.keyBinding = keyBinding;
        this.allowRepeats = allowRepeats;
        this.isKeyDown = false;
        instances.put(keyBinding, this);
        ClientRegistry.registerKeyBinding(keyBinding);
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
    
    public static boolean isRegistered(KeyBinding kb)
    {
        return instances.containsKey(kb);
    }
    
    public static void unRegister(KeyBinding kb)
    {
        if (isRegistered(kb))
        {
            FMLCommonHandler.instance().bus().unregister(instances.get(kb));
            instances.remove(kb);
        }
    }
    
}
