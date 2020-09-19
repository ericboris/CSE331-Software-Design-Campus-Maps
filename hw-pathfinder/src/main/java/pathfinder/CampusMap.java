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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.datastructures.ShortestPath;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {
    /**
     * A graph made from the campus_buildings and campus_paths files.
     */
    private final Graph<Point, Double> campusMap;

    /**
     * A mapping of the building shortNames to building longNames.
     */
    private final Map<String, String> shortToLong;

    /**
     * A mapping of the building shortNames to building points in the graph.
     */
    private final Map<String, Point> shortToPoint;

    /**
     * Constructs a graph from the campus_paths and campus_buildings files.
     * This includes building convenience maps for accessing the graph.
     *
     * @spec.effects Constructs a new CampusMap
     */
    public CampusMap() {
        List<CampusPath> campusPathList = CampusPathsParser.parseCampusPaths("campus_paths.tsv");
        List<CampusBuilding> campusBuildingList = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        shortToLong = new HashMap<>();
        shortToPoint = new HashMap<>();
        campusMap = new Graph<>();

        // Add the buildings to the graph and the short names to the maps
        for (CampusBuilding cb : campusBuildingList) {
            Point p = new Point(cb.getX(), cb.getY());
            shortToPoint.put(cb.getShortName(), p);
            shortToLong.put(cb.getShortName(), cb.getLongName());
            campusMap.addNode(p);
        }

        // Add the paths to the graph and maps
        for (CampusPath cp : campusPathList) {
            Point src = new Point(cp.getX1(), cp.getY1());
            Point dst = new Point(cp.getX2(), cp.getY2());
            campusMap.addNode(src);
            campusMap.addNode(dst);
            campusMap.addEdge(src, dst, cp.getDistance(), true);
        }
    }

    /**
     * @param shortName The short name of a building to query.
     * @return a boolean of whether the CampusMap contains a building with the given short name
     */
    @Override
    public boolean shortNameExists(String shortName) {
        return shortToLong.containsKey(shortName);
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return the long name of the building with the given short name
     */
    @Override
    public String longNameForShort(String shortName) {
        return shortToLong.get(shortName);
    }

    /**
     * @return the mapping of the building's short names to long names
     */
    @Override
    public Map<String, String> buildingNames() {
        // Doesn't expose the rep because the map is final and Strings are immutable
        return shortToLong;
    }

    /**
     * Finds the shortest path between start and end buildings in CampusMaps.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return the shortest path between start and end
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if (startShortName == null) {
           throw new IllegalArgumentException("startShortName is null");
        } else if (endShortName == null) {
            throw new IllegalArgumentException("endShortName is null");
        } else if (!shortNameExists(startShortName)) {
            throw new IllegalArgumentException(startShortName + " not in graph");
        } else if (!shortNameExists(endShortName)) {
            throw new IllegalArgumentException(endShortName + " not in graph");
        }

        Point src = shortToPoint.get(startShortName);
        Point dst = shortToPoint.get(endShortName);

        return ShortestPath.dijkstra(campusMap, src, dst);
    }

}
