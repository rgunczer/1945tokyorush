package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class TextureFactory {

    private static String folderName = "textures";

    public static Texture create(String fileName) {
        return new Texture(folderName + "/" + fileName);
    }

    public static Texture[] crreateArray(String[] fileNames) {
        Texture[] textureArray = new Texture[fileNames.length];

        for (int i = 0; i < fileNames.length - 1; ++i) {
            textureArray[i] = create(fileNames[i]);
        }

        return textureArray;
    }
}
