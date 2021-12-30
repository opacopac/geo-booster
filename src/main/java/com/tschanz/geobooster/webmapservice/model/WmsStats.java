package com.tschanz.geobooster.webmapservice.model;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class WmsStats {
    private final List<Long> pngResponseTimeMs = new ArrayList<>();
    private final List<Long> utfResponseTimeMs = new ArrayList<>();


    public void addPngResponse(long responseTimeMs) {
        this.pngResponseTimeMs.add(responseTimeMs);
    }


    public void addUtfGridResponse(long responseTimeMs) {
        this.utfResponseTimeMs.add(responseTimeMs);
    }


    public long getPngRequests() {
        return this.pngResponseTimeMs.size();
    }


    public long getUtfGridRequests() {
        return this.utfResponseTimeMs.size();
    }


    public long getLastNPngResponseAverage(int lastN) {
        if (this.pngResponseTimeMs.size() == 0) {
            return 0;
        } else {
            return this.getAverage(this.pngResponseTimeMs, lastN);
        }
    }


    public long getLastNUtfGridResponseAverage(int lastN) {
        if (this.utfResponseTimeMs.size() == 0) {
            return 0;
        } else {
            return this.getAverage(this.utfResponseTimeMs, lastN);
        }
    }


    private long getAverage(List<Long> values, int lastN) {
        var minIdx = Math.max(values.size() - lastN, 0);
        var maxIdx = values.size();
        var count = maxIdx - minIdx;
        var totalMs = values
            .subList(minIdx, maxIdx).stream()
            .reduce(0L, (sum, entry) -> sum += entry);

        return totalMs / count;
    }
}
