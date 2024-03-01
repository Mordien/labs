import React, { Component } from "react";
import Accordion from "./Accordion";
import "./App.css";

class App extends Component {
  state = {
    routes: [],
  };

  async componentDidMount() {
    const response = await fetch("/routes");
    const body = await response.json();
    this.setState({ routes: body });
  }

  render() {
    const { routes } = this.state;

    return (
      <div className="App">
        <header className="App-header">
          <div className="App-intro">
            <h4>Below is the top 10 bus routes with the most stops</h4>
            <div className="accordion">
              {routes.map(({ route, stops }) => (
                <Accordion route={route} stops={stops} />
              ))}
            </div>
          </div>
        </header>
      </div>
    );
  }
}
export default App;
