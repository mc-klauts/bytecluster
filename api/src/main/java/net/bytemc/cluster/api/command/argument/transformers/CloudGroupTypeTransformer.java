package net.bytemc.cluster.api.command.argument.transformers;

import java.lang.reflect.Parameter;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;
import net.bytemc.cluster.api.service.CloudGroupType;

public class CloudGroupTypeTransformer implements ArgumentTransformer<CloudGroupType> {

    @Override
    public CloudGroupType transform(
        Parameter parameter,
        String input
    ) {
        for (CloudGroupType value : CloudGroupType.values()) {
            if (value.name().equals(input.toUpperCase()))
                return value;
        }
        return null;
    }
}
