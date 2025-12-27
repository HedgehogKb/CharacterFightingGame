package com.hedgehogkb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.fighter.PositionHandler;
import com.hedgehogkb.fighter.animation.Animation;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.moves.Attack;
import com.hedgehogkb.fighter.moves.ChargeAttack;
import com.hedgehogkb.fighter.moves.Move;
import com.hedgehogkb.fighter.moves.MoveHandler;
import com.hedgehogkb.fighter.moves.MoveType;
import com.hedgehogkb.fighter.moves.MultiAttack;
import com.hedgehogkb.fighter.moves.SingleAttack;
import com.hedgehogkb.fighter.moves.SingleMove;
import com.hedgehogkb.keybinds.KeybindSettings;

public class InputTest {
    public static Move move = null;
    public static boolean charging = false;
    public static void main(String[] args) {
        HashMap<MoveType, Animation> animations = new HashMap<>();
        AnimationHandler animHandler = new AnimationHandler(animations);
        HashMap<MoveType, Move> moves = new HashMap<>();

        for (MoveType moveType : MoveType.values()) {
            moves.put(moveType, new SingleMove(moveType, 5));
        }
        moves.put(MoveType.NORMAL_ATTACK, new SingleAttack(MoveType.NORMAL_ATTACK, 1, 5));
        moves.put(MoveType.FORWARD_ATTACK, new SingleAttack(MoveType.FORWARD_ATTACK, 2, 5));
        moves.put(MoveType.DOWN_ATTACK, new MultiAttack(MoveType.DOWN_ATTACK, new double[]{1, 5, 10}, new double[]{1,1,1}));
        moves.put(MoveType.UP_ATTACK, new SingleAttack(MoveType.UP_ATTACK, 4, 5));
        moves.put(MoveType.NAIR_ATTACK, new SingleAttack(MoveType.NAIR_ATTACK, 1, 5));
        moves.put(MoveType.FORWARD_ATTACK, new ChargeAttack(MoveType.FORWARD_ATTACK,5,1,5, d -> 1+d));
        MoveHandler moveHandler = new MoveHandler(moves);
        PositionHandler positionHandler = new PositionHandler(0,0);

        Fighter fighter = new Fighter(KeybindSettings.getKeybinds(1), animHandler, moveHandler, positionHandler, 3);

        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        frame.addKeyListener(fighter.getInputDetector());

        //Move move = null;
       
        Timer timer = new Timer(1000/30, e -> {
            if (move instanceof Attack a) {
                a.advanceTimers(1.0/30.0);
            }
            Move curMove = moveHandler.getCurMove(1.0/30.0, 0.066, 0.066, 2, 2, 0);
            if (move == null || move != curMove || moveHandler.charging() != charging) {
                move = curMove;
                charging = moveHandler.charging();
                System.out.println(move.getMoveType().toString() + " charging: " + charging);
            }
        });

        timer.start();
    }



}
