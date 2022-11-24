// https://v3.nuxtjs.org/api/configuration/nuxt.config
export default defineNuxtConfig({

    // Modules installed.
    modules: ['@nuxtjs/tailwindcss', '@nuxtjs/apollo'],

    // HTML configuration.
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
    },

    // GraphQL client configuration.
    apollo: {
        clients: {
            default: {
                httpEndpoint: 'http://localhost:8080/graphql',
                httpLinkOptions: {
                    fetchOptions: {
                        mode: 'no-cors' //Cors Needed for external Cross origins, need to allow headers from server
                    },
                    credentials: "omit"
                }
            }
        },
    },

})
