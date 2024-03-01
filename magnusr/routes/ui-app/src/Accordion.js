import React, { useState } from "react";

const showStops = (stops) => {
  return (
    <div>
      {stops.map((stop) => (
        <span key={stop}>
          {" "}
          <div>{stop}</div>{" "}
        </span>
      ))}
    </div>
  );
};

const Accordion = ({ route, stops }) => {
  const [isActive, setIsActive] = useState(false);

  return (
    <div className="accordion-item">
      <div className="accordion-title" onClick={() => setIsActive(!isActive)}>
        <div>{route}</div>
        <div>{isActive ? "-" : "+"}</div>
      </div>
      {isActive && <div className="accordion-content">{showStops(stops)}</div>}
    </div>
  );
};

export default Accordion;
