package com.hedgehogkb.importing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.fighter.PositionHandler;
import com.hedgehogkb.fighter.animation.Animation;
import com.hedgehogkb.fighter.animation.AnimationFrame;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.animation.MultiAnimation;
import com.hedgehogkb.fighter.animation.SingleAnimation;
import com.hedgehogkb.fighter.moves.Move;
import com.hedgehogkb.fighter.moves.MoveHandler;
import com.hedgehogkb.fighter.moves.MoveType;
import com.hedgehogkb.fighter.moves.MultiAttack;
import com.hedgehogkb.fighter.moves.SingleAttack;
import com.hedgehogkb.fighter.moves.SingleMove;
import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;
import com.hedgehogkb.keybinds.KeybindSettings;


public class Importer {
    public static Fighter importFighterInfo(File inputFile, int fighterNumber) throws IncorrectProjectException, IOException, JSONException{
        if (!inputFile.isDirectory()) throw new IncorrectProjectException("Input file is not directory.");
        String projectName = inputFile.getName();
        
        File definitions = new File(inputFile, "Definitions");
        if (!definitions.exists()) throw new IncorrectProjectException("Can't find 'Definitions' folder.");

        File spriteSheets = new File(inputFile, "Sprite_Sheets");
        if (!spriteSheets.exists()) throw new IncorrectProjectException("Can't find 'Sprite_Sheets' folder.");

        File characterFile = new File(definitions, "Character.json");
        if (!characterFile.exists()) throw new IncorrectProjectException("Can't find 'Character.json file in Definitions folder.");

        File spriteSheetFile = new File(spriteSheets, projectName+".png");
        BufferedImage spriteSheet = ImageIO.read(spriteSheetFile);

        HashMap<MoveType, Animation> animations = readAnimationJson(characterFile, spriteSheet);

        HashMap<MoveType, Move> moves = buildMovesFromAnimations(animations);

        KeybindSettings.Keybinds keybinds = KeybindSettings.getKeybinds(fighterNumber);
        AnimationHandler animationHandler = new AnimationHandler(animations);
        MoveHandler moveHandler = new MoveHandler(moves);
        PositionHandler positionHandler = new PositionHandler(0, -128);

        Fighter fighter = new Fighter(keybinds,animationHandler, moveHandler, positionHandler, 2);

        readFighterStats(fighter, characterFile);
        return fighter;
    }

    private static void readFighterStats(Fighter fighter, File characterFile) throws IOException, JSONException{
        String content = new String(Files.readAllBytes(Paths.get(characterFile.toURI())));
        JSONObject characterJson = new JSONObject(content);
        
        if (characterJson.has("stats")) {
            JSONObject stats =characterJson.getJSONObject("stats");

            if (stats.has("max jumps")) 
                fighter.setMaxJumps(stats.getInt("max jumps"));

            if (stats.has("max grounded time")) 
                fighter.setMaxGroundedTime(stats.getDouble("max grounded time"));

            if (stats.has("air deceleration")) 
                fighter.setAirDecel(stats.getDouble("air deceleration"));

            if (stats.has("sprint speed")) 
                fighter.setMaxSprintingVel(stats.getDouble("sprint speed"));

            if (stats.has("ground deceleration")) 
                fighter.setStandingDecel(stats.getDouble("ground deceleration"));

            if (stats.has("weight")) 
                fighter.setWeight(stats.getDouble("weight"));

            if (stats.has("max y velocity")) 
                fighter.setMaxYVel(stats.getDouble("max y velocity"));

            if (stats.has("walk speed")) 
                fighter.setMaxWalkingVel(stats.getDouble("walk speed"));
        }
    }

    private static HashMap<MoveType, Animation> readAnimationJson(File characterFile, BufferedImage spriteSheet) throws IOException, JSONException{
        HashMap<MoveType, Animation> animations = new HashMap<>();
        String content = new String(Files.readAllBytes(Paths.get(characterFile.toURI())));
        JSONObject characterJson = new JSONObject(content);
    
        if (characterJson.has("moves")) {
            JSONObject moves = characterJson.getJSONObject("moves");
            for (String moveName : moves.keySet()) {
                MoveType moveType;
                try {
                    moveType = MoveType.valueOf(moveName);
                } catch (IllegalArgumentException e) {continue;}
                
                JSONObject moveJson = moves.getJSONObject(moveName);
                Animation animation = readMove(moveJson, spriteSheet);
                animations.put(moveType, animation);
            }
        }

        return animations;
    }

    private static Animation readMove(JSONObject moveJson, BufferedImage spriteSheet) {
        boolean variation = false;
        if (moveJson.has("variation")) variation = moveJson.getBoolean("variation");

        if (!variation) {
            if (moveJson.has("frames")) {
                JSONArray frameJsonArray = moveJson.getJSONArray("frames");
                return new SingleAnimation(readFrames(frameJsonArray, spriteSheet));
            } else {
                return new SingleAnimation(new ArrayList<AnimationFrame>());
            }
        } else {
            JSONArray animationsJsonArray;
            if (!moveJson.has("animations")) return new MultiAnimation(new ArrayList<ArrayList<AnimationFrame>>());

            ArrayList<ArrayList<AnimationFrame>> animations = new ArrayList<>();

            animationsJsonArray = moveJson.getJSONArray("animations");
            Iterator<Object> animationIterator = animationsJsonArray.iterator();
            while (animationIterator.hasNext()) {
                ArrayList<AnimationFrame> animation = new ArrayList<>();
                JSONObject animationJson = (JSONObject) animationIterator.next();
                if (!animationJson.has("frames")) {
                    animations.add(new ArrayList<AnimationFrame>());
                    continue;
                }
                JSONArray frameJsonArray = animationJson.getJSONArray("frames");
                animations.add(readFrames(frameJsonArray, spriteSheet));
            }
            return new MultiAnimation(animations);
        }
    }

    private static ArrayList<AnimationFrame> readFrames(JSONArray framesJson, BufferedImage spriteSheet) {
        ArrayList<AnimationFrame> animationFrames = new ArrayList<>();
        for (int i = 0; i < framesJson.length(); i++) {
            JSONObject frameJson = (JSONObject) framesJson.getJSONObject(i);
            animationFrames.add(readAnimationFrame(frameJson, spriteSheet));
        }
        return animationFrames;
    }

    private static AnimationFrame readAnimationFrame(JSONObject frameJson, BufferedImage spriteSheet) {
        AnimationFrame frame = new AnimationFrame();
        if (frameJson.has("duration")) frame.duration = frameJson.getDouble("duration");
        
        if (frameJson.has("x velocity")) {
            frame.changeXVel = true;
            frame.xVel = frameJson.getDouble("x velocity");
        }

        if (frameJson.has("y velocity")) {
            frame.changeYVel = true;
            frame.yVel = frameJson.getDouble("y velocity");
        }

        if (frameJson.has("x acceleration")) {
            frame.changeXAcc = true;
            frame.xAcc = frameJson.getDouble("x acceleration");
        }

        if (frameJson.has("y acceleration")) {
            frame.changeYAcc = true;
            frame.yAcc = frameJson.getDouble("y acceleration");
        }

        if (frameJson.has("hurtboxes")) {
            for (int i = 0; i < frameJson.getJSONArray("hurtboxes").length(); i++) {
                TubeHitbox hurtbox = readHurtbox(frameJson.getJSONArray("hurtboxes").getJSONObject(i));
                if (hurtbox != null) frame.hurtboxes.add(hurtbox);
            }
        }

        if (frameJson.has("hitboxes")) {
            for (int i = 0; i < frameJson.getJSONArray("hitboxes").length(); i++) {
                AttackHitbox hitbox = readAttackHitbox(frameJson.getJSONArray("hitboxes").getJSONObject(i));
                if (hitbox != null) frame.attackHitboxes.add(hitbox);
            }
        }

        if (frameJson.has("sprite")) {
            try {
                JSONObject spriteJson = frameJson.getJSONObject("sprite");
                int startX = spriteJson.getInt("start x");
                int startY = spriteJson.getInt("start y");
                int width = spriteJson.getInt("width");
                int height = spriteJson.getInt("height");

                BufferedImage sprite = spriteSheet.getSubimage(startX, startY, width, height);
                frame.sprite = sprite;
            } catch (JSONException e) {}
        }

        return frame;
    }

    private static TubeHitbox readHurtbox(JSONObject hurtboxJson) {
        try {
            double center1X = (double) hurtboxJson.getJSONArray("center 1").getDouble(0);
            double center1Y = (double) hurtboxJson.getJSONArray("center 1").getDouble(1);

            double center2X = (double) hurtboxJson.getJSONArray("center 2").getDouble(0);
            double center2Y = (double) hurtboxJson.getJSONArray("center 2").getDouble(1);
            
            double radius = hurtboxJson.getDouble("radius");

            return new TubeHitbox(center1X, center1Y, center2X, center2Y, radius);
        } catch (JSONException e) {return null;}
    }

    private static AttackHitbox readAttackHitbox(JSONObject hitboxJson) {
        AttackHitbox attackHitbox;
        try {
            double center1X = (double) hitboxJson.getJSONArray("center 1").getDouble(0);
            double center1Y = (double) hitboxJson.getJSONArray("center 1").getDouble(1);

            double center2X = (double) hitboxJson.getJSONArray("center 2").getDouble(0);
            double center2Y = (double) hitboxJson.getJSONArray("center 2").getDouble(1);
            
            double radius = hitboxJson.getDouble("radius");
            attackHitbox = new AttackHitbox(center1X, center1Y, center2X, center2Y, radius);
        } catch (JSONException e) {return null;}
        
        if (hitboxJson.has("damage")) attackHitbox.setDamage(hitboxJson.getDouble("damage"));
        if (hitboxJson.has("stun duration")) attackHitbox.setDamage(hitboxJson.getDouble("stun duration"));
        if (hitboxJson.has("knockback amount")) attackHitbox.setDamage(hitboxJson.getDouble("knockback amount"));
        if (hitboxJson.has("knockback angle")) attackHitbox.setDamage(hitboxJson.getDouble("knockback angle"));

        return attackHitbox;
    }

    private static HashMap<MoveType, Move> buildMovesFromAnimations(HashMap<MoveType, Animation> animations) {
        HashMap<MoveType, Move> moves = new HashMap<>();
        for (MoveType moveType : animations.keySet()) {
            Animation animation = animations.get(moveType);
            
            if (animation instanceof SingleAnimation sa) {
                if (isAnAttack(moveType)) {
                    SingleAttack move = new SingleAttack(moveType, sa.getDuration(), sa.getDuration());
                    moves.put(moveType, move);
                } else {
                    SingleMove move = new SingleMove(moveType, sa.getDuration());
                    moves.put(moveType, move);
                }
            } else if (animation instanceof MultiAnimation ma) {
                MultiAttack move = new MultiAttack(moveType, ma.getDurations(), ma.getDurations());
                moves.put(moveType, move);
            }
        }
        return moves;
    }   

    /**
     * Says if a moveType should be associated with an attack. I want more flexibility that this, so I should use the json attack stuff, but I'm not sure how to incorporate this
     * when the animations don't carry this information. I'll need to come up with a different approach than building moves from animations or I could rework animation, but I do not
     * want to do that since it's an interface.
     * @deprecated
     * @param moveType
     * @return
     */
    private static boolean isAnAttack(MoveType moveType) {
        String moveName = moveType.name();
        int nameLength = moveName.length();
        if (moveName.length() < 6) return false;
        if (moveName.substring(nameLength - 6).equals("ATTACK")) return true;
        if (moveName.substring(nameLength - 7).equals("SPECIAL")) return true;
        return false;
    }
}