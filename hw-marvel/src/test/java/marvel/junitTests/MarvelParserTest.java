package marvel.junitTests;

import marvel.HeroAppearance;
import marvel.MarvelParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Spliterator;

import static marvel.MarvelParser.getNextElem;

public class MarvelParserTest {
    @Test
    public void testParserOnStaffHeroes() {
        String filename = "staffSuperheroes.tsv";
        Spliterator<HeroAppearance> itr = MarvelParser.parseData(filename);

        HeroAppearance ha = getNextElem(itr);
        Assert.assertEquals("Ernst-the-Bicycling-Wizard", ha.getHero());
        Assert.assertEquals("CSE331", ha.getBook());
        ha = getNextElem(itr);
        Assert.assertEquals("Ernst-the-Bicycling-Wizard", ha.getHero());
        Assert.assertEquals("CSE403", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("Notkin-of-the-Superhuman-Beard", ha.getHero());
        Assert.assertEquals("CSE331", ha.getBook());
        ha = getNextElem(itr);
        Assert.assertEquals("Notkin-of-the-Superhuman-Beard", ha.getHero());
        Assert.assertEquals("CSE403", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("Perkins-the-Magical-Singing-Instructor", ha.getHero());
        Assert.assertEquals("CSE331", ha.getBook());
        ha = getNextElem(itr);
        Assert.assertEquals("Perkins-the-Magical-Singing-Instructor", ha.getHero());
        Assert.assertEquals("CSE401", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("Grossman-the-Youngest-of-them-all", ha.getHero());
        Assert.assertEquals("CSE331", ha.getBook());
        ha = getNextElem(itr);
        Assert.assertEquals("Grossman-the-Youngest-of-them-all", ha.getHero());
        Assert.assertEquals("CSE332", ha.getBook());
        ha = getNextElem(itr);
        Assert.assertEquals("Grossman-the-Youngest-of-them-all", ha.getHero());
        Assert.assertEquals("CSE341", ha.getBook());
    }


    @Test
    public void testParserOnMarvelHeroes() {
        String filename = "marvel.tsv";
        Spliterator<HeroAppearance> itr = MarvelParser.parseData(filename);

        // Check the first characters
        HeroAppearance ha = getNextElem(itr);
        Assert.assertEquals("FROST, CARMILLA", ha.getHero());
        Assert.assertEquals("AA2 35", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("KILLRAVEN/JONATHAN R", ha.getHero());
        Assert.assertEquals("AA2 35", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("M'SHULLA", ha.getHero());
        Assert.assertEquals("AA2 35", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("24-HOUR MAN/EMMANUEL", ha.getHero());
        Assert.assertEquals("AA2 35", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("OLD SKULL", ha.getHero());
        Assert.assertEquals("AA2 35", ha.getBook());

        // Skip the middle characters
        int countOfMiddleHeroesInMarvelTsv = 96652;
        for (int i = 0; i < countOfMiddleHeroesInMarvelTsv; i++) {
            getNextElem(itr);
        }

        // Check the last characters
        // Safe to assume that if the first and last several are correct
        // everything in the middle will be too
        ha = getNextElem(itr);
        Assert.assertEquals("M'SHULLA", ha.getHero());
        Assert.assertEquals("AA2 20", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("OLD SKULL", ha.getHero());
        Assert.assertEquals("AA2 20", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("MASTER FOUR", ha.getHero());
        Assert.assertEquals("AA2 20", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("WARLORD", ha.getHero());
        Assert.assertEquals("AA2 20", ha.getBook());

        ha = getNextElem(itr);
        Assert.assertEquals("KILLRAVEN/JONATHAN R", ha.getHero());
        Assert.assertEquals("AA2 38", ha.getBook());
    }
}
