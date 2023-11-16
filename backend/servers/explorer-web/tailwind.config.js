/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ['./src/main/resources/templates/**/*.html'],
    theme: {
        extend: {},
    },
    plugins: [
        require('@tailwindcss/forms'),
        require('@tailwindcss/typography'),
        require("daisyui")
    ],
    // daisyUI configuration.
    daisyui: {
        themes: ["light"],
    }
}
