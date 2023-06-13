package net.bytemc.cluster.plugin.minestom;

import net.bytemc.bytecluster.wrapper.WrapperLauncher;
import net.bytemc.cluster.api.logging.Logger;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;

public final class MinestomExtension extends Extension {

    @Override
    public void initialize() {
       // new CloudShutdownCommand();

        //set cloud generated token (only if velocity is present)
        WrapperLauncher.getSecureToken().ifPresent(s -> VelocityProxy.enable(s));

        if (!VelocityProxy.isEnabled()) {
            BungeeCordProxy.enable();
        }

        if (MojangAuth.isEnabled() && !VelocityProxy.isEnabled()) {
            Logger.warn("This player forwarding options is not compatible with Mojang auth support.");
        }
    }

    @Override
    public void terminate() {

    }

}
