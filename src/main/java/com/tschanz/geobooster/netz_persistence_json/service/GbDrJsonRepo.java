package com.tschanz.geobooster.netz_persistence_json.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tschanz.geobooster.netz.model.GbDr;
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
public class GbDrJsonRepo {
    private final static String GBDR_ZIP_FILE = "gbdr.zip";
    private final static String GBDR_JSON_FILE = "gbdr.json";


    public boolean hasQuickStartDr() {
        return new File(GBDR_ZIP_FILE).exists();
    }


    @SneakyThrows
    public GbDr readDr() {
        var fileInputStream = new FileInputStream(GBDR_ZIP_FILE);
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
        var fileOutputStream = new FileOutputStream(GBDR_ZIP_FILE);
        var zipOutputStream = new ZipOutputStream(fileOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry(GBDR_JSON_FILE));

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(zipOutputStream, StandardCharsets.UTF_8));
        var gson = new Gson();
        gson.toJson(dr, GbDr.class, writer);
        writer.close();

        zipOutputStream.close();
        fileOutputStream.close();
    }
}
