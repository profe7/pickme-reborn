importScripts(
  "https://storage.googleapis.com/workbox-cdn/releases/3.6.3/workbox-sw.js"
);

workbox.skipWaiting();
workbox.clientsClaim();

// cache name
workbox.core.setCacheNameDetails({
  prefix: "My-awesome-cache",
  precache: "precache",
  runtime: "runtime",
});

// runtime cache
// 1. stylesheet
workbox.routing.registerRoute(
  new RegExp(".css$"),
  workbox.strategies.cacheFirst({
    cacheName: "My-awesome-cache-Stylesheets",
    plugins: [
      new workbox.expiration.Plugin({
        maxAgeSeconds: 60 * 60 * 24 * 7, // cache for one week
        maxEntries: 20, // only cache 20 request
        purgeOnQuotaError: true,
      }),
    ],
  })
);

// 2. images
workbox.routing.registerRoute(
  new RegExp(".(png|svg|jpg|jpeg)$"),
  workbox.strategies.cacheFirst({
    cacheName: "My-awesome-cache-Images",
    plugins: [
      new workbox.expiration.Plugin({
        maxAgeSeconds: 60 * 60 * 24 * 7,
        maxEntries: 50,
        purgeOnQuotaError: true,
      }),
    ],
  })
);
