import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.keybinds.fighterKeybinds;

public class IceEffect extends Effect {
    private double walking_acc;
    private double sprinting_acc;
    private double standing_decel;

    public IceEffect(Fighter fighter) {
        super(fighter, 0.033);
    } 

    @Override
    public void initialEffect() {
        this.walking_acc = fighter.getWalkingAcc();
        this.sprinting_acc = fighter.getSprintingAcc();
        this.standing_decel = fighter.getStandingDecel();
        fighter.setWalkingAcc(this.walking_acc * 0.8);
        fighter.setWalkingAcc(this.sprinting_acc * 0.8);
        fighter.setWalkingAcc(this.standing_decel * 0.3);
    }

    @Override
    public void updateEffect(double deltaTime) {
        fighter.setWalkingAcc(this.walking_acc * 0.8);
        fighter.setWalkingAcc(this.sprinting_acc * 0.8);
        fighter.setWalkingAcc(this.standing_decel * 0.3);
    }

    @Override
    public void removeEffect() {
        fighter.setWalkingAcc(this.walking_acc);
        fighter.setWalkingAcc(this.sprinting_acc);
        fighter.setWalkingAcc(this.standing_decel);
    }
    
}