package com.hedgehogkb;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.hedgehogkb.battle.BattlePanel;
import com.hedgehogkb.battle.BattleRunner;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.moves.MoveHandler;
import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.fighter.PositionHandler;
import com.hedgehogkb.stage.Stage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Stage stage = new Stage();
        ArrayList<Fighter> fighters = new ArrayList<>();
        BattlePanel panel = new BattlePanel(stage, fighters);
        BattleRunner battleRunner = new BattleRunner(panel, stage, fighters);

        Thread gameThread = new Thread(battleRunner, "battle");


        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel.getPanel());
    
        frame.setVisible(true);
        gameThread.start();
    }
}
