package com.AnthonyDombrowski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import jdk.incubator.http.HttpRequest;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

  private static final String W_FASTAI_DIR = "C:\\Users\\adombrowski\\Documents\\fastai\\";

  private static final String W_MTGPICS_DIR = FASTAI_DIR + "data\\mtgpics\\";

  private static final String L_MTGPICS_DIR = "~/data/mtgpics";

  String s = "C:\\Users\\adombrowski\\Documents\\fastai\\data\\mtgpics";

  public static void main(String[] args) {
    final String
            jsonFilename = L_MTGPICS_DIR + "/scryfall-oracle-cards_May_5_2019.json";
    // final String
    //         dirTrainCommon =
    //         MTGPICS_DIR + "train/common";
    // final String
    //         dirTrainUncommon =
    //         MTGPICS_DIR + "train/uncommon";

    // final String
    //         dirValidCommon =
    //         MTGPICS_DIR + "valid/common";

    // final String
    //         dirValidUncommon =
    //         MTGPICS_DIR + "valid/uncommon";

    final String[] rarities = [ "common", "uncommon", "rare", "mythic"];
    final String[] tv = [ "train", "valid" ];
    final List<String> rarityPaths = new ArrayList<>();

    for ( i = 0; i < rarities.length; i++) {
      rarityPaths.add(L_MTGPICS_DIR + "/" + rarities[i]);
    }

    try {
      //
//      TODO: Get the JSON file from scryfall.com api programatically if need a new version
//      HttpRequest httpRequest =
//              HttpRequest.newBuilder()
//                      .uri(new URI("GET https://api.scryfall.com/bulk-data\n"))
//                      .GET()
//                      .build();

      String
        jsonObjectsStringList =
        new String(Files.readAllBytes(Paths.get(jsonFilename)));
      JsonParser parser = new JsonParser();
      JsonElement element = null;

      JsonArray el = parser.parse(jsonObjectsStringList).getAsJsonArray();
      int count = 0;
      // File dirFileTrainCommon = new File(dirTrainCommon);
      // File dirFileValidCommon = new File(dirValidCommon);
      // File dirFileTrainUncommon = new File(dirTrainUncommon);
      // File dirFileValidUncommon = new File(dirValidUncommon);
      File destDirCommon = null;
      File destDirUnCommon = null;
      File destFile = null;

      Set<String> oracleIdsSeen = new HashSet<>();

      for (JsonElement element1 : el) {
        try {
          final JsonObject object = element1.getAsJsonObject();

          // Check by oracle id if we've already looked at this card.
          if (!oracleIdsSeen.add(object.get("oracle_id").getAsString())) {
            // We've already looked at this card.
            System.out.println("repeat card read");
            continue;
          }

          // Unlimited Ed. cards can look a little different.
          if (object.has("set_name") && object.get("set_name").getAsString().equalsIgnoreCase("Unlimited Edition")) {
            continue;
          }

          // Tokens, promos, and other esoteric objects don't have these identifiers. Skip them.
          if (!object.has("multiverse_ids")) {
            continue;
          }

          double percentageComplete = (double) count / (double) el.size() * 100.00;
          System.out.print("***************");
          System.out.printf("\t %.2f %% complete. (%d/%d) \t", percentageComplete, count, el.size());
          System.out.println("***************");

          // This gets images based on the existence of the rarity tag.
          if (object.has("rarity")) {
            String rarityLevel = object.get("rarity").getAsString();
            String imageURI = getPNGImageURI(object);


            for (i = 0; i < rarities.length; i++) {
              if(rarityLevel.equalsIgnoreCase(rarities[i])) {
                String imgFilename = object.get("name").getAsString() + ".png";
                destFile =
                new File(rarityPaths.get(i), imgFilename);

              if (destFile.exists()) {
                System.out.println("Already have img.");
                continue;
              }

              FileUtils.copyURLToFile(new URL(imageURI), destFile,
                // connect timeout 1min
                (1_000 * 60 * 1),
                // read timeout 1min
                (1_000 * 60 * 1));
              }

              System.out.println(destFile.toString());
              System.out.println(imageURI);
              System.out.println();
            }
          }

          count++;
        } catch (Exception exc) {
          System.err.println("Couldn't get png image from " +
                               element1.toString());
          exc.printStackTrace();
        }
      }


       if (el.isJsonArray()) {
         for (JsonElement ele : el) {
           if (ele.isJsonObject() && ele.getAsJsonObject().has("image_uris")){
             System.out.println(ele.getAsJsonObject());

           }
         }
       }

    } catch (IOException e) {
      System.err.println("Couldn't read all json objects from file.");
    }
  }

  private static String getPNGImageURI(JsonObject jsonObject) {
    if (!jsonObject.has("image_uris")) {
      throw new IllegalArgumentException();
    }

    JsonElement imageURISJSONObject = jsonObject.get("image_uris");

    if (!imageURISJSONObject.isJsonObject()) {
      throw new IllegalArgumentException();
    }

    JsonObject imageurisobject = imageURISJSONObject.getAsJsonObject();

    if (!imageurisobject.has("png")) {
      throw new IllegalArgumentException();
    }

    return imageurisobject.get("png").getAsString();
  }

  private static String getArtCropURI(JsonObject jsonObject) {
    if (!jsonObject.has("image_uris")) {
      throw new IllegalArgumentException();
    }

    JsonElement imageURISJSONObject = jsonObject.get("image_uris");

    if (!imageURISJSONObject.isJsonObject()) {
      throw new IllegalArgumentException();
    }

    JsonObject imageurisobject = imageURISJSONObject.getAsJsonObject();

    if (!imageurisobject.has("art_crop")) {
      throw new IllegalArgumentException();
    }

    return imageurisobject.get("art_crop").getAsString();
  }
}
