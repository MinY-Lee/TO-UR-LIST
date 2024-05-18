//sw.js

//install

// //fetch event
self.addEventListener("fetch", (event) => {
    if (event.request.url.includes("oauth2")) {
        console.log("detected");
        return false;
    }
});

//cache 날리기
self.addEventListener("activate", (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames.map((cahceName) => {
                    return caches.delete(cahceName);
                })
            );
        })
    );
});
