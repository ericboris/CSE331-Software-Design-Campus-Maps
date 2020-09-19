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
import "./Map.css";


interface MapProps {
    path: any;
}

interface MapState {
    backgroundImage: HTMLImageElement | null;
}

/**
 * Displays the map on the canvas
 */
class Map extends Component<MapProps, MapState> {
    canvas: React.RefObject<HTMLCanvasElement>;

    /**
     * Constructs the map object
     *
     * @param props the props from the parent
     */
    constructor(props: MapProps) {
        super(props);
        this.state = {
            backgroundImage: null,
        };
        this.canvas = React.createRef();
        this.fetchAndSaveImage();
    }

    /**
     * Draws the background image if mounting is successful
     */
    componentDidMount() {
        this.drawBackgroundImage();
    }

    /**
     * Draws the background image if updating is successful
     */
    componentDidUpdate() {
        this.drawBackgroundImage();
    }

    /**
     * Loads the background image
     */
    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background,
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    /**
     * Draws the background to the canvas
     */
    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }

        if (this.props.path !== null) {
            // convert the path to a list of coordinates
            // and draw the edges and their junctions
            let edges = this.getEdges();
            for (let e = 0; e < edges.length; e++) {
                this.drawEdge(ctx, edges[e], "blue");
                this.drawPoint(ctx, edges[e], "blue");
            }

            // draw the source point
            let src = {
                x1: edges[0].x1,
                y1: edges[0].y1,
                color: "green",
            }
            this.drawPoint(ctx, src, "LawnGreen");

            // draw the destination point
            let dst = {
                x1: edges[edges.length - 1].x2,
                y1: edges[edges.length - 1].y2,
                color: "red",
            }
            this.drawPoint(ctx, dst, "red");
        }
    }

    /**
     * Draws a point on the canvas
     *
     * @param ctx the context
     * @param edge the edge to get the point coordinates from
     * @param color the color of the point
     */
    drawPoint = (ctx: any, edge: any, color: string) => {
        ctx.fillStyle = color;
        const radius = 7;
        ctx.beginPath();
        ctx.arc(edge.x1, edge.y1, radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    /**
     * Draws an edge on the canvas
     *
     * @param ctx the context
     * @param edge the edge to draw
     * @param color the color of the edge
     */
    drawEdge = (ctx: any, edge: any, color: string) => {
        ctx.beginPath();
        ctx.moveTo(edge.x1, edge.y1);
        ctx.lineTo(edge.x2, edge.y2);
        ctx.strokeStyle = color;
        ctx.lineWidth = 7;
        ctx.stroke();
    }

    /**
     * Converts the given path into usable edges
     */
    getEdges = () => {
        let jsonPath = this.props.path;
        let pathArray = jsonPath.path;
        let edges = [];
        for (let i = 0; i < pathArray.length; i++) {
            // p is the current path item
            let p = pathArray[i];
            let edge = {
                x1: parseInt(p.start.x),
                y1: parseInt(p.start.y),
                x2: parseInt(p.end.x),
                y2: parseInt(p.end.y),
            }
            edges.push(edge);
        }
        return edges;
    };

    /**
     * Renders the object
     */
    render() {
        return (
            <div id={"map"}>
                <canvas ref={this.canvas}/>
            </div>
        )
    }
}

export default Map;