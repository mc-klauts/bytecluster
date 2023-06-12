package net.bytemc.cluster.api.command.argument.transformers;

import net.bytemc.cluster.api.command.argument.ArgumentTransformer;
import net.bytemc.cluster.api.service.CloudGroupType;

public final class CloudGroupTypeTransformer implements
    ArgumentTransformer<CloudGroupType> {

    @Override
    public CloudGroupType transform(String input) {
        if (!this.isValid(input)) {
            return CloudGroupType.VELOCITY;
        }
        return CloudGroupType.valueOf(input.toUpperCase());
    }

    private boolean isValid(String input) {
        for (CloudGroupType value : CloudGroupType.values()) {
            if (input.toUpperCase().equals(value.name())) {
                return true;
            }
        }
        return false;
    }
}
