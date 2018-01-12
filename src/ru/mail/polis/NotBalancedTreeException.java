package ru.mail.polis;

/**
 * Created by Nechaev Mikhail
 * Since 12/12/2017.
 */
public class NotBalancedTreeException extends Exception {

  public NotBalancedTreeException(String message) {
    super(message);
  }

  public static NotBalancedTreeException create(String message, int leftHeight, int rightHeight, String nodeInfo) {
    return new NotBalancedTreeException(
            message + "\n"
                    + "leftHeight = " + leftHeight + ","
                    + "rightHeight = " + rightHeight + "\n"
                    + "nodeInfo = " + nodeInfo
    );
  }
}
