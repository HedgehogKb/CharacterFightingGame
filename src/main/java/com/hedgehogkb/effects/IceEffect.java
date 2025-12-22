import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.keybinds.fighterKeybinds;

public class IceEffect extends Effect {
    public IceEffect(Fighter fighter) {
        super (fighter, 0.033);
    } 

    @Override
    public void initialEffect() {} //do nothing

    @Override
    public void updateEffect(double deltaTime) {
        double figherAcc = fighter.getXAcc();
    }

    @Override
    public void removeEffect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeEffect'");
    }
    
}