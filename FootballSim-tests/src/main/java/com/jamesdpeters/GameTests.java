package com.jamesdpeters;

import com.jamesdpeters.entities.TeamsEntity;
import com.jamesdpeters.frameworks.Team;
import com.jamesdpeters.frameworks.VersusGame;
import org.hibernate.Session;
import com.jamesdpeters.utils.SessionStore;
import com.jamesdpeters.utils.Utils;

import java.util.concurrent.ExecutionException;

public class GameTests {

    static long startTime;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IllegalAccessException, NoSuchFieldException {
        startTime = System.currentTimeMillis();
        SessionStore.setDB("GameSave");
        Session session = SessionStore.getSession();

        int team1id = 9;
        int team2id = 10;

        TeamsEntity team1ent = session.createQuery("from TeamsEntity where id = "+team1id,TeamsEntity.class).getSingleResult();
        TeamsEntity team2ent = session.createQuery("from TeamsEntity where id = "+team2id,TeamsEntity.class).getSingleResult();

        Team team1 = new Team(session,team1ent);
        Team team2 = new Team(session,team2ent);

        team1.init();
        team2.init();

        VersusGame game = new VersusGame(team1,team2);

        game.printInfo();

        double totalHome = 0;
        double totalAway = 0;

        double LIMIT = 500;
        for(int i = 0; i <= LIMIT; i++){
            int homeGoals = game.generateHomeGoals();
            int awayGoals = game.generateAwayGoals();

            totalHome += homeGoals;
            totalAway += awayGoals;

            Utils.logger.debug(game.getHome().getTeamName()+" "+homeGoals+" - "+awayGoals+" "+game.getAway().getTeamName());
        }

        totalHome /= LIMIT;
        totalAway /= LIMIT;

        Utils.logger.debug(game.getHome().getTeamName()+" "+totalHome+" - "+totalAway+" "+game.getAway().getTeamName());



    }
}
