package responses;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;

public record ListGamesResponse (ArrayList<GameData> games) {
}
