/** @type {import('tailwindcss').Config} */
export default {
    content: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
    theme: {
        keyframes: {
            bounce: {
                '0%, 100%': {
                    transform: 'translateX(-1%)',
                    'animation-timing-function': 'cubic-bezier(0,0,1,1)',
                },
                '50%': {
                    transform: 'translateX(1%)',
                    'animation-timing-function': 'cubic-bezier(1,1,0,0)',
                },
            },
        },
        animation: {
            bounce: 'bounce 0.5s 1', // 1s 동안 1회만 실행
        },
        extend: {},
    },
    plugins: [],
};
