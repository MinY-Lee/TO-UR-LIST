export default function GetISOStringKor(date?: Date) {
    if (date) {
        return new Date(date.getTime() + 9 * 60 * 60 * 1000).toISOString();
    }
    return new Date(new Date().getTime() + 9 * 60 * 60 * 1000).toISOString();
}
