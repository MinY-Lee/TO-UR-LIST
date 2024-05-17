import { defineConfig } from "vite";
import { VitePWA } from "vite-plugin-pwa";
import react from "@vitejs/plugin-react-swc";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),
        VitePWA({
            injectRegister: null,
            registerType: "prompt",
            workbox: {
                clientsClaim: true,
                skipWaiting: true,
                globPatterns: ["**/*.{js,tsx,ts,tsx,css,html,ico,png,svg}"],
            },
            manifest: {
                name: "tourlist",
                short_name: "tourlist",
                start_url: "/",
                display: "standalone",
                description: "당신을 위한 여행 관리 서비스, TO-UR-LIST",
                theme_color: "#5FAAD9",
                background_color: "#5FAAD9",
                lang: "ko-KR",
                orientation: "portrait",
                icons: [
                    {
                        src: "/icons/tourlist.ico",
                        type: "image/x-icon",
                        sizes: "48x48",
                        purpose: "any maskable",
                    },
                    {
                        src: "/icons/tourlist_96.png",
                        type: "image/png",
                        sizes: "96x96",
                        purpose: "any maskable",
                    },
                    {
                        src: "/icons/tourlist_128.png",
                        type: "image/png",
                        sizes: "128x128",
                        purpose: "any maskable",
                    },
                    {
                        src: "/icons/tourlist_196.png",
                        type: "image/png",
                        sizes: "196x196",
                        purpose: "any maskable",
                    },
                    {
                        src: "/icons/tourlist_512.png",
                        type: "image/png",
                        sizes: "512x512",
                        purpose: "any maskable",
                    },
                ],
            },
        }),
    ],
    server: {
        headers: {
            "Cross-Origin-Opener-Policy": "same-origin",
            "Cross-Origin-Embedder-Policy": "require-corp",
            "Cross-Origin-Resource-Policy": "cross-origin",
        },
        proxy: {
            "/api/auth": {
                target: "http://localhost:8081/",
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ""),
            },
            "/api": {
                target: "http://localhost:8080/",
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ""),
            },
            "/maps": {
                target: "https://maps.googleapis.com/",
                changeOrigin: true,
            },
        },
    },

    //env directory
    envDir: "../",
});
