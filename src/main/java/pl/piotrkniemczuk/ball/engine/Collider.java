package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends Transform {

    private List<Collider> enteredCollider;

    public abstract void enter(Collider transform);

    public abstract void collision(Collider transform);

    public abstract void exit(Collider transform);

    public abstract String getTag();

    public Collider(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(position, rotation, scale);
        this.enteredCollider = new ArrayList<>();
    }

    public Collider(){
        super();
        this.enteredCollider = new ArrayList<>();
    }

    public boolean isCollision(Collider collider){
        return getUp() >= collider.getDown() && getDown() <= collider.getUp() &&
                getLeft() <= collider.getRight() && getRight() >= collider.getLeft() &&
                getForward() >= collider.getBack() && getBack() <= collider.getForward();
    }

    public float getUp(){
        return position.y + (1.0f * scale.y);
    }
    public float getDown(){
        return position.y - (1.0f * scale.y);
    }
    public float getLeft(){
        return position.x - (1.0f * scale.x);
    }
    public float getRight(){
        return position.x + (1.0f * scale.x);
    }
    public float getForward(){
        return position.z + (1.0f * scale.z);
    }
    public float getBack(){
        return position.z - (1.0f * scale.z);
    }

    public List<Collider> getEnteredCollider() {
        return enteredCollider;
    }

    public void setEnteredCollider(List<Collider> enteredCollider) {
        this.enteredCollider = enteredCollider;
    }
}
