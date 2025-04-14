package domain;

/**
 * The ResourceGenerator interface provides a contract for generating resources
 * in a specific lane. Implementing classes should provide the logic for
 * generating resources based on the lane number.
 */
public interface ResourceGenerator {
    public abstract Resource generateResource(int lane);
}
