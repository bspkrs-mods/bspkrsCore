package bspkrs.fml.util;

import net.minecraftforge.fml.relauncher.*;
import java.util.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.lwjgl.input.*;

@SideOnly(Side.CLIENT)
public abstract class InputEventListener
{
    private static HashMap<KeyBinding, InputEventListener> instances;
    protected KeyBinding keyBinding;
    protected boolean isKeyDown;
    protected boolean allowRepeats;

    public InputEventListener(final KeyBinding keyBinding, final boolean allowRepeats)
    {
        this.keyBinding = keyBinding;
        this.allowRepeats = allowRepeats;
        this.isKeyDown = false;
        InputEventListener.instances.put(keyBinding, this);
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    public KeyBinding getKeyBinding()
    {
        return this.keyBinding;
    }

    @SubscribeEvent
    public void onKeyInputEvent(final InputEvent.KeyInputEvent event)
    {
        this.onInputEvent((InputEvent)event);
    }

    @SubscribeEvent
    public void onMouseInputEvent(final InputEvent.MouseInputEvent event)
    {
        this.onInputEvent((InputEvent)event);
    }

    private void onInputEvent(final InputEvent event)
    {
        final int keyCode = this.keyBinding.getKeyCode();
        final boolean state = (keyCode < 0) ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
        if(state != this.isKeyDown || (state && this.allowRepeats))
        {
            if(state)
            {
                this.keyDown(this.keyBinding, state == this.isKeyDown);
            }
            else
            {
                this.keyUp(this.keyBinding);
            }
            this.isKeyDown = state;
        }
    }

    public abstract void keyDown(final KeyBinding p0, final boolean p1);

    public abstract void keyUp(final KeyBinding p0);

    public static boolean isRegistered(final KeyBinding kb)
    {
        return InputEventListener.instances.containsKey(kb);
    }

    public static void unRegister(final KeyBinding kb)
    {
        if(isRegistered(kb))
        {
            MinecraftForge.EVENT_BUS.unregister((Object)InputEventListener.instances.get(kb));
            InputEventListener.instances.remove(kb);
        }
    }

    static
    {
        InputEventListener.instances = new HashMap<KeyBinding, InputEventListener>();
    }
}
