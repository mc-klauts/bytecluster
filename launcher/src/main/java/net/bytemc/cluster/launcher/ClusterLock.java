package net.bytemc.cluster.launcher;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

final class ClusterLock {

    private static final String CLUSTER_FILE_NAME = "launcher.lock";

    private FileLock lock;
    private FileChannel channel;

    public void publishLock() {
        try {
            this.lock.release();
            this.channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean lock(Path clusterDirectory) {
        try {
            this.channel = this.initializeFileChannel(clusterDirectory);
            this.lock = this.channel.tryLock();
            return this.lock != null;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private FileChannel initializeFileChannel(Path clusterDirectory) throws IOException {
        return FileChannel.open(clusterDirectory.resolve(CLUSTER_FILE_NAME), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
}
