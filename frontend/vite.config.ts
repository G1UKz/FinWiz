import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';
import path from 'node:path';

export default defineConfig({
  plugins: [tailwindcss(), react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '^/(auth|users|accounts|categories|transactions|actuator|swagger-ui|v3)': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
