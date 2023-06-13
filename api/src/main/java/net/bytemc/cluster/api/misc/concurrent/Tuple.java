package net.bytemc.cluster.api.misc.concurrent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Tuple<L, R> {

    private L l;
    private R r;

}
