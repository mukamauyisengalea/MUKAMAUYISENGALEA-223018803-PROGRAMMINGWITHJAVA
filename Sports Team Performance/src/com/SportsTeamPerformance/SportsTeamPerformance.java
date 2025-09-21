package com.SportsTeamPerformance;

public class SportsTeamPerformance {
	public static void main(String[] args)  {
		Team team = new Team("Musanze FC");
		Team team2 = new Team("Amagaju");
		team.addPlayer(new Player("Jiles", 30));
		team.addPlayer(new Player("Emmy", 35));
		team.addPlayer(new Player("Japhet", 47));
		team2.addPlayer(new Player("Abrozo", 49));
		team2.addPlayer(new Player("Nyerere", 20));
		team2.addPlayer(new Player("Yves", 50));
		Team[] teams = {team, team2};
		Team teamWithHighestScore = teams[0];
		for (Team t : teams) {
			t.highestScoringPlayer();
			if (t.calculateTotalScores()> teamWithHighestScore.calculateTotalScores()) {
				teamWithHighestScore = t;
			}
		}
		System.out.println("The team "+  teamWithHighestScore.getTeamName() + "Has Highest Score: " + teamWithHighestScore.calculateTotalScores());
	}
}