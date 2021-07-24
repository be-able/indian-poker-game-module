package indian.poker.game.utils;

public final class AuthUtils {

  private static final int BIT_FOR_AUTH_R = 0b10;
  private static final int BIT_FOR_AUTH_W = 0b100;
  private static final int BIT_FOR_INFO_R = 0b1000;
  private static final int BIT_FOR_INFO_W = 0b10000;

  public static boolean isAuthRead(long auth) {

    return 0 < (auth & BIT_FOR_AUTH_R);
  }

  public static boolean isAuthWrite(long auth) {

    return 0 < (auth & BIT_FOR_AUTH_W);
  }

  public static boolean isInfoRead(long auth) {

    return 0 < (auth & BIT_FOR_INFO_R);
  }

  public static boolean isInfoWrite(long auth) {

    return 0 < (auth & BIT_FOR_INFO_W);
  }

  public static long setAuthRead(long auth, boolean authRead) {

    int index = BIT_FOR_AUTH_R;
    return authRead ? auth | index : auth - (auth & index);
  }

  public static long setAuthWrite(long auth, boolean authWrite) {

    int index = BIT_FOR_AUTH_W;
    return authWrite ? auth | index : auth - (auth & index);
  }

  public static long setInfoRead(long auth, boolean infoRead) {

    int index = BIT_FOR_INFO_R;
    return infoRead ? auth | index : auth - (auth & index);
  }

  public static long setInfoWrite(long auth, boolean infoWrite) {

    int index = BIT_FOR_INFO_W;
    return infoWrite ? auth | index : auth - (auth & index);
  }
}
