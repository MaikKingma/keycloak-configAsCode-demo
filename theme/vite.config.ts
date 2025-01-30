import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { keycloakify } from "keycloakify/vite-plugin";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),
        keycloakify({
            accountThemeImplementation: "none",
            keycloakVersionTargets: {
                "22-to-25": "config-as-code-theme.jar",
                "all-other-versions": false,
            }
        })
    ]
});
