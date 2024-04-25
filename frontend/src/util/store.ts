import { configureStore, combineReducers } from '@reduxjs/toolkit';
import userSlice from './reduxSlices/userSlice';
import storage from 'redux-persist/lib/storage';
import { persistReducer, persistStore } from 'redux-persist';

const rootSlice = combineReducers({
    userSlice,
});

const persistConfig = {
    key: 'root',
    storage,
    whitelist: ['userSlice'],
};

const persistedReducer = persistReducer(persistConfig, rootSlice);

const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({ serializableCheck: false }),
});

const persistor = persistStore(store);
export { store, persistor };
