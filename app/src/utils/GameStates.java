package utils;

public enum GameStates {
    MENU, PAUSE, PLAYING, LEVEL_WON, GAME_OVER;

    public static volatile GameStates current = MENU;
}
