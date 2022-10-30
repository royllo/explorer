// https://v3.nuxtjs.org/api/configuration/nuxt.config
export default defineNuxtConfig({

    // Modules installed.
    modules: ['@nuxtjs/tailwindcss'],

    // HTML head configuration.
    head: {
        // Favicons.
        link: [
            {rel: 'icon', type: 'image/x-icon', href: '/favicon.png'},
            {rel: 'icon', size: '16x16', type: 'image/x-icon', href: '/favicon-16x16.png'},
            {rel: 'icon', size: '32x32', type: 'image/x-icon', href: '/favicon-32*32.png'},
            {rel: 'apple-touch-icon', href: '/apple-touch-icon.png'}
        ]
    }
})
