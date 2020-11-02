package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class CustomizeSkylandsLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private BetaGeneratorSettings generatorSettings;
    
    private boolean generateIceDesert = false;
    private boolean generateSkyDim = false;
    
    private ButtonListWidget buttonList;

    public CustomizeSkylandsLevelScreen(CreateWorldScreen parent, BetaGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.beta.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
      
        if (generatorSettings.settings.contains("generateIceDesert"))
            generateIceDesert = generatorSettings.settings.getBoolean("generateIceDesert");
        if (generatorSettings.settings.contains("generateSkyDim"))
            generateSkyDim = generatorSettings.settings.getBoolean("generateSkyDim");
    }
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.client.openScreen(parent);
                return;
            }
        ));

        this.addButton(new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            (buttonWidget) -> {
                this.client.openScreen(parent);
            }
        ));
        
 this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
 
         this.buttonList.addSingleOptionEntry(
             new BooleanOption(
                 "createWorld.customize.beta.generateSkyDim", 
                 (gameOptions) -> { return generateSkyDim; }, 
                 (gameOptions, value) -> {
                     generateSkyDim = value;
                     generatorSettings.settings.putBoolean("generateSkyDim", value);
                 }
         ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.beta.generateIceDesert", 
                (gameOptions) -> { return generateIceDesert; }, 
                (gameOptions, value) -> {
                    generateIceDesert = value;
                    generatorSettings.settings.putBoolean("generateIceDesert", value);
                }
        ));
        
        this.children.add(this.buttonList);

    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
}
