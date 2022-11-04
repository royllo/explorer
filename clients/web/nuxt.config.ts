// https://v3.nuxtjs.org/api/configuration/nuxt.config
export default defineNuxtConfig({

    // Modules installed.
    modules: ['@nuxtjs/tailwindcss'],

    // Application configuration.
    app: {
        // HTML head configuration.
        head: {
            // Title.
            title: 'Royllo - Taro asset explorer',
            // Favicons.
            link: [
                {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'},
                {rel: 'icon', type: 'image/png', href: '/favicon-16x16.png'},
                {rel: 'icon', type: 'image/png', href: '/favicon-32x32.png'},
                {rel: 'apple-touch-icon', type: 'image/png', href: '/apple-touch-icon.png'}
            ]
        }
    }

})
