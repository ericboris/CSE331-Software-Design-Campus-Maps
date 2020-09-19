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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Spark;

import java.util.Map;

/**
 * A spark server for finding the shortest paths between buildings on the UW campus.
 */
public class SparkServer {
    /**
     * The Gson object.
     */
    private static final Gson gson = new Gson();

    /**
     * The campus map object.
     */
    private static final CampusMap cm = new CampusMap();

    /**
     * The command line method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Helps set up settings that allow the React
        // application to make requests of the Spark server.
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        // Returns the shortest path between the source and destination.
        Spark.get("/getPath", (request, response) -> {
            String src = request.queryParams("src");
            String dst = request.queryParams("dst");
            if (src == null) {
                Spark.halt(400, "A source must be provided");
            }
            if (dst == null) {
               Spark.halt(400, "A destination must be provided");
            }
            Path<Point> path = cm.findShortestPath(src, dst);
            return gson.toJson(path);
        });

        // Returns the names of the buildings.
        Spark.get("/getBuildings", (request, response) -> {
            Map<String, String> buildings = cm.buildingNames();
            return gson.toJson(buildings);
        });
    }
}
