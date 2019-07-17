package optionmenu.options;

import entities.FixtureResultEntity;
import entities.FixturesEntity;
import entities.LeaguesEntity;
import entities.TeamsEntity;
import optionmenu.menus.MainMenu;
import optionmenu.menus.Menu;
import org.apache.commons.lang3.StringUtils;
import utils.GameInfoStore;
import utils.Utils;

import java.util.List;

public class FixtureResultsListOption extends Option {

    private static final int FIXTURE_AMOUNT = 8;

    public FixtureResultsListOption(Menu menu) {
        super(menu);
    }

    @Override
    public String getTitle() {
        return "Previous Results";
    }

    @Override
    public String getDescription() {
        return "Show your teams previous results.";
    }

    @Override
    public Menu postRunMenu() {
        return getParentMenu();
    }

    @Override
    protected void run() {
        int teamId = GameInfoStore.getGameInfo().getTeam().getId();
        int gameweek = GameInfoStore.getGameInfo().getCurrentGameWeek();

        List<FixtureResultEntity> fixtures = session.createQuery("from FixtureResultEntity  where (fixture.hometeamid = "+teamId+" or fixture.awayteamid = "+teamId+") and fixture.gameweek <= "+gameweek+" and fixture.gameweek > "+(gameweek-FIXTURE_AMOUNT), FixtureResultEntity.class).list();
        if(fixtures.size() == 0) System.out.println("No previous results to show.");
        for(FixtureResultEntity fixturesEntity : fixtures){
            TeamsEntity home = fixturesEntity.getFixture().getHometeam();
            TeamsEntity away = fixturesEntity.getFixture().getAwayteam();

            Utils.printPaddedMatchResult(home,away,fixturesEntity.getHomeGoals(),fixturesEntity.getAwayGoals(),30);
        }
    }
}
