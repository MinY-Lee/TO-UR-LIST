import localAxios from './http-common';
const local = localAxios();

export async function getPublishedFeed() {
    return await local.get(`/api/feed/published`);
}

export async function getLikedFeed() {
    return await local.get(`/api/feed/liked`);
}
