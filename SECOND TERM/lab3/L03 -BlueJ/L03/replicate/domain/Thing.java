package domain;
import java.awt.Color;

/**
 * Interface representing a generic Thing.
 * This interface defines a set of properties and methods that each Thing must implement.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public interface Thing {

  /** Constant representing a round shape */
  public static final int ROUND = 1;

  /** Constant representing a square shape */
  public static final int SQUARE = 2;

  /**
   * Abstract method that needs to be implemented to define the behavior of the Thing.
   * Each Thing should decide its behavior by implementing this method.
   */
  public abstract void decide();

  /**
   * Method to perform a change in the Thing.
   * This is a default implementation that can be overridden by classes implementing this interface.
   */
  default void change() {
  };

  /**
   * Method to get the shape of the Thing.
   * By default, returns {@code SQUARE}.
   * @return the shape of the Thing, either {@code ROUND} or {@code SQUARE}.
   */
  default int shape() {
      return SQUARE;
  }

  /**
   * Method to get the color of the Thing.
   * By default, returns {@code Color.red}.
   * @return the color of the Thing.
   */
  default Color getColor() {
      return Color.red;
  };

  /**
   * Method to determine if the Thing is active.
   * By default, returns {@code false}.
   * @return {@code true} if the Thing is active, {@code false} otherwise.
   */
  default boolean isActive() {
      return false;
  }
}