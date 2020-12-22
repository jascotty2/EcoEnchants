package com.willfp.eco.util.bukkit.meta;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.metadata.FixedMetadataValue;

public class MetadataValueFactory extends PluginDependentFactory<FixedMetadataValue> {
    public MetadataValueFactory(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    public FixedMetadataValue create(Object value) {
        return new FixedMetadataValue(this.plugin, value);
    }
}