/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#0172bc',
          50: '#e6f3fb',
          100: '#cce7f7',
          200: '#99cff0',
          300: '#66b7e8',
          400: '#339fe1',
          500: '#0172bc',
          600: '#015b96',
          700: '#014471',
          800: '#002e4b',
          900: '#001726',
        },
        secondary: {
          DEFAULT: '#f7941e',
          50: '#fef7ed',
          100: '#fdefd6',
          200: '#fbdfad',
          300: '#f9cf84',
          400: '#f7bf5b',
          500: '#f7941e',
          600: '#d67d0f',
          700: '#a55f0b',
          800: '#744208',
          900: '#432604',
        },
      },
    },
  },
  plugins: [],
};
