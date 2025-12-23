package com.hedgehogkb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.fighter.PositionHandler;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.moves.Attack;
import com.hedgehogkb.fighter.moves.Move;
import com.hedgehogkb.fighter.moves.MoveHandler;
import com.hedgehogkb.fighter.moves.MoveType;
import com.hedgehogkb.fighter.moves.SingleAttack;
import com.hedgehogkb.fighter.moves.SingleMove;
import com.hedgehogkb.keybinds.KeybindSettings;

public class InputTest {
    public static Move move = null;
    public static void main(String[] args) {
        KeybindSettings keybindSettings = new KeybindSettings();
        AnimationHandler animHandler = new AnimationHandler();
        HashMap<MoveType, Move> moves = new HashMap<>();

        for (MoveType moveType : MoveType.values()) {
            moves.put(moveType, new SingleMove(moveType, 5));
        }
        moves.put(MoveType.NORMAL_ATTACK, new SingleAttack(MoveType.NORMAL_ATTACK, 1, 5));
        moves.put(MoveType.FORWARD_ATTACK, new SingleAttack(MoveType.FORWARD_ATTACK, 2, 5));
        moves.put(MoveType.DOWN_ATTACK, new SingleAttack(MoveType.DOWN_ATTACK, 3, 5));
        moves.put(MoveType.UP_ATTACK, new SingleAttack(MoveType.UP_ATTACK, 4, 5));
        moves.put(MoveType.NAIR_ATTACK, new SingleAttack(MoveType.NAIR_ATTACK, 1, 5));
        moves.put(MoveType.FORWARD_ATTACK, new SingleAttack(MoveType.FORWARD_ATTACK, 5, 5));
        MoveHandler moveHandler = new MoveHandler(moves);
        PositionHandler positionHandler = new PositionHandler(0,0,256,256);

        Fighter fighter = new Fighter(keybindSettings, animHandler, moveHandler, positionHandler, 3);

        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        frame.addKeyListener(fighter.getInputDetector());

        //Move move = null;
        Timer timer = new Timer(1000/30, e -> {
            Move curMove = moveHandler.getCurMove(0.033, 0.066, 0.066, 2, 2, 0);
            if (move == null || move != curMove) {
                move = curMove;
                System.out.println(move.getMoveType().toString());
            }
        });

        timer.start();
    }



}
