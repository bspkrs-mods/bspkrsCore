package bspkrs.fml.util;

import java.util.HashMap;

import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class InputEventListener
{
    private static HashMap<KeyBinding, InputEventListener> instances = new HashMap<KeyBinding, InputEventListener>();

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
        int keyCode = keyBinding.getKeyCode();
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
            MinecraftForge.EVENT_BUS.unregister(instances.get(kb));
            instances.remove(kb);
        }
    }

}
