package com.dynamicbiomes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    public boolean DefaultProfilesOn = true;
    public boolean perPlayerBiomeUpdatingOn = false;
    public int QuadsPerTick = 4;
    public int radius = 4;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("dynamic-biomes.json");
    public static ModConfig INSTANCE = new ModConfig();


    public static void load(){
        try{
            if (Files.exists(PATH)){
                try (Reader reader = Files.newBufferedReader(PATH)){
                    ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                    if (loaded != null){
                        INSTANCE = loaded;
                    }
                }
            }
            save();

        } catch (IOException | JsonParseException e){
            e.printStackTrace();
        }
    }

    public static void save() throws IOException{
        try(Writer writer = Files.newBufferedWriter(PATH)){
            GSON.toJson(INSTANCE, writer);
        }
    }
    public ModConfig() {

    }
}
