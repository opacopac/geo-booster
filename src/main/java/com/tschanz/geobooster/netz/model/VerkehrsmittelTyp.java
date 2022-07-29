package com.tschanz.geobooster.netz.model;

import lombok.RequiredArgsConstructor;

import java.util.*;


@RequiredArgsConstructor
public enum VerkehrsmittelTyp {
    BAHN((byte) 0b00000001),
    BUS((byte) 0b00000010),
    SCHIFF((byte) 0b00000100),
    SEILBAHN((byte) 0b00001000),
    TRAM((byte) 0b00010000),
    FUSSWEG((byte) 0b00100000);


    private final byte bitMask;


    public static Collection<VerkehrsmittelTyp> ANY = Arrays.asList(BAHN, BUS, SCHIFF, SEILBAHN, TRAM, FUSSWEG);
    public static Collection<VerkehrsmittelTyp> ANY_EXCEPT_FUSSWEG = Arrays.asList(BAHN, BUS, SCHIFF, SEILBAHN, TRAM);
    public static Collection<VerkehrsmittelTyp> FUSSWEG_ONLY = Collections.singletonList(FUSSWEG);


    public static byte getBitMask(Collection<VerkehrsmittelTyp> vmTypes) {
        var bitMask = 0b00000000;
        for (VerkehrsmittelTyp vmType : vmTypes) {
            bitMask = (bitMask | vmType.bitMask);
        }

        return (byte) bitMask;
    }


    public static List<VerkehrsmittelTyp> getVmTypes(byte bitMask) {
        var vmTypeList = new ArrayList<VerkehrsmittelTyp>();
        Arrays.stream(VerkehrsmittelTyp.values()).forEach(vmType -> {
            if ((bitMask & vmType.bitMask) > 0) {
                vmTypeList.add(vmType);
            }
        });

        return vmTypeList;
    }
}
