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
import Map from "./Map";
import Controller from "./Controller";

interface AppState {
    reqResult: string | null;
    src: string;
    dst: string;
    path: any;
    buildings: any;
}

/**
 * A container for the campus maps
 */
class App extends Component<{}, AppState> {
    /**
     * Constructs the object
     *
     * @param props the properties from the parent
     */
    constructor(props: any) {
        super(props);
        this.state = {
            reqResult: null,
            src: "",
            dst: "",
            path: null,
            buildings: null,
        }
    }

    /**
     * Gets the buildings if the object mounts
     */
    componentDidMount() {
        this.getBuildings();
    }

    /**
     * Loads the buildings from the server
     */
    getBuildings = () => {
        fetch("http://localhost:4567/getBuildings")
            .then((res) => {
                if (res.status !== 200) {
                    throw Error("The request could not be processed.")
                }
                return res.json();                  // Get the json data returned by the server
            }).then((json) => {                     // possible error here
                return JSON.stringify(json);        // Convert the request to a string
        }).then((result) => {
            this.setState({
                reqResult: result,                  // Store the raw string
                buildings: JSON.parse(result),      // as well as parse it into the buildings
            })
        }).catch((error) => {
            alert(error);
        })
    }

    /**
     * Gets a path from the server
     */
    getPath = () => {
        fetch("http://localhost:4567/getPath?src=" + this.state.src + "&dst=" + this.state.dst)
            .then((res) => {
                if (res.status !== 200) {
                    throw Error("The request could not be processed.")
                }
                return res.json();                  // Get the json data returned by the server
            }).then((json) => {                     // possible error here
            return JSON.stringify(json);            // Convert the request to a string
        }).then((result) => {
            this.setState({
                path: JSON.parse(result),           // parse the result into the path
            })
        }).catch((error) => {
            alert(error);
        })
    }

    /**
     * Handles the button clicks
     *
     * @param value either the go or clear buttons were clicked
     */
    goClicked = (value: any | null) => {
        if (value === null) {
            this.setState({
                path: null,
            })
        } else if (this.state.src !== this.state.dst) {
            this.getPath();
        }
    }

    /**
     * Renders the object
     */
    render() {
        return (
            <div>
                <Controller src={this.state.src}
                            dst={this.state.dst}
                            buildings={this.state.buildings}
                            onSrcChange={(event) => {this.setState({
                                src: event.target.value,
                            })}}
                            onDstChange={(event) => {this.setState({
                                dst: event.target.value,
                            })}}
                            goClick={this.goClicked}
                />
                <Map path={this.state.path}/>
            </div>
        );
    }

}

export default App;