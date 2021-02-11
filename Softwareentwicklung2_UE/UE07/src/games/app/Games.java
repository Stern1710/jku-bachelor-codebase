package games.app;

import games.Game;
import games.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Games {

	private static final Path CSV = Paths.get("games.csv");
	private static final String BUNDESLIGA = "BUNDESLIGA";
	private static final String BAYERN = "FC Bayern Muenchen";

	public static void main(String[] args) throws IOException {
		
		// Task 1: Parse the CSV input file into a list of Games! 
		//(solve with Files.lines within try-with-resources)
		
		List<Game> games = new ArrayList<>();		
		try (Stream<String> fs = Files.lines(CSV)) {
			fs.skip(1).forEach(entry -> games.add(Game.fromString(entry)));
		}
		
		games.forEach(System.out::println);
		System.out.println();

		// -------------------
		
		// Task 2: How many games are Bundesliga games (such games contain BUNDESLIGA)?
		// (solve with filter)
		
		long bundesligaGameCount = -1; 
		bundesligaGameCount = games.stream()
				.filter(bGame -> bGame.getInfo().contains(BUNDESLIGA))
				.count();
		
		System.out.println("There were " + bundesligaGameCount + " Bundesliga games");
		System.out.println();

		// -------------------
		
		// Task 3: What are home and away games 
		// (solve with partitionBy) 
		
		Map<Boolean, List<Game>> homeAwayMap = null; 
		homeAwayMap = games	.stream()
				.collect(Collectors.partitioningBy(haGame -> haGame.getHome().equals(BAYERN)));
		
		System.out.println("*** HOME ***"); 
		homeAwayMap.get(true).forEach(System.out::println); 
		System.out.println("*** AWAY ***"); 
		homeAwayMap.get(false).forEach(System.out::println); 
		System.out.println();

		// -------------------
		
		// Task 4: Grouping into won, lost, draw
		// (solve with groupingBy)
		
		Map<Result, List<Game>> wonLostDrawMap = null; 
		wonLostDrawMap = games.stream()
				.collect(Collectors.groupingBy(gGame -> {
					int dif = gGame.getHomeGoals() - gGame.getAwayGoals();
					if (dif < 0) return Result.LOST;
					else if (dif == 0) return Result.DRAW;
					else return Result.WON;
				}));

		System.out.println("*** WON ***"); 
		wonLostDrawMap.get(Result.WON).forEach(System.out::println); 
		System.out.println("*** DRAW ***"); 
		wonLostDrawMap.get(Result.DRAW).forEach(System.out::println); 
		System.out.println("*** LOST ***"); 
		wonLostDrawMap.get(Result.LOST).forEach(System.out::println); 
		System.out.println();
		
		// -------------------
		
		// Task 5.1: How many goals were scored per game on average? 
		//(solve with mapToInt)
		double avgGoalsPerGame1 = 0.0;
		avgGoalsPerGame1 = games.stream()
				.mapToInt(a1Game -> a1Game.goalCount())
				.average()
				.orElse(0.0);
		
		System.out.printf("Average goals per game: %.2f\n", avgGoalsPerGame1);
		
		// Task 5.2: How many goals were scored per game on average? 
		// (solve withCollectors.averagingDouble)
		double avgGoalsPerGame2 = 0.0; 
		avgGoalsPerGame2 = games.stream()
				.collect(Collectors.averagingDouble(a2Game -> a2Game.goalCount()));
		
		System.out.printf("Average goals per game: %.2f\n", avgGoalsPerGame2);
		System.out.println();

		// -------------------
		
		// Task 6: How many games did Bayern win at home
		// (home team must be equal to BAYERN)? 
		// (solve with double filter and count)
		long wonHomeGamesCount = -1; 
		wonHomeGamesCount = games.stream()
				.filter(whGame -> whGame.getHome().equals(BAYERN))
				.filter(whGame -> whGame.getHomeGoals() - whGame.getAwayGoals() > 0)
				.count();

		System.out.println(BAYERN + " won " + wonHomeGamesCount + " games at home");
		System.out.println();

		// -------------------
		
		// Task 7.1: What was the game with the least number of goals? (solve with
		// sorted and findFirst)
		Game leastNumberOfGoalsGame1 = null; 
		leastNumberOfGoalsGame1 = games.stream()
				.sorted((g1, g2) -> g1.goalCount() - g2.goalCount())
				.findFirst()
				.orElseThrow(NoSuchElementException::new);

		System.out.println("Game with least number of goals: " + leastNumberOfGoalsGame1);
		
		// Task 7.2: What was the game with the least number of goals? (solve with min
		// and Comparator.comparingInt)
		Game leastNumberOfGoalsGame2 = null; 
		leastNumberOfGoalsGame2 = games.stream()
				.min(Comparator.comparingInt(game -> game.goalCount()))
				.orElseThrow(NoSuchElementException::new);
		
		System.out.println("Game with least number of goals: " + leastNumberOfGoalsGame2);
		System.out.println();

		// -------------------
		
		// Task 8: What are all distinct starting times? 
		// (solve with a single stream andCollectors.joining)
		String startingTimesString = null; 
		startingTimesString = games.stream()
				.map(sGame -> sGame.getTime())
				.distinct()
				.collect(Collectors.joining(", "));

		System.out.println("Distinct starting times: " + startingTimesString);
		System.out.println();

		// -------------------
		
		// Task 9: Did Bayern win any away game with at least 2 goals difference
		// (away team must be equal to BAYERN)? (solve with anyMatch)
		boolean bayernWon = false; 
		bayernWon = games.stream()
				.anyMatch(bGame -> bGame.getAway().equals(BAYERN) && bGame.getAwayGoals() - bGame.getHomeGoals() >= 2);

		System.out.println("Bayern won away game with at least 2 goals difference: " + (bayernWon ? "yes" : "no"));
		System.out.println();

		// -------------------
		
		// Task 10: A buddy of you gave you the 2019 games but grouped by the home team.
		// You want to retrieve all the games as a simple list! (solve with flatMap and
		// Collectors.toList)
		Map<String, List<Game>> games2019ByHomeTeam = games.stream()
				.filter(game -> game.getDate().contains("2019"))
				.collect(Collectors.groupingBy(Game::getHome));
		
		List<Game> flattenedGames = games2019ByHomeTeam
				.values()
				.stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		flattenedGames.forEach(System.out::println);
	}
	
}
