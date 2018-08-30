package com.AnthonyDombrowski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    // write your code here
    String
      filename =
      "/home/akddombrowski/Pictures/scryfall-default" + "-cards.json";
    String
      dirTrainCommon =
      "/home/akddombrowski/Pictures/mtgpicdownload/train/common/";
    String
      dirTrainUncommon =
      "/home/akddombrowski/Pictures/mtgpicdownload/train/uncommon/";
    String
      dirValidCommon =
      "/home/akddombrowski/Pictures/mtgpicdownload/valid/common/";
    String
      dirValidUncommon =
      "/home/akddombrowski/Pictures/mtgpicdownload/valid/uncommon/";

    File jsonFile = new File(filename);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<JsonElement> jsonObjects = new ArrayList<>();

    try {
      String
        jsonObjectsStringList =
        new String(Files.readAllBytes(Paths.get(filename)));
      JsonParser parser = new JsonParser();
      JsonElement element = null;

      JsonArray el = parser.parse(jsonObjectsStringList).getAsJsonArray();
      int count = 0;
      File dirFileTrainCommon = new File(dirTrainCommon);
      File dirFileValidCommon = new File(dirValidCommon);
      File dirFileTrainUncommon = new File(dirTrainUncommon);
      File dirFileValidUncommon = new File(dirValidUncommon);
      File destDirCommon = null;
      File destDirUnCommon = null;
      File destFile = null;
      for (JsonElement element1 : el) {
        try {
          // Roughly a 1/3 of the images will go to the valid directory
          // and the other 2/3 to the train directory.
          if (count % 3 == 0) {
            count++;
            continue;
            // destDirCommon = dirFileValidCommon;
            // destDirUnCommon = dirFileValidUncommon;
          } else {
            destDirCommon = dirFileTrainCommon;
            destDirUnCommon = dirFileTrainUncommon;
          }
          JsonObject object = element1.getAsJsonObject();
          if (object.has("rarity")) {
            String rarityLevel = object.get("rarity").getAsString();
            String imageURI = getPNGImageURI(object);
            if (rarityLevel.equalsIgnoreCase("common")) {
              destFile =
                new File(destDirCommon,
                  object.get("name").getAsString() + ".png");
              FileUtils.copyURLToFile(new URL(imageURI), destFile,
                // connect timeout 5min
                (1_000 * 60 * 5),
                // read timeout 5min
                (1_000 * 60 * 5));
              System.out.println(destDirCommon.toString());
              System.out.println("common");
              System.out.println(imageURI);
            } else if (rarityLevel.equalsIgnoreCase("uncommon")) {
              destFile =
                new File(destDirUnCommon,
                  object.get("name").getAsString() + ".png");
              FileUtils.copyURLToFile(new URL(imageURI),
                destFile,
                (1_000 * 60 * 5),
                // connect timeout 5min
                (1_000 * 60 * 5)
                // read timeout 5min
              );
              System.out.println(destDirUnCommon.toString());
              System.out.println("uncommon");
              System.out.println(imageURI);
            }
          }

          count++;

        } catch (Exception exc) {
          System.err.println("Couldn't get png image from " +
                               element1.toString());
          exc.printStackTrace();
        }
      }


      // if (el.isJsonArray()) {
      //   for (JsonElement ele : el) {
      //     if (ele.isJsonObject() && ele.getAsJsonObject().has("image_uris")){
      //       System.out.println(ele.getAsJsonObject(new
      //                                                JsonElement
      //                                                ("image_uris")));
      //
      //     }
      //   }
      // }

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
}
