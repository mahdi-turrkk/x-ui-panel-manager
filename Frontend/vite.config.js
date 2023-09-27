import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {VitePWA} from "vite-plugin-pwa";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        VitePWA({
            registerType: 'autoUpdate',
            injectRegister: 'auto',
            workbox: {
                globPatterns: ['**/*.{js.css.html.ico.png.svg.json.vue.txt.woff2}'],
                cleanupOutdatedCaches: true
            },
            mode: "production",
            manifest: {
                name: 'Management panel for 3X-ui panels',
                short_name: 'Panel Manager',
                description: 'Management panel for your 3X-ui panels all in one place.',
                theme_color: '#0066ff',
                background_color: '#ffffff',
                display: 'standalone',
                scope : "/",
                start_url: "/",
                icons: [
                    {
                        src: '/icon-192*192.webp',
                        sizes: '192x192',
                        type: 'image/png'
                    },
                    {
                        src: '/icon-256*256.webp',
                        sizes: '256x256',
                        type: 'image/png'
                    },
                    {
                        src: '/icon-384*384.webp',
                        sizes: '384x384',
                        type: 'image/png'
                    },
                    {
                        src: '/icon-512*512.webp',
                        sizes: '512x512',
                        type: 'image/png'
                    },
                    {
                        src: '/icon.webp',
                        sizes: '2560x2560',
                        type: 'image/png',
                        purpose : 'any'
                    },
                    {
                        src: '/icon.webp',
                        sizes: '2560x2560',
                        type: 'image/png',
                        purpose : 'maskable'
                    },
                ],
                includeAssets: ['/index.html'],
            }
        })
    ],
})
