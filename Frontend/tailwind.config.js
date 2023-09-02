/** @type {import('tailwindcss').Config} */
export default {
    content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    theme: {
        extend: {
            colors: {
                primary: {
                    1: 'hsl(var(--color-primary-1) / <alpha-value>)',
                    2: 'hsl(var(--color-primary-2) / <alpha-value>)',
                    3: 'hsl(var(--color-primary-3) / <alpha-value>)'
                },
                secondary: {
                    1: 'hsl(var(--color-secondary-1) / <alpha-value>)',
                    2: 'hsl(var(--color-secondary-2) / <alpha-value>)',
                    3: 'hsl(var(--color-secondary-3) / <alpha-value>)'
                },
                background: {
                    1: 'hsl(var(--color-background-1) / <alpha-value>)',
                    2: 'hsl(var(--color-background-2) / <alpha-value>)',
                    3: 'hsl(var(--color-background-3) / <alpha-value>)'
                },
                info: {
                    1: 'hsl(var(--color-info-1) / <alpha-value>)',
                    2: 'hsl(var(--color-info-2) / <alpha-value>)',
                    3: 'hsl(var(--color-info-3) / <alpha-value>)'
                },
                error: 'hsl(var(--color-error) / <alpha-value>)',
                warning: 'hsl(var(--color-warning) / <alpha-value>)',
                success: 'hsl(var(--color-success) / <alpha-value>)',
            }
        },
    },
    plugins: [],
}

