import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from "vite-plugin-pwa";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
      vue(),
      VitePWA({
        registerType : 'autoUpdate',
        injectRegister : 'auto',
        workbox : {
          globPatterns : ['**/*.{js.css.html.ico.png.svg.json.vue.txt.woff2}'],
          cleanupOutdatedCaches  : true
        },
          manifest :{
            name : 'Management panel for 3X-ui panels',
              short_name : 'Panel Manager',
              description : 'Management panel for your 3X-ui panels all in one place.',
              theme_color : '#ffffff',
              background_color : '#ffffff',
              display : 'standalone',
            start_url : "/",
              icons : [
                {
                  src : '/src/assets/icon-192*192.png',
                  sizes : '192×192',
                  type : 'image/png'
                },
                {
                  src : '/src/assets/icon-256*256.png',
                  sizes : '256×256',
                  type : 'image/png'
                },
                {
                  src : '/src/assets/icon-384*384.png',
                  sizes : '384×384',
                  type : 'image/png'
                },
                {
                  src : '/src/assets/icon-512*512.png',
                  sizes : '512×512',
                  type : 'image/png'
                },
              ]
          }
      })
  ],
})
