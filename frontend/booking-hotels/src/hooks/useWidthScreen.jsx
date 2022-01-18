import React, { useState, useEffect } from "react";

function useWidthScreen() {
  const [widthScreen, setWidthScreen] = useState(window.innerWidth);

  const handleResize = () => setWidthScreen(window.innerWidth);

  useEffect(() => {
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);
  return widthScreen;
}

export default useWidthScreen;
