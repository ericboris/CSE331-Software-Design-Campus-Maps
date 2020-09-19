package marvel;

import com.opencsv.bean.CsvBindByName;

/**
 * A mutable java bean class for superheroes and their comic book appearances.
 */
public class HeroAppearance {
    /**
     * The name of the hero.
     */
    @CsvBindByName
    private String hero;

    /**
     * The name of the book the hero appears in.
     */
    @CsvBindByName
    private String book;

    // Abstraction Function:
    // A HeroAppearance ha such that:
    // hero = the name of this hero
    // book = the name of the book this hero appears in

    // Representation Invariant:
    // (There are no null fields after the first assignment)

    /**
     * Set the hero's name.
     *
     * @param hero the name of the hero
     * @spec.effects this
     */
    public void setHero(String hero) {
        this.hero = hero;
    }

    /**
     * Set the book's name.
     *
     * @param book the name of the book
     * @spec.effects this
     */
    public void setBook(String book) {
        this.book = book;
    }

    /**
     * Get the hero's name.
     *
     * @return the hero's name
     */
    public String getHero() {
        return hero;
    }

    /**
     * Get the book the hero appears in.
     *
     * @return the book name
     */
    public String getBook() {
        return book;
    }

    /**
     * Return this object as a String.
     *
     * @return this object as a String
     */
    @Override
    public String toString() {
        return "HeroAppearance{hero=" + hero + ", book=" + book + "}";
    }
}
