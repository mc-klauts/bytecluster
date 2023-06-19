package net.bytemc.bytecluster.wrapper.groups;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.groups.CollectionGroupRequest;
import net.bytemc.cluster.api.network.packets.groups.CollectionGroupResponse;
import net.bytemc.cluster.api.network.packets.groups.SingletonGroupRequest;
import net.bytemc.cluster.api.network.packets.groups.SingletonGroupResponse;
import net.bytemc.cluster.api.network.packets.services.CloudServiceRequestPlayerAmountPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceResponsePlayerAmountPacket;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;

import java.util.Collection;

public final class CloudServiceGroupProviderImpl implements CloudServiceGroupProvider {
    @Override
    public AsyncTask<Collection<CloudServiceGroup>> findGroupsAsync() {
        var tasks = new AsyncTask<Collection<CloudServiceGroup>>();
        Wrapper.getInstance().sendQueryPacket(new CollectionGroupRequest(), CollectionGroupResponse.class, (packet) -> tasks.complete(packet.getGroups()));
        return tasks;
    }

    @Override
    public Collection<CloudServiceGroup> findGroups() {
        return findGroupsAsync().getSync(null);
    }

    @Override
    public AsyncTask<CloudServiceGroup> findGroupAsync(String id) {
        var tasks = new AsyncTask<CloudServiceGroup>();
        Wrapper.getInstance().sendQueryPacket(new SingletonGroupRequest(id), SingletonGroupResponse.class, (packet) -> tasks.complete(packet.getServiceGroup()));
        return tasks;
    }

    @Override
    public CloudServiceGroup findGroup(String id) {
        return findGroupAsync(id).getSync(null);
    }

    @Override
    public void addGroup(CloudServiceGroup group) {
        //todo
    }

    @Override
    public void removeGroup(String name) {
        //todo
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public AsyncTask<Boolean> existsAsync(String id) {
        //todo
        return null;
    }

    @Override
    public CloudServiceGroup getCloudServiceGroupFromBuffer(PacketBuffer buffer) {

        var name = buffer.readString();
        var bootstrapNodes = buffer.readString();
        var groupType = buffer.readEnum(CloudGroupType.class);
        int min = buffer.readInt();
        int max = buffer.readInt();
        int maxMemory = buffer.readInt();
        boolean fallback = buffer.readBoolean();

        return new WrapperCloudServiceGroup(name, groupType, min, max, maxMemory, fallback, bootstrapNodes);
    }
}
