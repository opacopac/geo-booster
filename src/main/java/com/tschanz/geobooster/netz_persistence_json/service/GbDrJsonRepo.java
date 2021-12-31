package com.tschanz.geobooster.netz_persistence_json.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.presentation.service.QuickStartDrSelector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Service
@RequiredArgsConstructor
public class GbDrJsonRepo implements QuickStartDrSelector {
    private int quickStartDrIdx = -1;

    private final static String GBDR_ZIP_FILE = "gbdr_%d.zip";
    private final static String GBDR_JSON_FILE = "gbdr_%d.json";


    public void selectQuickStartDr(int index) {
        this.quickStartDrIdx = index;
    }


    public boolean hasQuickStartDr() {
        var filename = String.format(GBDR_ZIP_FILE, this.quickStartDrIdx);
        return new File(filename).exists();
    }


    @SneakyThrows
    public GbDr readDr() {
        var fileInputStream = new FileInputStream(String.format(GBDR_ZIP_FILE, this.quickStartDrIdx));
        var zipInputStream = new ZipInputStream(fileInputStream);
        zipInputStream.getNextEntry();

        JsonReader reader = new JsonReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
        var gson = new Gson();
        GbDr gbDr = gson.fromJson(reader, GbDr.class);
        reader.close();

        zipInputStream.close();
        fileInputStream.close();

        return gbDr;
    }


    @SneakyThrows
    public void save(GbDr dr) {
        var fileOutputStream = new FileOutputStream(String.format(GBDR_ZIP_FILE, this.quickStartDrIdx));
        var zipOutputStream = new ZipOutputStream(fileOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry(String.format(GBDR_JSON_FILE, this.quickStartDrIdx)));

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(zipOutputStream, StandardCharsets.UTF_8));
        var gson = new Gson();
        gson.toJson(dr, GbDr.class, writer);
        writer.close();

        zipOutputStream.close();
        fileOutputStream.close();
    }
}
