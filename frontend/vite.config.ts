import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        headers: {
            'Cross-Origin-Opener-Policy': 'same-origin',
            'Cross-Origin-Embedder-Policy': 'credentialless',
        },
        proxy: {
            '/api': {
                target: 'http://localhost:8080/',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            },
            '/authapi': {
                target: 'http://localhost:8081/',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/authapi/, ''),
            },
        },
    },
    //env directory
    envDir: '../',
});
