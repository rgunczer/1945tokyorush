package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LevelLoader {

    public class LevelInfo {
        public String name;
        public int speed;
        public int height;
        public boolean cloud;
        public String terrain;
        public String music;

        public void dump() {
            System.out.println(">>> Level Info <<<");
            System.out.println("Name: " + this.name);
            System.out.println("Speed: " + this.speed);
            System.out.println("Height: " + this.height);
            System.out.println("Cloud: " + this.cloud);
            System.out.println("Terrain: " + this.terrain);
            System.out.println("Music: " + this.music);
            System.out.println("-------------------------");
        }
    }

    public class WayPointInfo {
        int id;
        Vector2 pos;

        WayPointInfo(int id, float x, float y) {
            this.id = id;
            this.pos = new Vector2(x, y);
        }
    }

    public class PlantInfo {
        String type;
        Vector2 pos;
        PlantInfo(String type, float x, float y) {
            this.type = type;
            this.pos = new Vector2(x, y);
        }
    }

    public Array<WayPointInfo> wayPoints;
    public Array<PlantInfo> plants;
    public LevelInfo levelInfo;

    public int plantIndex;

    private void loadWaypoints(JsonValue root) {
        JsonValue entry = root.get("WayPoints");
        wayPoints = new Array<WayPointInfo>(entry.size);
        for(JsonValue wp = entry.child; wp != null; wp = wp.next) {
            int id = wp.getInt("id");
            JsonValue array = wp.get("pos");
            float x = array.getInt(0);
            float y = array.getInt(1);
            wayPoints.add(new WayPointInfo(id, x, y));
        }
    }

    private void loadPlants(JsonValue root) {
        JsonValue entry = root.get("Plants");
        plants = new Array<PlantInfo>(entry.size);
        for(JsonValue plant = entry.child; plant != null; plant = plant.next) {
            String type = plant.getString("type");
            JsonValue array = plant.get("pos");
            float x = array.getInt(0);
            float y = array.getInt(1);
            plants.add(new PlantInfo(type, x, y));
        }
    }

    private void loadLevelInfo(JsonValue root) {
        levelInfo = new LevelInfo();
        JsonValue entry = root.get("Level");
        levelInfo.name = entry.getString("name", "na");
        levelInfo.speed = entry.getInt("speed", 0);
        levelInfo.height = entry.getInt("height", 0);
        levelInfo.cloud = entry.getBoolean("cloud", false);
        levelInfo.terrain = entry.getString("terrain", "na");
        levelInfo.music = entry.getString("music", "na");
    }

    public void load(String fileName) {
        FileHandle file = Gdx.files.internal("levels/" + fileName);

        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(file);

        loadWaypoints(root);
        loadPlants(root);
        loadLevelInfo(root);

        plantIndex = 0;

        System.out.println("level stat:");
        System.out.println("waypoints: " + wayPoints.size);
        System.out.println("plants: " + plants.size);
        this.levelInfo.dump();
        System.out.println("-----------");
    }
}
