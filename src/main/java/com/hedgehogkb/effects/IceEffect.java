import com.hedgehogkb.fighter.Fighter;

public class IceEffect extends Effect {
    public IceEffect(Fighter fighter) {
        super (fighter, 0.033);
    } 

    @Override
    public void initialEffect() {} //do nothing

    @Override
    public void updateEffect(double deltaTime) {}

    @Override
    public void removeEffect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeEffect'");
    }
    
}