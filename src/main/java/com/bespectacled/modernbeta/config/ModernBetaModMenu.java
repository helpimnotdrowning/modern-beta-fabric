package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.config.ModernBetaConfig;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class ModernBetaModMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return "modernbeta";
    }

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get();
    }
}
