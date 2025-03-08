package fr.hashtek.spigot.zm;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.spigot.hashgui.manager.HashGuiManager;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Zm
    extends JavaPlugin
    implements HashLoggable
{

    private static Zm INSTANCE;
    private Tekore core;
    private HashLogger logger;

    private PluginManager pluginManager;
    private HashGuiManager guiManager;

    private HashConfig hashConfig;

    @Override
    public void onEnable()
    {
        INSTANCE = this;

        try {
            this.core = Tekore.getInstance();
        }
        catch (NoClassDefFoundError exception) {
            System.err.println("Tekore failed to load, stopping server.");
            this.getServer().shutdown();
            return;
        }

        this.setupConfig();
        this.setupHashLogger();

        this.logger.info(this, "Starting Zm...");

        // ...

        this.logger.info(this, "Zm loaded.");
    }

    @Override
    public void onDisable()
    {
        this.logger.info(this, "Disabling Zm...");

        // ...

        this.logger.info(this, "Zm disabled.");
    }

    /**
     * Creates a new instance of HashConfig, to read configuration files.
     * Also creates a new instance of LobbyConfiguration.
     */
    private void setupConfig()
    {
        final String configFilename = "config.yml";

        try {
            this.hashConfig = new HashConfig(
                this.getClass(),
                configFilename,
                this.getDataFolder().getPath() + "/" + configFilename,
                true
            );
        } catch (IOException exception) {
            this.logger.fatal(this, "Failed to read config file. Stopping server.", exception);
            this.getServer().shutdown();
        }
    }

    /**
     * Creates an instance of HashLogger.
     * This function doesn't use HashLogger because it is called before the
     * initialization of HashLogger. System.err.println is used instead.
     */
    private void setupHashLogger()
    {
        try {
            this.logger = HashLogger.fromEnvConfig(this, this.hashConfig.getEnv());
        } catch (IllegalArgumentException | NullPointerException exception) {
            System.err.println("Can't initialize HashLogger. Stopping.");
            this.getServer().shutdown();
        }
    }

}
