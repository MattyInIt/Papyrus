package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import java.util.Set;

public class RegistriesArgumentProviderRewriter extends SearchReplaceRewriter {

    private static final Set<ResourceKey<? extends Registry<?>>> NEW_BRIDGE_METHODS = Set.of(
        Registries.ITEM,
        Registries.BLOCK
    );

    @Override
    public void insert(SearchMetadata metadata, StringBuilder builder) {
        RegistryEntries.forEach(entry -> {
            builder.append(metadata.indent());
            builder.append("register(").append(
                "%s.%s, %s.class, %s.%s, %s.class, %s.class, %s".formatted(
                Types.REGISTRY_KEY.simpleName(),
                entry.registryKeyField(),
                this.importCollector.getShortName(entry.data().api().klass()),
                Registries.class.getSimpleName(),
                entry.registryKeyField(),
                this.importCollector.getShortName(entry.data().impl().klass()),
                this.importCollector.getShortName(entry.elementClass()),
                Boolean.toString(NEW_BRIDGE_METHODS.contains(entry.getRegistryKey()))
            )).append(");");
            builder.append("\n");
        });
    }
}
