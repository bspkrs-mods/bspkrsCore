package bspkrs.util;

import org.apache.logging.log4j.*;

public enum BSLog
{
    INSTANCE;

    private Logger logger;

    public Logger getLogger()
    {
        if(this.logger == null)
        {
            this.init();
        }
        return this.logger;
    }

    private void init()
    {
        if(this.logger != null)
        {
            return;
        }
        this.logger = LogManager.getLogger("bspkrsCore");
    }

    public static void info(final String format, final Object... args)
    {
        BSLog.INSTANCE.log(Level.INFO, format, args);
    }

    public static void log(final Level level, final Throwable exception, final String format, final Object... args)
    {
        if(args != null && args.length > 0)
        {
            BSLog.INSTANCE.getLogger().log(level, String.format(format, args), exception);
        }
        else
        {
            BSLog.INSTANCE.getLogger().log(level, format, exception);
        }
    }

    public static void severe(final String format, final Object... args)
    {
        BSLog.INSTANCE.log(Level.ERROR, format, args);
    }

    public static void warning(final String format, final Object... args)
    {
        BSLog.INSTANCE.log(Level.WARN, format, args);
    }

    private void log(final Level level, final String format, final Object... data)
    {
        if(data != null && data.length > 0)
        {
            this.getLogger().log(level, String.format(format, data));
        }
        else
        {
            this.getLogger().log(level, format);
        }
    }
}
