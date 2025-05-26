import Cookies from 'js-cookie';

export const cookieUtils  = () => {

    /**
     * 쿠키 조회
     * @param key
     * @returns {*}
     */
    const getCookie = (key) => {
        return Cookies.get(key);
    }

    /**
     * 쿠키 저장
     * @param key
     * @param value
     * @param day
     */
    const setCookie = (key, value, day) => {
        Cookies.set(key, value, { expires: day });
    }

    /**
     * 쿠키 삭제
     * @param key
     */
    const removeCookie = (key) => {
        Cookies.remove(key);
    }

    return {
        getCookie,
        setCookie,
        removeCookie,
    }
}