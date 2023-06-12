package net.bytemc.cluster.api.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Tuple<L, R> {

    private L l;
    private R r;

}
