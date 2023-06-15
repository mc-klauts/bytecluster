package net.bytemc.cluster.api.command.argument.transformers;

import net.bytemc.cluster.api.command.argument.ArgumentTransformer;
import net.bytemc.cluster.api.service.CloudGroupType;

import java.lang.reflect.Parameter;

public final class CloudGroupTypeTransformer implements ArgumentTransformer<CloudGroupType> {

    private boolean isValid(String input) {
        for (CloudGroupType value : CloudGroupType.values()) {
            if (input.toUpperCase().equals(value.name())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CloudGroupType transform(Parameter parameter, String input) {
        if (!this.isValid(input)) {
            return CloudGroupType.VELOCITY;
        }
        return CloudGroupType.valueOf(input.toUpperCase());
    }
}
