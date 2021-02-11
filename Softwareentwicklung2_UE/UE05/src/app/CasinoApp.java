package app;

import inout.In;
import inout.Out;
import list.LinkedList;
import list.List;

public class CasinoApp {
	public static void main(String[] args) {
		List<GameResult> results = new LinkedList<GameResult>();
		
		In.open("casino.csv");
		while(In.available() > 0) {
			String csvLine = In.readLine();
			String[] csvLineSplitted = csvLine.split(";");
			results.add(new GameResult(csvLineSplitted[0], Double.parseDouble(csvLineSplitted[1])));
		}

		//Get every game name once
		List<String> gameNames = results
				.map(games -> games.getName())
				.distinct();			
		
		for(String gameName : gameNames) {
			List<Double> winList = results
					.filter(game -> game.getName().equals(gameName))
					.map(game -> game.getWin());
			
			int losses = winList
					.filter(win -> win < 0)
					.reduce(0, (a, b) -> ++a)
					.intValue();
			int wins = winList
					.filter(win -> win >= 0)
					.reduce(0, (a, b) -> ++a)
					.intValue();
			
			double maxLoss = winList.reduce(0.0, Math::min);	
			double maxWin = winList.reduce(0.0, Math::max);
			double sum = winList.reduce(0.0, (a, b) -> a+b);

			Out.println(gameName);
			Out.println(String.format("  Anzahl Verluste: %d", losses));
			Out.println(String.format("  Anzahl Gewinne: %d", wins));
			Out.println(String.format("  Maximaler Verlust: %,.2f", maxLoss));
			Out.println(String.format("  Maximaler Gewinn: %,.2f", maxWin));
			Out.println(String.format("  Summe Verlust / Gewinn: %,.2f", sum));
		}
	}
}
