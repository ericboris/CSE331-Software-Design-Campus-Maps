import React, {Component} from 'react';

interface  ControllerProps {
    src: any;
    dst: any;
    buildings: any;
    onSrcChange(value: any): void;
    onDstChange(value: any): void;
    goClick(value: string | null): void;
}

/**
 * A controller for the campus map
 */
class Controller extends Component<ControllerProps> {

    /**
     * Returns a list of selection drop down options built from the buildings list
     */
    generateSelections = () => {
        if (this.props.buildings != null) {
            let buildings = Object.entries(this.props.buildings);
            buildings.sort();
            let selections = [];
            // puts each building's short name into the dropdown
            for (let i = 0; i < buildings.length; i++) {
                let selection = (
                    <option value={buildings[i][0]} key={buildings[i][0]}>
                        {buildings[i][0]}
                    </option>
                );
                selections.push(selection);
            }
            return selections;
        }
        return [];
    };

    /**
     * Calls the parent's goClick method to find the shortest path
     */
    goClicked = () => {
        this.props.goClick("");
    }

    /**
     * Calls the parent's goClick method to clear the shortest path
     */
    clearClicked = () => {
        this.props.goClick(null);
    }

    /**
     * Render the object
     */
    render() {
        let selections = this.generateSelections();
        return (
            <div id="Controller">
                From:
                <select id="source" onChange={this.props.onSrcChange}>
                    {selections}
                </select>
                To:
                <select id="destination" onChange={this.props.onDstChange}>
                    {selections}
                </select>
                <button onClick={this.goClicked}>Go!</button>
                <button onClick={this.clearClicked}>Clear</button>
            </div>
        );
    }
}

export default Controller;