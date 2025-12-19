package com.hedgehogkb;

import javax.swing.JFrame;

import com.hedgehogkb.battle.BattlePanel;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.fighter.MoveHandler;
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
        Stage testStage = new Stage();

        AnimationHandler testAnimHandler = new AnimationHandler();
        MoveHandler testMoveHandler = new MoveHandler();
        PositionHandler testPosHandler = new PositionHandler(0,0,0,0);
        Fighter testFighter = new Fighter(testAnimHandler, testMoveHandler, testPosHandler,3);

        BattlePanel panel = new BattlePanel(testStage, testFighter);

        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel.getPanel());
        frame.setVisible(true);
    }
}
