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

import React, {Component} from 'react';

interface EdgeListProps {
    value: string;
    onChange(edges: any): void;  // called when a new edge list is ready
    onDrawClick(edges: any): void;
}

/**
 * A Textfield that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps> {

    onInputChange = (event: any) => {
        this.props.onChange(event.target.value);
    };

    /**
     * Draw the edges in the text box on the canvas
     */
    drawButtonClicked = () => {
        // hold any error messages in an array to display later
        let errorMessages = [];
        // these are the parsed edge field lines
        let parsedEdgeData = [];
        // prepare to loop every line in the edges field
        let edges = this.props.value.split("\n");
        for (let e = 0; e < edges.length; e++) {
            // blank lines shouldn't cause an alert so skip them
            if (edges[e] === "" || edges[e] === " ") {
                continue;
            }
            // split the line into it's space separated components
            let data = edges[e].split(" ");
            // and handle any errors that might exist in the length of the line
            if (data.length < 3) {
                errorMessages.push("Line " + (e + 1) + ": Missing a portion of the line, or missing a space.");
            } else if (data.length > 3) {
                errorMessages.push("Line " + (e + 1) + ": Extra portion of the line, or an extra space.");
            // but continue if there are the expected 3 data points
            } else {
                // get the first and second point, both of which are composed of an x and y coordinate
                // and separated by a comma
                let p1 = data[0].split(",");
                // cause an alert if either point is invalid
                if (p1.length === 2) {
                    let p2 = data[1].split(",");
                    if (p2.length === 2) {
                        // but if the points are valid then add them to the parsed edge data array
                        let coords = {
                            x1: parseInt(p1[0]),
                            y1: parseInt(p1[1]),
                            x2: parseInt(p2[0]),
                            y2: parseInt(p2[1]),
                            color: data[2],
                        };
                        parsedEdgeData.push(coords);
                        // if any of the point coordinates are invalid then cause an alert
                        if (isNaN(coords.x1) || isNaN(coords.y1) || isNaN(coords.x2) || isNaN(coords.y2)) {
                            errorMessages.push("Line " + (e + 1) + ": Coordinate(s) contain non-integer value(s).");
                        }
                        if (coords.x1 < 0 || coords.y1 < 0 || coords.x2 < 0 || coords.y2 < 0) {
                            errorMessages.push("Line " + (e + 1) + ": Coordinate(s) contain negative values(s).");
                        }
                    } else {
                        errorMessages.push("Line " + (e + 1) + ": Wrong number of inputs to the second coordinate.");
                    }
                } else {
                    errorMessages.push("Line " + (e + 1) + ": Wrong number of inputs to the first coordinate.");
                }
            }
        }
        // display alert message if any data was invalid
        if (errorMessages.length !== 0) {
            let message = "There was an error with some of your line input.\n";
            message += "For reference, the correct form for each line is: x1,y1 x2,y2 color\n\n";
            for (let e of errorMessages) {
                message += e + "\n";
            }
            alert(message);
        // otherwise, draw the lines
        } else {
            this.props.onDrawClick(parsedEdgeData);
        }
    }

    /**
     * Clear the lines drawn on the grid
     */
    clearButtonClicked = () => {
        this.props.onDrawClick([]);
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    value={this.props.value}
                    onChange={this.onInputChange}
                /> <br/>
                <button onClick={this.drawButtonClicked}>Draw</button>
                <button onClick={this.clearButtonClicked}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;