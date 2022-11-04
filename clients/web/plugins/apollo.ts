export default defineNuxtPlugin((nuxtApp) => {
    nuxtApp.hook('apollo:error', (error) => {
        console.error(error)
        // Handle different error cases
    })
})