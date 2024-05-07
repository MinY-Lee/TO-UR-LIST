import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import { BrowserRouter } from 'react-router-dom';

//tailwind css
import './index.css';

// carousel css
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

//variables css
import './assets/css/variables.css';
import { Provider } from 'react-redux';
import { store, persistor } from './util/store.ts';
import { PersistGate } from 'redux-persist/integration/react';

ReactDOM.createRoot(document.getElementById('root')!).render(
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </PersistGate>
    </Provider>
);
