//sw.js

//install

// //fetch event
self.addEventListener("fetch", (event) => {
    if (event.request.url.includes("oauth2")) {
        console.log("detected");
        return false;
    }
});
