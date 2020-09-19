/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

/**
 * Parser utility to load the Marvel Comics dataset.
 */
public class MarvelParser {
    // Abstraction Function and Representation Invariant
    // would usually go here but MarvelParser is static

    /**
     * Reads the Marvel Universe dataset. Each line of the input file contains a character name and a
     * comic book the character appeared in, separated by a tab character
     *
     * @param filename the file that will be read
     * @spec.requires filename is a valid file in the resources/data folder
     * @throws IllegalArgumentException if the provided filename is invalid
     * @return an iterator of HeroAppearances from the given tsv
     */
    public static Spliterator<HeroAppearance> parseData(String filename) {
        InputStream stream = MarvelParser.class.getResourceAsStream("/data/" + filename);
        if (stream == null) {
            throw new IllegalArgumentException("provided an invalid file name");
        }
        Reader reader = new BufferedReader(new InputStreamReader(stream));

        // Read the file into a spliterator
        // The spliterator contains HeroAppearance objects which are java beans
        return new CsvToBeanBuilder<HeroAppearance>(reader)
            .withType(HeroAppearance.class)
            .withSeparator('\t')
            .withIgnoreLeadingWhiteSpace(true)
            .build()
            .spliterator();
    }

    /**
     * Give spliterator next functionality like iterator.
     * Gets the next element in the spliterator, repeated calls function like next for iterator.
     *
     * @param spliterator The spliterator to get the next element of
     * @param <T> Functions on spliterator of generic types
     * @return the next element in the spliterator
     */
    public static <T> T getNextElem(Spliterator<T> spliterator) {
        List<T> result = new ArrayList<>(1);

        if (spliterator.tryAdvance(result::add)) {
            return result.get(0);
        } else {
            return null;
        }
    }
}