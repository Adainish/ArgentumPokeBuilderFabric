package io.github.adainish.argentumpokebuilderfabric.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.argentumpokebuilderfabric.ArgentumPokeBuilderFabric;
import io.github.adainish.argentumpokebuilderfabric.util.Adapters;

import java.io.*;

public class LanguageConfig
{
    public String prefix;
    public String splitter;

    public LanguageConfig()
    {
        this.prefix = "&c&l[&b&lPokeBuilder&c&l]";
        this.splitter = " » ";
    }

    public static void writeConfig()
    {
        File dir = ArgentumPokeBuilderFabric.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        LanguageConfig config = new LanguageConfig();
        try {
            File file = new File(dir, "language.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            ArgentumPokeBuilderFabric.getLog().warn(e);
        }
    }

    public static LanguageConfig getConfig()
    {
        File dir = ArgentumPokeBuilderFabric.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "language.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            ArgentumPokeBuilderFabric.getLog().error("Something went wrong attempting to read the Language Config");
            return null;
        }

        return gson.fromJson(reader, LanguageConfig.class);
    }
}
